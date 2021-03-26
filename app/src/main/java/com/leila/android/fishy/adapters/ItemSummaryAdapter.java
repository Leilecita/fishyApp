package com.leila.android.fishy.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.leila.android.fishy.R;
import com.leila.android.fishy.ValuesHelper;
import com.leila.android.fishy.network.models.ReportProduct;
import com.leila.android.fishy.network.models.reportsOrder.SummaryDay;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

public class ItemSummaryAdapter extends  BaseAdapter<SummaryDay,ItemSummaryAdapter.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private Context mContext;


    public ItemSummaryAdapter(Context context, List<SummaryDay> items){
        setItems(items);
        mContext = context;
    }

    public ItemSummaryAdapter(){

    }

    private long getLongFromCat(String cat){

        long l= 0;
        for(int i =0; i < cat.length(); ++i){
            char c = cat.charAt(i);

            long x = c;
            l = l +x;
        }
        return l;
    }

    @Override
    public long getHeaderId(int position) {
        if(position>= getItemCount()){
            return -1;
        } else {

            return getLongFromCat(getItem(position).category.toLowerCase()) ;


        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_header_cat_summary, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }


    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position < getItemCount()) {
            LinearLayout linear = (LinearLayout) holder.itemView;

            final SummaryDay e = getItem(position);
            String dateToShow = e.category;

            int count = linear.getChildCount();
            View v = null;
            View v2 = null;
            View v3 = null;

            for(int i=0; i<count; i++) {
                v = linear.getChildAt(i);
                if(i==0){
                    LinearLayout linear2= (LinearLayout) v;

                    int count2 = linear2.getChildCount();

                    for(int j=0; j<count2; j++) {

                        v2 = linear2.getChildAt(j);

                        if(j==0){

                            LinearLayout rel1= (LinearLayout) v2;

                            int coutn3=rel1.getChildCount();

                            for(int k=0;k< coutn3; ++k){
                                v3=rel1.getChildAt(k);

                                if(k==0){
                                    TextView t= (TextView) v3;
                                    t.setText(toLowerCase(dateToShow));
                                }
                            }
                        }
                    }
                }
            }

        }

    }


    public static class ViewHolder extends RecyclerView.ViewHolder  {
        TextView cant;
        TextView name_product;
        TextView total_amount;

        public ViewHolder(View v){
            super(v);
            cant=v.findViewById(R.id.cant);
            name_product=v.findViewById(R.id.name_product);
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
        holder.cant.setText(ValuesHelper.get().getIntegerQuantityRounded(round(item.totalQuantity,2)));

       // holder.total_amount.setText(String.valueOf(round(item.price*item.totalQuantity,2)));

        if(item.totalPrice !=null)
        holder.total_amount.setText(String.valueOf(round(item.totalPrice,2)));

        holder.name_product.setText(item.nameProduct);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        System.out.println(String.valueOf((double) tmp / factor));
        return (double) tmp / factor;
    }


    public String toLowerCase(String text){
        if (text.equals("")) {
            return text;
        }else{
            return text.substring(0,1).toUpperCase() + text.substring(1).toLowerCase();
        }
    }
}
