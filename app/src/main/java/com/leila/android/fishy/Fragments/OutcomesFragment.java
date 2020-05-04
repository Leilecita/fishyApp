package com.leila.android.fishy.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leila.android.fishy.CurrentValuesHelper;
import com.leila.android.fishy.CustomLoadingListItemCreator;
import com.leila.android.fishy.DateHelper;
import com.leila.android.fishy.DividerDecoration;
import com.leila.android.fishy.R;
import com.leila.android.fishy.ValidatorHelper;


import com.leila.android.fishy.adapters.ReportOutcomeAdapter;
import com.leila.android.fishy.network.ApiClient;
import com.leila.android.fishy.network.Error;
import com.leila.android.fishy.network.GenericCallback;
import com.leila.android.fishy.network.models.Outcome;

import com.leila.android.fishy.network.models.ReportOutcome;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OutcomesFragment extends BaseFragment implements Paginate.Callbacks{

    private RecyclerView mRecyclerView;
    private ReportOutcomeAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View mRootView;

    //pagination
    private boolean loadingInProgress;
    private Integer mCurrentPage;
    private Paginate paginate;
    private boolean hasMoreItems;

    private String dateSelected;


    public void onClickButton(){ createOutcome(); }
    public int getIconButton(){
        return R.drawable.add;
    }
    public int getVisibility(){
        return 0;
    }

    public void onClickAction(){

    }

    @Override
    public void onRefresh() {

    }

    public OutcomesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView=inflater.inflate(R.layout.fragment_spendings, container, false);

        mRecyclerView = mRootView.findViewById(R.id.list_outcomes);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ReportOutcomeAdapter(getActivity(), new ArrayList<ReportOutcome>());

        registerForContextMenu(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        //STICKY

        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);

        // Add decoration for dividers between list items
        mRecyclerView.addItemDecoration(new DividerDecoration(getContext()));

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });


        dateSelected=DateHelper.get().getActualDate();
        setHasOptionsMenu(true);

        implementsPaginate();

        return mRootView;
    }

    private void clearview(){
        mCurrentPage = 0;
        mAdapter.clear();
        hasMoreItems=true;
    }

    public void listOutcomes(){
        loadingInProgress=true;

        ApiClient.get().getOutcomes(mCurrentPage,new GenericCallback<List<ReportOutcome>>() {
            @Override
            public void onSuccess(List<ReportOutcome> data) {
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

        paginate= Paginate.with(mRecyclerView, this)
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
       listOutcomes();
    }

    @Override
    public boolean isLoading() {
        return loadingInProgress;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return !hasMoreItems;
    }


    private void createOutcome(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.create_outcome, null);
        builder.setView(dialogView);

        final TextView date= dialogView.findViewById(R.id.date);
        final EditText description= dialogView.findViewById(R.id.description);
        final EditText value= dialogView.findViewById(R.id.value);
        final Button ok= dialogView.findViewById(R.id.ok);
        final TextView cancel= dialogView.findViewById(R.id.cancel);

        date.setText(DateHelper.get().getOnlyDate(DateHelper.get().getActualDateToShow()));

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(date);
            }
        });

        final AlertDialog dialog = builder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String descr=description.getText().toString().trim();
                String valuet= value.getText().toString().trim();

                    if(ValidatorHelper.get().isTypeDouble(valuet) ){

                        Outcome newOut= new Outcome(Double.valueOf(valuet),descr);
                        newOut.created=dateSelected;

                        ApiClient.get().postOutcome(newOut, new GenericCallback<Outcome>() {
                            @Override
                            public void onSuccess(Outcome data) {
                                clearview();
                            }

                            @Override
                            public void onError(Error error) {

                            }
                        });
                        dialog.dismiss();

                    }else{
                        Toast.makeText(dialogView.getContext(),"Tipo de dato no valido ", Toast.LENGTH_LONG).show();
                    }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void selectDate(final TextView date){
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
                        date.setText(sdayOfMonth+"-"+smonthOfYear+"-"+year);
                        dateSelected=year+"-"+smonthOfYear+"-"+sdayOfMonth+" "+DateHelper.get().getTime(DateHelper.get().getActualDate());

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


}
