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

import com.leila.android.fishy.DateHelper;
import com.leila.android.fishy.R;
import com.leila.android.fishy.ValuesHelper;
import com.leila.android.fishy.network.models.Income;

import java.util.List;

public class IncomeAdapter extends BaseAdapter<Income, IncomeAdapter.ViewHolder> {

    private Context mContext;


    public IncomeAdapter(Context context, List<Income> outcomes) {
        setItems(outcomes);
        mContext = context;
    }

    public IncomeAdapter() {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public TextView type;
        public TextView value;
        public TextView numberDay;
        public TextView dateDay;


        public ViewHolder(View v) {
            super(v);
            type = v.findViewById(R.id.type);
            description = v.findViewById(R.id.description);
            value = v.findViewById(R.id.value);
            numberDay = v.findViewById(R.id.number_day);
            dateDay = v.findViewById(R.id.date_day);

        }
    }

    @Override
    public IncomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_income, parent, false);
        IncomeAdapter.ViewHolder vh = new IncomeAdapter.ViewHolder(v);

        return vh;
    }


    private void clearViewHolder(IncomeAdapter.ViewHolder vh) {
        if (vh.description != null)
            vh.description.setText(null);
        if (vh.type != null)
            vh.type.setText(null);
        if (vh.value != null)
            vh.value.setText(null);
        if (vh.numberDay != null)
            vh.numberDay.setText(null);
        if (vh.dateDay != null)
            vh.dateDay.setText(null);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final IncomeAdapter.ViewHolder holder, final int position) {
        clearViewHolder(holder);

        final Income current = getItem(position);

        Double val=ValuesHelper.round(current.value,2);
        holder.value.setText("$" + val);

        holder.description.setText(current.description);
        holder.type.setText(current.type);

        holder.dateDay.setText(DateHelper.get().getNameDay(current.created));
        holder.numberDay.setText(DateHelper.get().getDayEvent(current.created));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"fecha "+current.created,Toast.LENGTH_LONG).show();
            }
        });


    }
}