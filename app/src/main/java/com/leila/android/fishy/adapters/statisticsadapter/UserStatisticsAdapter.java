package com.leila.android.fishy.adapters.statisticsadapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.leila.android.fishy.R;
import com.leila.android.fishy.adapters.BaseAdapter;
import com.leila.android.fishy.network.models.reportsStatistics.UserStatistics;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserStatisticsAdapter extends BaseAdapter<UserStatistics, UserStatisticsAdapter.ViewHolder> {

    private Context mContext;

    public UserStatisticsAdapter(Context context, List<UserStatistics> users){
        setItems(users);
        mContext = context;

    }

    public UserStatisticsAdapter(){

    }

    public List<UserStatistics> getListUser(){
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView text_name;
        public CircleImageView letter_user;
        public TextView quantity_delivery_orders;
        public TextView num;


        public ViewHolder(View v){
            super(v);
            text_name= v.findViewById(R.id.text_name);
            letter_user= v.findViewById(R.id.letter_user);
            quantity_delivery_orders=v.findViewById(R.id.quantity_orders);
            num=v.findViewById(R.id.num);
        }
    }

    @Override
    public UserStatisticsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_statistics_user,parent,false);
        UserStatisticsAdapter.ViewHolder vh = new UserStatisticsAdapter.ViewHolder(v);
        return vh;
    }

    private void clearViewHolder(UserStatisticsAdapter.ViewHolder vh){
        if(vh.text_name!=null)
            vh.text_name.setText(null);
        if(vh.num!=null)
            vh.num.setText(null);
        if(vh.quantity_delivery_orders!=null)
            vh.quantity_delivery_orders.setText(null);


    }

    private Drawable getDrawableFirstLetter(UserStatistics user){

        //get first letter of each String item
        String firstLetter = String.valueOf(user.name.charAt(0));
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(user);
        //int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(100)
                .height(100)
                .endConfig()
                .buildRound(firstLetter, color);
        return drawable;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final UserStatisticsAdapter.ViewHolder holder, final int position){
        clearViewHolder(holder);

        final UserStatistics currentUser=getItem(position);

        holder.text_name.setText(currentUser.name);
        holder.quantity_delivery_orders.setText(String.valueOf(currentUser.cant_orders));


    }
}
