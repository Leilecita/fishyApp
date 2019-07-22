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
import com.leila.android.fishy.network.ApiClient;
import com.leila.android.fishy.network.Error;
import com.leila.android.fishy.network.GenericCallback;
import com.leila.android.fishy.network.models.Order;
import com.leila.android.fishy.network.models.User;

import java.util.List;

public class OrderAdapter extends BaseAdapter<Order,OrderAdapter.ViewHolder> {

    private Context mContext;

    public OrderAdapter(Context context, List<Order> orders){
        setItems(orders);
        mContext = context;
    }

    public OrderAdapter(){

    }


    public List<Order> getListOrders(){
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView name;
        public TextView address;
        public TextView amount;
        public TextView list;



        public ViewHolder(View v){
            super(v);
            name= v.findViewById(R.id.user_name);
            address= v.findViewById(R.id.user_address);
            amount= v.findViewById(R.id.amount_order);
            list= v.findViewById(R.id.list);




        }
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_item_order,parent,false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    private void clearViewHolder(OrderAdapter.ViewHolder vh){
        if(vh.name!=null)
            vh.name.setText(null);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        clearViewHolder(holder);

        final Order currentOrder=getItem(position);

        ApiClient.get().getUser(currentOrder.user_id, new GenericCallback<User>() {
            @Override
            public void onSuccess(User data) {
                holder.name.setText(data.name);
                holder.address.setText(data.address);

            }

            @Override
            public void onError(Error error) {

            }
        });
    }
}
