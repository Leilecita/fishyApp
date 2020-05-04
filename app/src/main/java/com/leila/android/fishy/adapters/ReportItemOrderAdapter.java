package com.leila.android.fishy.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leila.android.fishy.R;
import com.leila.android.fishy.network.models.reportsOrder.ReportItemOrder;

import java.util.List;

public class ReportItemOrderAdapter extends BaseAdapter<ReportItemOrder,ReportItemOrderAdapter.ViewHolder> {

    private Context mContext;

    public ReportItemOrderAdapter(Context context, List<ReportItemOrder> items){
        setItems(items);
        mContext = context;
    }

    public ReportItemOrderAdapter(){

    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView name;
        public TextView cant;
        public TextView amount;

        public ViewHolder(View v){
            super(v);
            name= v.findViewById(R.id.name_product);
            cant= v.findViewById(R.id.cant);
            amount= v.findViewById(R.id.total_amount);
        }
    }

    @Override
    public ReportItemOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_order_simple,parent,false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    private void clearViewHolder(ReportItemOrderAdapter.ViewHolder vh){
        if(vh.name!=null)
            vh.name.setText(null);
        if(vh.cant!=null)
            vh.cant.setText(null);
        if(vh.amount!=null)
            vh.amount.setText(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        clearViewHolder(holder);

        final ReportItemOrder currentItem=getItem(position);

        holder.name.setText(currentItem.fish_name);
        holder.cant.setText(getIntegerQuantity(currentItem.quantity));
        holder.amount.setText(String.valueOf(round(currentItem.price*currentItem.quantity,2)));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        System.out.println(String.valueOf((double) tmp / factor));
        return (double) tmp / factor;
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

}
