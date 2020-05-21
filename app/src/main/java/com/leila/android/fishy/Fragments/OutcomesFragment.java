package com.leila.android.fishy.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.leila.android.fishy.types.Constants;
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

    private LinearLayout bottomSheet;
    private ImageView congelado;
    private ImageView nafta;
    private ImageView mercaderia;
    private ImageView todos;

    private String selectedType="todos";

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
      //  mRecyclerView.addItemDecoration(new DividerDecoration(getContext()));

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });


        dateSelected=DateHelper.get().getActualDate();
        setHasOptionsMenu(true);

        bottomSheet = mRootView.findViewById(R.id.bottomSheet);
        final BottomSheetBehavior bsb = BottomSheetBehavior.from(bottomSheet);

        implementsPaginate();

        //bts(bsb);
        topBarListener(bottomSheet);

        return mRootView;
    }

    private void resetAll(){
        todos.setImageResource(R.drawable.circle_rose_soft);
        nafta.setImageResource(R.drawable.circle_rose_soft);
        mercaderia.setImageResource(R.drawable.circle_rose_soft);
        congelado.setImageResource(R.drawable.circle_rose_soft);
    }


    private void topBarListener(View bottomSheet){
        congelado=bottomSheet.findViewById(R.id.congelado);
        nafta=bottomSheet.findViewById(R.id.nafta);
        mercaderia=bottomSheet.findViewById(R.id.mercaderia);
        todos=bottomSheet.findViewById(R.id.todos);
        todos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedType=Constants.OUT_ALL;
                resetAll();
                clearAndList();
                todos.setImageResource(R.drawable.circle_rose);
                }
        });
        congelado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedType= Constants.OUT_CONGELADO;
                resetAll();
                clearAndList();
                congelado.setImageResource(R.drawable.circle_rose);

            }
        });
        nafta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedType=Constants.OUT_NAFTA;
                resetAll();
                clearAndList();
                nafta.setImageResource(R.drawable.circle_rose);
            }
        });
        mercaderia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedType=Constants.OUT_MERC;
                resetAll();
                clearAndList();
                mercaderia.setImageResource(R.drawable.circle_rose);
            }
        });
    }

    private void clearview(){
        mCurrentPage = 0;
        mAdapter.clear();
        hasMoreItems=true;
    }

    private void clearAndList(){
        clearview();
        listOutcomes();
    }

    public void listOutcomes(){
        loadingInProgress=true;

        ApiClient.get().getOutcomes(mCurrentPage,selectedType,new GenericCallback<List<ReportOutcome>>() {
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
        final EditText description_type= dialogView.findViewById(R.id.description_type);
        final LinearLayout other_type= dialogView.findViewById(R.id.line_other_type);
        final EditText value= dialogView.findViewById(R.id.value);
        final Button ok= dialogView.findViewById(R.id.ok);
        final TextView cancel= dialogView.findViewById(R.id.cancel);
        final Spinner spinner= dialogView.findViewById(R.id.spinner_outcome);

        List<String> spinner_time = new ArrayList<>();
        spinner_time.add("Seleccione detalle");
        spinner_time.add("Nafta");
        spinner_time.add("Patente");
        spinner_time.add("Seguro");
        spinner_time.add("Mercadería");
        spinner_time.add("Congelado");
        spinner_time.add("Mano de obra");
        spinner_time.add("Otro");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, spinner_time);

        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        int spinnerPosition = adapter.getPosition(CurrentValuesHelper.get().getLastZone());
        spinner.setSelection(spinnerPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemSelected=String.valueOf(spinner.getSelectedItem());
                if(itemSelected.equals("Otro")){
                    description_type.setText("");
                    other_type.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

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

                String type= String.valueOf(spinner.getSelectedItem());
                if(type.equals("Otro")){
                    type=description_type.getText().toString().trim();
                }

                String descr=description.getText().toString().trim();

                String valuet= value.getText().toString().trim();

                    if(ValidatorHelper.get().isTypeDouble(valuet) && !type.equals("")){

                        Outcome newOut= new Outcome(Double.valueOf(valuet),type,descr);
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
