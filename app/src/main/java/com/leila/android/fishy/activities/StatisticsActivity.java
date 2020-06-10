package com.leila.android.fishy.activities;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hadiidbouk.charts.BarData;
import com.hadiidbouk.charts.ChartProgressBar;
import com.leila.android.fishy.Events.EventOrderState;
import com.leila.android.fishy.R;

import com.leila.android.fishy.adapters.statisticsadapter.ItemStatisticAdapter;
import com.leila.android.fishy.network.ApiClient;
import com.leila.android.fishy.network.Error;
import com.leila.android.fishy.network.GenericCallback;

import com.leila.android.fishy.network.models.reportsStatistics.ReportStatistics;
import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class StatisticsActivity extends BaseActivity {


    @Override
    public int getLayoutRes() {
        return R.layout.statistics_act;
    }

    private RecyclerView mRecyclerView;
    private ItemStatisticAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;

    private TextView textIntoCircle;


    private LinearLayout lineOrders;
    private LinearLayout lineFact;
    private LinearLayout lineRent;
    private LinearLayout lineFish;

    private TextView textOrders;
    private TextView textFact;
    private TextView textRent;
    private TextView textFish;

    public ChartProgressBar chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackArrow();

        /*mRecyclerView = this.findViewById(R.id.statistics_recycler);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ItemStatisticAdapter(this, new ArrayList<ReportStatistics>());
        mRecyclerView.setAdapter(mAdapter);

        getReport();*/

        listenerTopBar();

        menuChangeDate();

        textIntoCircle=findViewById(R.id.textIntoCircle);

       /* bottom_sheet = findViewById(R.id.bottom_sheet);
        final ImageView btn_bottom_sheet = findViewById(R.id.btn_bottom_sheet);

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setHideable(true);

        btn_bottom_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottom_sheet.getVisibility() == View.VISIBLE){
                    btn_bottom_sheet.setImageDrawable(getResources().getDrawable(R.mipmap.down2));
                    bottom_sheet.setVisibility(View.GONE);
                }else if(bottom_sheet.getVisibility() == View.GONE){
                    bottom_sheet.setVisibility(View.VISIBLE);
                    btn_bottom_sheet.setImageDrawable(getResources().getDrawable(R.mipmap.close));
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });


        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        bottom_sheet.setVisibility(View.GONE);
                        btn_bottom_sheet.setImageDrawable(getResources().getDrawable(R.mipmap.down2));
                        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        btn_bottom_sheet.setImageDrawable(getResources().getDrawable(R.mipmap.close));
                        //btn_bottom_sheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                       // btn_bottom_sheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });*/

        final FitChart fitChart = (FitChart)findViewById(R.id.fitChart);

        fitChart.setMinValue(0f);
        fitChart.setMaxValue(100f);
        fitChart.setValue(80f);

        Collection<FitChartValue> values = new ArrayList<>();
        values.add(new FitChartValue(30f, getResources().getColor(R.color.colorAccent)));
        values.add(new FitChartValue(20f, getResources().getColor(R.color.colorPrimaryFishy)));
        values.add(new FitChartValue(15f, 3));
        values.add(new FitChartValue(10f, 4));
        fitChart.setValues(values);


        //-----

        chart=this.findViewById(R.id.ChartProgressBar);

        ArrayList<BarData> dataList = new ArrayList<>();
        BarData data = new BarData();
        Double maxValue=0.0;

         /* for(int i=0;i < infoYear.listMonths.size(); ++i){
            String monthToShow= DateHelper.get().getNameMonth(infoYear.listMonths.get(i).month);
            data = new BarData(monthToShow.substring(0,3), infoYear.listMonths.get(i).outcomes.floatValue(),
                    "tot "+ValuesHelper.get().getIntegerQuantity(infoYear.listMonths.get(i).outcomes));
            dataList.add(data);

            if(infoYear.listMonths.get(i).outcomes> maxValue){
                maxValue=infoYear.listMonths.get(i).outcomes;
            }
        }*/

        chart.setMaxValue(maxValue.floatValue());
        chart.setDataList(dataList);

        chart.build();

    }


    private void menuChangeDate(){
        final LinearLayout line_years = findViewById(R.id.line_years);
        final LinearLayout line_months = findViewById(R.id.line_months);
        LinearLayout change_months = findViewById(R.id.change_month);
        LinearLayout change_yaers = findViewById(R.id.change_year);

        change_months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line_months.setVisibility(View.VISIBLE);
            }
        });

        change_yaers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line_years.setVisibility(View.VISIBLE);
            }
        });
    }

    private void listenerTopBar(){
        lineOrders= findViewById(R.id.line_orders);
        lineFact= findViewById(R.id.line_fact);
        lineRent= findViewById(R.id.lineRent);
        lineFish= findViewById(R.id.lineFish);

        textOrders= findViewById(R.id.textOrders);
        textFact= findViewById(R.id.textFact);
        textRent= findViewById(R.id.textRent);
        textFish= findViewById(R.id.textfish);

        lineOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textOrders.setBackgroundResource(R.drawable.rect_bottom);
                textFact.setBackgroundResource(R.drawable.rect_bottom_white);
                textRent.setBackgroundResource(R.drawable.rect_bottom_white);
                textFish.setBackgroundResource(R.drawable.rect_bottom_white);

                textIntoCircle.setText("Pedidos");
            }
        });

        lineFact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textOrders.setBackgroundResource(R.drawable.rect_bottom_white);
                textFact.setBackgroundResource(R.drawable.rect_bottom);
                textRent.setBackgroundResource(R.drawable.rect_bottom_white);
                textFish.setBackgroundResource(R.drawable.rect_bottom_white);

                textIntoCircle.setText("Facturación");
            }
        });
        lineRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textOrders.setBackgroundResource(R.drawable.rect_bottom_white);
                textFact.setBackgroundResource(R.drawable.rect_bottom_white);
                textRent.setBackgroundResource(R.drawable.rect_bottom);
                textFish.setBackgroundResource(R.drawable.rect_bottom_white);

                textIntoCircle.setText("Rentabilidad");
            }
        });
        lineFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textOrders.setBackgroundResource(R.drawable.rect_bottom_white);
                textFact.setBackgroundResource(R.drawable.rect_bottom_white);
                textRent.setBackgroundResource(R.drawable.rect_bottom_white);
                textFish.setBackgroundResource(R.drawable.rect_bottom);

                textIntoCircle.setText("Pescados vendidos");
            }
        });

    }


    private void getReport(){

        ApiClient.get().getReportStatistics(new GenericCallback<List<ReportStatistics>>() {
            @Override
            public void onSuccess(List<ReportStatistics> data) {
                mAdapter.pushList(data);
            }

            @Override
            public void onError(Error error) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.menu_statistics, menu);
            return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calendar:

                return true;
            case android.R.id.home:


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
