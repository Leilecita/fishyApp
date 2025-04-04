package com.leila.android.fishy.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leila.android.fishy.CustomLoadingListItemCreator;
import com.leila.android.fishy.R;
import com.leila.android.fishy.adapters.ItemSummaryAdapter;
import com.leila.android.fishy.network.ApiClient;
import com.leila.android.fishy.network.Error;
import com.leila.android.fishy.network.GenericCallback;
import com.leila.android.fishy.network.models.reportsOrder.SummaryDay;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SummaryPeriodByFishProduct extends BaseActivity implements Paginate.Callbacks{

    @Override
    public int getLayoutRes() {
        return R.layout.summary_kg_fish_period;
    }


    private RecyclerView mRecyclerView;
    private ItemSummaryAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //pagination
    private boolean loadingInProgress;
    private Integer mCurrentPage;
    private Paginate paginate;
    private boolean hasMoreItems;

    private String since;
    private String to;
    private TextView dateSince;
    private TextView dateTo;


    private TextView amountSales;
    private TextView amountKg;

    private LinearLayout bottomSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackArrow();

        setTitle("Resumen productos vendidos");

        mRecyclerView = this.findViewById(R.id.list_summary);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ItemSummaryAdapter(this,new ArrayList<SummaryDay>());

        registerForContextMenu(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });

        registerForContextMenu(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        dateSince = this.findViewById(R.id.since);
        dateTo = this.findViewById(R.id.to);

        dateSince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(dateSince,"si");
            }
        });

        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(dateTo,"no");
            }
        });

        since="";
        to="";

        amountKg = this.findViewById(R.id.sumkg);
        amountSales= this.findViewById(R.id.sumSales);

        getTotalAmountSales();
        implementsPaginate();
    }

    private void clearview() {
        mCurrentPage = 0;
        mAdapter.clear();
        hasMoreItems = true;
    }

    public void getTotalAmountSales(){
        ApiClient.get().getAmountSalesByPeriod(since, to, new GenericCallback<SummaryDay>() {
            @Override
            public void onSuccess(SummaryDay data) {
                amountKg.setText(String.valueOf(round(data.totalQuantity,1)));
                amountSales.setText(String.valueOf(round(data.totalPrice,1)));
            }

            @Override
            public void onError(Error error) {

            }
        });

    }

    public void list() {
        loadingInProgress = true;

        ApiClient.get().getSummaryByPeriod(mCurrentPage,since,to, new GenericCallback<List<SummaryDay>>() {
            @Override
            public void onSuccess(List<SummaryDay> data) {

                if (data.size() == 0) {
                    hasMoreItems = false;
                }else{
                    int prevSize = mAdapter.getItemCount();
                    mAdapter.pushList(data);
                    mCurrentPage++;
                    if(prevSize == 0){
                        layoutManager.scrollToPosition(0);
                    }
                }
                loadingInProgress = false;
            }

            @Override
            public void onError(Error error) {
                loadingInProgress = false;
            }
        });
    }

    private void implementsPaginate(){

        loadingInProgress=false;
        mCurrentPage=0;
        hasMoreItems = true;

        paginate= Paginate.with(mRecyclerView,this)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(new CustomLoadingListItemCreator())
                .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
                    @Override
                    public int getSpanSize() {
                        return 0;
                    }
                })
                .build();
    }

    @Override
    public void onLoadMore() {
        list();
    }

    @Override
    public boolean isLoading() {
        return loadingInProgress;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return !hasMoreItems;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectDate(final TextView date, final String issince){
        final DatePickerDialog datePickerDialog;
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        final int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        datePickerDialog = new DatePickerDialog(this,R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String sdayOfMonth = String.valueOf(dayOfMonth);
                        if (sdayOfMonth.length() == 1) {
                            sdayOfMonth = "0" + dayOfMonth;
                        }
                        String smonthOfYear = String.valueOf(monthOfYear + 1);
                        if (smonthOfYear.length() == 1) {
                            smonthOfYear = "0" + smonthOfYear;
                        }
                        date.setText(sdayOfMonth+"-"+smonthOfYear+"-"+year);

                        if(issince.equals("si")){
                            since = year+"-"+smonthOfYear+"-"+sdayOfMonth+" 00:00:00";
                        }else{
                            to = year+"-"+smonthOfYear+"-"+sdayOfMonth+" 00:00:00";
                        }

                        clearview();
                        getTotalAmountSales();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        System.out.println(String.valueOf((double) tmp / factor));
        return (double) tmp / factor;
    }

}
