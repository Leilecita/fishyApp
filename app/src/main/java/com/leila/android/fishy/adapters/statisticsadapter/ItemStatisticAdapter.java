package com.leila.android.fishy.adapters.statisticsadapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.leila.android.fishy.R;

import com.leila.android.fishy.adapters.BaseAdapter;
import com.leila.android.fishy.network.models.reportsStatistics.ItemStatistic;
import com.leila.android.fishy.network.models.reportsStatistics.ProductStatistic;
import com.leila.android.fishy.network.models.reportsStatistics.ReportStatistics;
import com.leila.android.fishy.network.models.reportsStatistics.UserStatistics;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.List;

public class ItemStatisticAdapter  extends BaseAdapter<ReportStatistics,ItemStatisticAdapter.ViewHolder> {


    private Context mContext;

    public ItemStatisticAdapter(Context context, List<ReportStatistics> items){
        setItems(items);
        mContext = context;
    }

    public ItemStatisticAdapter(){

    }


    public List<ReportStatistics> getListItem(){
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView total_amount;
        public TextView quantity_delivery_orders;
        public TextView ranking_fish;
        public TextView ranking_client;
        public TextView  month;

        public RecyclerView ranking_recycler_clients;
        public RecyclerView ranking_recycler_producst;

        public CardView cardClients;
        public CardView cardPro;


        public ViewHolder(View v){
            super(v);
            total_amount= v.findViewById(R.id.total_amount);
            quantity_delivery_orders= v.findViewById(R.id.quantity_delivery_orders);
            ranking_client= v.findViewById(R.id.client_ranking);
            ranking_fish= v.findViewById(R.id.fish_ranking);
            month= v.findViewById(R.id.month);

            ranking_recycler_clients= v.findViewById(R.id.listClients);
            ranking_recycler_producst= v.findViewById(R.id.listFish);

            cardClients= v.findViewById(R.id.card_rank_clients);
            cardPro= v.findViewById(R.id.card_rank_fish);

        }
    }

    @Override
    public ItemStatisticAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_statistics,parent,false);
        ItemStatisticAdapter.ViewHolder vh = new ItemStatisticAdapter.ViewHolder(v);

        return vh;
    }


    private void clearViewHolder(ItemStatisticAdapter.ViewHolder vh){
        if(vh.total_amount!=null)
            vh.total_amount.setText(null);
        if(vh.quantity_delivery_orders!=null)
            vh.quantity_delivery_orders.setText(null);
        if(vh.ranking_client!=null)
            vh.ranking_client.setText(null);
        if(vh.ranking_fish!=null)
            vh.ranking_fish.setText(null); if(vh.month!=null)
            vh.month.setText(null);

    }

    private String getIntegerQuantity(Double val){
        String[] arr=String.valueOf(val).split("\\.");
        int[] intArr=new int[2];
        intArr[0]=Integer.parseInt(arr[0]);
        intArr[1]=Integer.parseInt(arr[1]);

        if(intArr[1] == 0){
            return String.valueOf(intArr[0]);
        }else{
            return String.valueOf(val);
        }
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        System.out.println(String.valueOf((double) tmp / factor));
        return (double) tmp / factor;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ItemStatisticAdapter.ViewHolder holder, final int position){
        clearViewHolder(holder);

        final ReportStatistics currentItem=getItem(position);
        holder.quantity_delivery_orders.setText(String.valueOf(currentItem.cant_orders_by_month));
        holder.total_amount.setText(String.valueOf(currentItem.sum_by_month));

        holder.month.setText(currentItem.month);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cardPro.setVisibility(View.VISIBLE);
                holder.cardClients.setVisibility(View.VISIBLE);
            }
        });

        //recycler ranking clients--------------------------

        UserStatisticsAdapter adapter= new UserStatisticsAdapter(mContext,new ArrayList<UserStatistics>());
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(mContext);
        holder.ranking_recycler_clients.setLayoutManager(layoutManager);
        holder.ranking_recycler_clients.setAdapter(adapter);
        adapter.setItems(currentItem.rankingClients);

        //recycler ranking products------------------------------------
      //  RecyclerView recyclerProducts= holder.ranking_recycler_producst;
        ProductStatisticsAdapter adapterProduct= new ProductStatisticsAdapter(mContext,new ArrayList<ProductStatistic>());
        RecyclerView.LayoutManager layoutManagerProduct= new LinearLayoutManager(mContext);
        holder.ranking_recycler_producst.setLayoutManager(layoutManagerProduct);
        holder.ranking_recycler_producst.setAdapter(adapterProduct);
        adapterProduct.setItems(currentItem.rankingProducts);




    }

    private double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

}
