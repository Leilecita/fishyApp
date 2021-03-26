package com.leila.android.fishy.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.leila.android.fishy.Interfaces.OnAddItemListener;
import com.leila.android.fishy.Interfaces.OnCategoryListener;
import com.leila.android.fishy.R;
import com.leila.android.fishy.network.models.ReportCategory;

import java.util.List;

public class CategoryAdapter extends BaseAdapter<ReportCategory, CategoryAdapter.ViewHolder> {

    private Context mContext;

    private OnCategoryListener onCategoryListener = null;

    public void setOnCategoryListener(OnCategoryListener lister){
        onCategoryListener = lister;
    }

    public CategoryAdapter(Context context, List<ReportCategory> outcomes) {
        setItems(outcomes);
        mContext = context;
    }

    public CategoryAdapter() {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;


        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);

        }
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        CategoryAdapter.ViewHolder vh = new CategoryAdapter.ViewHolder(v);

        return vh;
    }


    private void clearViewHolder(CategoryAdapter.ViewHolder vh) {
        if (vh.name != null)
            vh.name.setText(null);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final CategoryAdapter.ViewHolder holder, final int position) {
        clearViewHolder(holder);

        final ReportCategory current = getItem(position);
        holder.name.setText(current.category);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("toca acaa");
                if(onCategoryListener!=null){

                    onCategoryListener.onCategoryListener(current.category);

                }
            }
        });


    }
}