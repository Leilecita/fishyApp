package com.example.android.fishy;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.fishy.Interfaces.ItemTouchHelperViewHolder;

public class ItemViewHolder extends RecyclerView.ViewHolder implements
        ItemTouchHelperViewHolder {

    public final TextView textView;
    public final ImageView handleView;

    public ItemViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.user_address);

        handleView = (ImageView) itemView.findViewById(R.id.name);
    }

    @Override
    public void onItemSelected() {
        itemView.setBackgroundColor(Color.YELLOW);
    }

    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(0);
    }
}