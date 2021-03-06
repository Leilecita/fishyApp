package com.example.android.fishy.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.android.fishy.Interfaces.OnStartDragListener;
import com.example.android.fishy.R;
import com.example.android.fishy.network.models.reportsOrder.SummaryDay;

import java.util.List;

public class ItemSummaryAdapter extends  BaseAdapter<SummaryDay,ItemSummaryAdapter.ViewHolder>{

    private Context mContext;


    public ItemSummaryAdapter(Context context, List<SummaryDay> items){
        setItems(items);
        mContext = context;
    }

    public ItemSummaryAdapter(){

    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        TextView cant;
        TextView name_product;
       // TextView price;
        TextView total_amount;


        public ViewHolder(View v){
            super(v);
            cant=v.findViewById(R.id.cant);
            name_product=v.findViewById(R.id.name_product);
           // price=v.findViewById(R.id.price);
            total_amount=v.findViewById(R.id.total_amount);

        }
    }

    @Override
    public ItemSummaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_day,parent,false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    private void clearViewHolder(ItemSummaryAdapter.ViewHolder vh){
        if(vh.cant!=null)
            vh.cant.setText(null);
        if(vh.name_product!=null)
            vh.name_product.setText(null);

        if(vh.total_amount!=null)
            vh.total_amount.setText(null);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        clearViewHolder(holder);

        final SummaryDay item= getItem(position);


        holder.cant.setText(getIntegerQuantity(item.totalQuantity));

//        holder.price.setText(String.valueOf(item.price));
        holder.total_amount.setText(String.valueOf(item.price*item.totalQuantity));
        holder.name_product.setText(item.nameProduct);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
            }
        });

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
