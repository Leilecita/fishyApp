package com.leila.android.fishy.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leila on 19/7/18.
 */

public abstract class BaseAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private List<T> mList = new ArrayList<>();

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public T getItem(int position) {
        return mList.get(position);
    }

    public void pushItem(T item){
        mList.add(item);
        notifyItemInserted(this.mList.size()-1);
    }

    public void pushList(List<T> list){
        int cant= this.mList.size();
        this.mList.addAll(list);
        notifyItemRangeInserted(cant, list.size());
    }

    public List<T> getList(){return mList;}

    public void updateItem(int position, T t) {
        mList.set(position, t);
        notifyItemChanged(position);
    }

    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,mList.size());

    }

    public void setItems(List<T> items) {
        mList = items;
        notifyDataSetChanged();
    }

    public void clear(){
        mList.clear();
        notifyDataSetChanged();
    }

}
