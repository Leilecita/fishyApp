package com.example.android.fishy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.android.fishy.CustomLoadingListItemCreator;
import com.example.android.fishy.Interfaces.OnStartDragListener;
import com.example.android.fishy.R;
import com.example.android.fishy.SimpleItemTouchHelperCallback;
import com.example.android.fishy.adapters.ReportOrderAdapter;
import com.example.android.fishy.network.ApiClient;
import com.example.android.fishy.network.Error;
import com.example.android.fishy.network.GenericCallback;
import com.example.android.fishy.network.models.User;
import com.example.android.fishy.network.models.reportsOrder.ReportOrder;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;

import java.util.ArrayList;
import java.util.List;

public class UserHistoryOrders extends BaseActivity implements Paginate.Callbacks,OnStartDragListener{

    private RecyclerView mRecyclerView;
    private ReportOrderAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //pagination
    private boolean loadingInProgress;
    private Integer mCurrentPage;
    private Paginate paginate;
    private boolean hasMoreItems;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
    public static void start(Context mContext, User user){
        Intent i=new Intent(mContext, UserHistoryOrders.class);
        i.putExtra("ID",user.id);
        i.putExtra("NAME",user.getName());
        i.putExtra("DIRECCION",user.address);
        mContext.startActivity(i);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_users_history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackArrow();

        mRecyclerView = findViewById(R.id.list_orders);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ReportOrderAdapter(this, new ArrayList<ReportOrder>(),this);
        mAdapter.setHistoryUser(true);

        registerForContextMenu(mRecyclerView);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(mAdapter);


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

        //implementsPaginate();

    }

    private void listHistoryUser(){

        loadingInProgress=true;

        ApiClient.get().getOrdersReportByUserIdByPage(mCurrentPage, getIntent().getLongExtra("ID", -1), new GenericCallback<List<ReportOrder>>() {
            @Override
            public void onSuccess(List<ReportOrder> data) {

                if(data.size() == 0){
                    hasMoreItems = false;
                }else{
                    int prevSize = mAdapter.getItemCount();

                    mAdapter.pushList(data);
                    mCurrentPage++;
                    if(prevSize == 0){
                        layoutManager.scrollToPosition(0);
                    }

                }
                loadingInProgress=false;
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
        listHistoryUser();
    }

    @Override
    public boolean isLoading() {
        return loadingInProgress;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return !hasMoreItems;
    }

}
