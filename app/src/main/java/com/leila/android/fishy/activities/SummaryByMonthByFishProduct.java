package com.leila.android.fishy.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leila.android.fishy.CustomLoadingListItemCreator;
import com.leila.android.fishy.DateHelper;
import com.leila.android.fishy.R;
import com.leila.android.fishy.adapters.ItemSummaryAdapter;
import com.leila.android.fishy.adapters.ReportIncomeAdapter;
import com.leila.android.fishy.network.ApiClient;
import com.leila.android.fishy.network.Error;
import com.leila.android.fishy.network.GenericCallback;
import com.leila.android.fishy.network.models.Income;
import com.leila.android.fishy.network.models.ReportIncome;
import com.leila.android.fishy.network.models.reportsOrder.SummaryDay;
import com.leila.android.fishy.types.Constants;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SummaryByMonthByFishProduct extends BaseActivity implements Paginate.Callbacks{

    @Override
    public int getLayoutRes() {
        return R.layout.fragments_incomes;
    }



    private RecyclerView mRecyclerView;
    private ItemSummaryAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View mRootView;

    //pagination
    private boolean loadingInProgress;
    private Integer mCurrentPage;
    private Paginate paginate;
    private boolean hasMoreItems;

    private String dateSelected;
    private String selectedType= Constants.INCOME_ALL;

    private ImageView todos;
    private ImageView entrega;
    private ImageView personal;

    private LinearLayout bottomSheet;
    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackArrow();

        setTitle("Kg");

        mRecyclerView = this.findViewById(R.id.list_incomes);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ItemSummaryAdapter(this,new ArrayList<SummaryDay>());

        registerForContextMenu(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        //STICKY

        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);

        // Add decoration for dividers between list items
        // mRecyclerView.addItemDecoration(new DividerDecoration(getContext()));

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });


        dateSelected = DateHelper.get().getActualDate();

        implementsPaginate();
        bottomSheet = this.findViewById(R.id.bottomSheet);

        final BottomSheetBehavior bsb = BottomSheetBehavior.from(bottomSheet);
        topBarListener(bottomSheet);
    }

    private void resetAll(){
        todos.setImageResource(R.drawable.circle_rose_soft);
        entrega.setImageResource(R.drawable.circle_rose_soft);
        personal.setImageResource(R.drawable.circle_rose_soft);
    }
    private void topBarListener(View bottomSheet){
        entrega=bottomSheet.findViewById(R.id.entrega);
        personal=bottomSheet.findViewById(R.id.personal);
        todos=bottomSheet.findViewById(R.id.todos);
        todos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedType=Constants.INCOME_ALL;
                clearAndList();
                resetAll();
                todos.setImageResource(R.drawable.circle_rose);
            }
        });
        entrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedType= Constants.INCOME_ENTREGA;
                clearAndList();
                resetAll();
                entrega.setImageResource(R.drawable.circle_rose);

            }
        });
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedType=Constants.INCOME_PERSONAL;
                clearAndList();
                resetAll();

                personal.setImageResource(R.drawable.circle_rose);
            }
        });

    }

    private void clearAndList(){
        clearview();
        list();
    }

    private void clearview() {
        mCurrentPage = 0;
        mAdapter.clear();
        hasMoreItems = true;
    }

    public void list() {
        loadingInProgress = true;

        ApiClient.get().getSummaryByMonth(mCurrentPage,"since","to", new GenericCallback<List<SummaryDay>>() {
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

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectDate(final TextView date){
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
                        dateSelected=year+"-"+smonthOfYear+"-"+sdayOfMonth+" "+DateHelper.get().getTime(DateHelper.get().getActualDate());

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}