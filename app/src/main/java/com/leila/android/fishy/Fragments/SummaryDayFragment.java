package com.leila.android.fishy.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leila.android.fishy.CurrentValuesHelper;
import com.leila.android.fishy.Events.EventOrderState;
import com.leila.android.fishy.R;
import com.leila.android.fishy.adapters.ItemSummaryAdapter;
import com.leila.android.fishy.network.ApiClient;
import com.leila.android.fishy.network.Error;
import com.leila.android.fishy.network.GenericCallback;
import com.leila.android.fishy.network.models.reportsOrder.SummaryDay;
import com.leila.android.fishy.network.models.reportsOrder.ValuesDay;
import com.leila.android.fishy.types.Constants;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SummaryDayFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private ItemSummaryAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View mRootView;
    private TextView mDeliver_date;
    private TextView mSumTot;
    private TextView mSumTotCard;
    private TextView mSumTotCash;
    private TextView mSumTotTrans;
    private TextView mSumDone;
    private TextView mSumPendient;

    private ImageView mView;
    private LinearLayout mCompleteViewTot;
    private LinearLayout bottomSheet;

    public void onClickButton(){  }
    public int getIconButton(){
        return R.drawable.addperson;
    }

    public int getVisibility(){
        return 4;
    }
    public SummaryDayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onRefresh() {

    }
    public String getHint() {
        return "hola";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView=inflater.inflate(R.layout.fragment_summary_day2, container, false);

        mRecyclerView = mRootView.findViewById(R.id.list_summary);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ItemSummaryAdapter(getActivity(),new ArrayList<SummaryDay>());

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });


        registerForContextMenu(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        setHasOptionsMenu(true);

        loadSummaryDay();
        mDeliver_date= mRootView.findViewById(R.id.deliver_date);
/*

        mSumDone= mRootView.findViewById(R.id.sumDone);
        mSumPendient= mRootView.findViewById(R.id.sumPendient);
        mSumTot= mRootView.findViewById(R.id.sumTot);
        mSumTotCard= mRootView.findViewById(R.id.sumTotCard);
        mSumTotCash= mRootView.findViewById(R.id.sumTotCash);
        mSumTotTrans= mRootView.findViewById(R.id.sumTotTrans);

        mView=mRootView.findViewById(R.id.view);
        mCompleteViewTot=mRootView.findViewById(R.id.completeViewTot);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCompleteViewTot.getVisibility() == View.GONE){
                    mCompleteViewTot.setVisibility(View.VISIBLE);
                    mView.setImageResource(R.drawable.viewmore);
                }else{
                    mCompleteViewTot.setVisibility(View.GONE);
                    mView.setImageResource(R.drawable.viewless);
                }
            }
        });
*/

        mDeliver_date.setText(CurrentValuesHelper.get().getSummaryDate());
        mDeliver_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });

        bottomSheet = mRootView.findViewById(R.id.bottomSheet);
        final BottomSheetBehavior bsb = BottomSheetBehavior.from(bottomSheet);

        EventBus.getDefault().register(this);
        topBarListener(bottomSheet);
        return mRootView;
    }

    private void topBarListener(View bottomSheet){
        mSumDone= bottomSheet.findViewById(R.id.sumDone);
        mSumPendient= bottomSheet.findViewById(R.id.sumPendient);
        mSumTot= bottomSheet.findViewById(R.id.sumTot);
        mSumTotCard= bottomSheet.findViewById(R.id.sumTotCard);
        mSumTotCash= bottomSheet.findViewById(R.id.sumTotCash);
        mSumTotTrans= bottomSheet.findViewById(R.id.sumTotTrans);
        ImageView tranf= bottomSheet.findViewById(R.id.transf);
        tranf.setColorFilter(getResources().getColor(R.color.colorAccent));

        mView=bottomSheet.findViewById(R.id.view);
        mCompleteViewTot=bottomSheet.findViewById(R.id.completeViewTot);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCompleteViewTot.getVisibility() == View.GONE){
                    mCompleteViewTot.setVisibility(View.VISIBLE);
                    mView.setImageResource(R.drawable.viewless);
                }else{
                    mCompleteViewTot.setVisibility(View.GONE);
                    mView.setImageResource(R.drawable.viewmore);
                }
            }
        });


    }


    public void loadSummaryDay(){
        listItemsDay();
        getValuesDay();
    }

    @Override
    public void onResume() {

        super.onResume();
        loadSummaryDay();
    }

    @Subscribe
    public void onEvent(EventOrderState event){
        if(event.getDate().equals(CurrentValuesHelper.get().getSummaryDate())){
            loadSummaryDay();
            Toast.makeText(getContext(), event.getState(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {

        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void getValuesDay(){

        String date=CurrentValuesHelper.get().getSummaryDate();
        if(date!=null){
            ApiClient.get().getValuesDay(date, new GenericCallback<ValuesDay>() {
                @Override
                public void onSuccess(ValuesDay data) {
                    mSumDone.setText(String.valueOf(round(data.sumDone,1)));
                    mSumPendient.setText(String.valueOf(round(data.sumPendient,1)));
                    mSumTot.setText(String.valueOf(round(data.sumTot,1)));

                    mSumTotCard.setText(String.valueOf(data.sumTotCard));
                    mSumTotCash.setText(String.valueOf(data.sumTotCash));
                    mSumTotTrans.setText(String.valueOf(data.sumTotTrans));
                }

                @Override
                public void onError(Error error) {

                }
            });
        }
    }

    private void selectDate(){
        final DatePickerDialog datePickerDialog;
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        final int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        datePickerDialog = new DatePickerDialog(getContext(),R.style.datepicker,
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
                        mDeliver_date.setText(sdayOfMonth+"-"+smonthOfYear+"-"+year);
                        CurrentValuesHelper.get().setSummaryDate(sdayOfMonth+"-"+smonthOfYear+"-"+year);
                        listItemsDay();
                        getValuesDay();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void listItemsDay(){
        String date=CurrentValuesHelper.get().getSummaryDate();
        if(date!=null){
            ApiClient.get().getSummaryDay(date, new GenericCallback<List<SummaryDay>>() {
                @Override
                public void onSuccess(List<SummaryDay> data) {
                    mAdapter.setItems(data);
                }

                @Override
                public void onError(Error error) {

                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
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
