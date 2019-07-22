package com.leila.android.fishy.Fragments;

import android.Manifest;
import android.app.DatePickerDialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.leila.android.fishy.CurrentValuesHelper;
import com.leila.android.fishy.CustomLoadingListItemCreator;
import com.leila.android.fishy.DialogHelper;
import com.leila.android.fishy.Interfaces.OnStartDragListener;
import com.leila.android.fishy.Interfaces.OrderFragmentListener;
import com.leila.android.fishy.R;
import com.leila.android.fishy.SimpleItemTouchHelperCallback;
import com.leila.android.fishy.adapters.ReportOrderAdapter;
import com.leila.android.fishy.network.ApiClient;
import com.leila.android.fishy.network.Error;
import com.leila.android.fishy.network.GenericCallback;
import com.leila.android.fishy.network.models.Neighborhood;
import com.leila.android.fishy.network.models.reportsOrder.ReportOrder;
import com.leila.android.fishy.network.models.reportsOrder.ValuesOrderReport;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import pub.devrel.easypermissions.EasyPermissions;

public class OrdersFragment extends BaseFragment implements Paginate.Callbacks,OnStartDragListener, EasyPermissions.PermissionCallbacks, OrderFragmentListener{

    private RecyclerView mRecyclerView;
    private ReportOrderAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View mRootView;
    private TextView mDeliver_date;
    private String dateToShow;
    private ItemTouchHelper mItemTouchHelper;

    private FloatingActionButton save;

    //pagination
    private boolean loadingInProgress;
    private Integer mCurrentPage;
    private Paginate paginate;
    private boolean hasMoreItems;
    private String mQuery = "";
    private String token = "";

    TextView pendients_order;
    TextView sends_order;

   // private boolean mOnlyaddress;

    public void onClickButton(){
        if(mAdapter != null){
            changeView();
        }
    }

    public int getIconButton(){
        return R.drawable.resumen;
    }

    public int getVisibility(){
        return 0;
    }


    public String getHint() {
        return "holaadasd";
    }

    public void refreshPendientOrders(){

        loadValuesPendientsAndSend();
    }


    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onRefresh() {
        clearView();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView=inflater.inflate(R.layout.fragment_orders, container, false);
        mRecyclerView = mRootView.findViewById(R.id.list_orders);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ReportOrderAdapter(getActivity(), new ArrayList<ReportOrder>(),this);
        dateToShow=null;

        final Spinner spinner = mRootView.findViewById(R.id.spinner_zone);
        final Spinner spinnerTime = mRootView.findViewById(R.id.spinner_time);
        pendients_order = mRootView.findViewById(R.id.pendients);
        sends_order = mRootView.findViewById(R.id.sends);


        registerForContextMenu(mRecyclerView);
        setHasOptionsMenu(true);

        mAdapter.setOnOrderFragmentLister(this);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(mAdapter);

        mDeliver_date= mRootView.findViewById(R.id.deliver_date);

        loadTextDate();

        ApiClient.get().getNeighborhoods(new GenericCallback<List<Neighborhood>>() {
            @Override
            public void onSuccess(List<Neighborhood> data) {
                createSpinner(spinner,spinnerTime,createArray(data));
            }

            @Override
            public void onError(Error error) {
               // DialogHelper.get().showMessage("Error","No se pudieron cargar los barrios",getContext());
            }
        });

        implementsPaginate();

        loadValuesPendientsAndSend();

        return mRootView;
    }

    private void loadValuesPendientsAndSend(){
        if(CurrentValuesHelper.get().getLastDate()!=null){

            ApiClient.get().getValuesOrderReport(CurrentValuesHelper.get().getLastDate(), new GenericCallback<ValuesOrderReport>() {
                @Override
                public void onSuccess(ValuesOrderReport data) {

                    pendients_order.setText(String.valueOf(data.pendients));
                    sends_order.setText(String.valueOf(data.sends));
                }

                @Override
                public void onError(Error error) {
                    DialogHelper.get().showMessage("Error","no se pudo cargar valores",getActivity());
                }
            });
        }
    }


    private String generateText(List<ReportOrder> list2){

        //List<ReportOrder> list2= mAdapter.getList();
        StringBuffer cadena = new StringBuffer();
        for (int x=0;x<list2.size();x++){
            cadena =cadena.append(list2.get(x).order_id+":"+x+";");
        }
        cadena.append("}");
        return cadena.toString();
    }

    private void clearView(){
        mCurrentPage = 0;
        mAdapter.clear();
        hasMoreItems=true;
    }

    private void createSpinner(final Spinner spinner, final Spinner spinnerTime,List<String> data){

        List<String> spinner_time = new ArrayList<>();
        spinner_time.add("Todos los horarios");
        spinner_time.add("Mañana");
        spinner_time.add("Mediodía");
        spinner_time.add("Tarde");

        ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, spinner_time);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, data);

        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        adapter_time.setDropDownViewResource(R.layout.spinner_item);
        spinnerTime.setAdapter(adapter_time);

        int spinnerPosition = adapter.getPosition(CurrentValuesHelper.get().getLastZone());
        spinner.setSelection(spinnerPosition);
        int spinnerPositionTime = adapter_time.getPosition(CurrentValuesHelper.get().getLastTimeZone());
        spinnerTime.setSelection(spinnerPositionTime);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemSelected=String.valueOf(spinner.getSelectedItem());
                CurrentValuesHelper.get().setLastZone(itemSelected);

                clearView();
                listOrders(mQuery,itemSelected, CurrentValuesHelper.get().getLastTimeZone());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemSelected=String.valueOf(spinnerTime.getSelectedItem());
                CurrentValuesHelper.get().setLastTimeZone(itemSelected);
                clearView();
                listOrders(mQuery,CurrentValuesHelper.get().getLastZone(),itemSelected);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private List<String> createArray(List<Neighborhood> list){
        List<String> listN=new ArrayList<>();
        listN.add("Todas las zonas");
        for(int i=0; i < list.size();++i){
            if(list.get(i) != null && list.get(i).name != null){
                listN.add(list.get(i).getName());
            }
        }
        return listN;
    }

    private void changeView(){
        clearView();
        System.out.println("changeview");

        if(mAdapter.getOnlyAddress()){
            mAdapter.setOnlyAddress(false);
           // save.setVisibility(View.INVISIBLE);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setOnlyAddress(true);
           // save.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(mAdapter);
        }
        listOrders(mQuery,CurrentValuesHelper.get().getLastZone(), CurrentValuesHelper.get().getLastTimeZone());
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
                        dateToShow=sdayOfMonth+"-"+smonthOfYear+"-"+year;
                        CurrentValuesHelper.get().setLastDate(dateToShow);

                        clearView();
                        listOrders(mQuery,CurrentValuesHelper.get().getLastZone(),CurrentValuesHelper.get().getLastTimeZone());
                        loadValuesPendientsAndSend();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void listOrders(final String query,String zone, String time){
        loadingInProgress=true;
        this.mQuery = query;
        final String newToken = UUID.randomUUID().toString();
        this.token =  newToken;
        if(CurrentValuesHelper.get().getLastDate()!=null){
            ApiClient.get().getOrders(mCurrentPage,CurrentValuesHelper.get().getLastDate(),zone, time,
                    query, new GenericCallback<List<ReportOrder>>() {
                        @Override
                        public void onSuccess(List<ReportOrder> data) {

                            if(token.equals(newToken)){
                                System.out.println("IMPRIME"+mCurrentPage+" data size "+data.size());
                                Log.e("TOKEN", "Llega token: " + newToken);
                                if (query == mQuery) {
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
                            }else{
                                Log.e("TOKEN", "Descarta token: " + newToken);
                            }
                        }
                        @Override
                        public void onError(Error error) {
                            loadingInProgress = false;
                        }
                    });
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        checkPermissions();
        boolean onlyAddress=mAdapter.getOnlyAddress();
        System.out.println("OnResume");
        if(!isLoading()) {
            mCurrentPage = 0;
            mAdapter.clear();
            mAdapter.setOnlyAddress(onlyAddress);
            hasMoreItems=true;
            dateToShow=CurrentValuesHelper.get().getLastDate();
            loadValuesPendientsAndSend();
            listOrders("",CurrentValuesHelper.get().getLastZone(),CurrentValuesHelper.get().getLastTimeZone());
        }

    }


    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView();
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Ingrese nombre");

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.requestFocus();
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.trim().toLowerCase().equals(mQuery)) {
                    mCurrentPage = 0;
                    mAdapter.clear();
                    listOrders(newText.trim().toLowerCase(),CurrentValuesHelper.get().getLastZone(),CurrentValuesHelper.get().getLastTimeZone());
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void loadTextDate(){
        if(CurrentValuesHelper.get().getLastDate()!=null){
            mDeliver_date.setText(CurrentValuesHelper.get().getLastDate());
        }else{
            mDeliver_date.setText(mDeliver_date.getText().toString());
        }
        mDeliver_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
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
        listOrders(mQuery,CurrentValuesHelper.get().getLastZone(),CurrentValuesHelper.get().getLastTimeZone());
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }



    private final static int RC_WRITE_PERMISSIONS = 132;
    private void checkPermissions(){
        if (!EasyPermissions.hasPermissions(getContext(), Manifest.permission.READ_SMS)) {
            // Request one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage_permission),
                    RC_WRITE_PERMISSIONS, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d("PERMS", "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d("PERMS", "onPermissionsDenied:" + requestCode + ":" + perms.size());
    }

}
