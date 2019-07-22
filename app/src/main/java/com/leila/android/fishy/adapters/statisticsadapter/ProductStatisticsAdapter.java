package com.leila.android.fishy.adapters.statisticsadapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leila.android.fishy.R;
import com.leila.android.fishy.adapters.BaseAdapter;
import com.leila.android.fishy.network.models.reportsStatistics.ProductStatistic;

import java.util.List;

public class ProductStatisticsAdapter extends BaseAdapter<ProductStatistic, ProductStatisticsAdapter.ViewHolder> {

    private Context mContext;

    public ProductStatisticsAdapter(Context context, List<ProductStatistic> products){
        setItems(products);
        mContext = context;
    }

    public ProductStatisticsAdapter(){

    }

    public List<ProductStatistic> getListProduct(){
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView name;
        public TextView soldedCant;


        public ViewHolder(View v){
            super(v);
            name= v.findViewById(R.id.name_product);
            soldedCant= v.findViewById(R.id.quantity_kg);
        }
    }

    @Override
    public ProductStatisticsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_statistics_product,parent,false);
        ProductStatisticsAdapter.ViewHolder vh = new ProductStatisticsAdapter.ViewHolder(v);

        return vh;
    }

    private void clearViewHolder(ProductStatisticsAdapter.ViewHolder vh){
        if(vh.name!=null)
            vh.name.setText(null);
        if(vh.soldedCant!=null)
            vh.soldedCant.setText(null);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ProductStatisticsAdapter.ViewHolder holder, final int position){
        clearViewHolder(holder);

        final ProductStatistic currentProduct=getItem(position);
        holder.soldedCant.setText(String.valueOf(currentProduct.quantity));
        holder.name.setText(currentProduct.fish_name);

    }
}
