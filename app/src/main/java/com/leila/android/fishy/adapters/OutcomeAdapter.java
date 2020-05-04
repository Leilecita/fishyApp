package com.leila.android.fishy.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.leila.android.fishy.DateHelper;
import com.leila.android.fishy.R;

import com.leila.android.fishy.ValuesHelper;
import com.leila.android.fishy.network.models.Outcome;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.Date;
import java.util.List;

public class OutcomeAdapter extends BaseAdapter<Outcome, OutcomeAdapter.ViewHolder>{

   /* private OnAddItemListener onAddItemOrderLister = null;

    public void setOnAddItemListener(OnAddItemListener lister){
        onAddItemOrderLister = lister;
    }
*/
    private Context mContext;


    public OutcomeAdapter(Context context, List<Outcome> outcomes){
        setItems(outcomes);
        mContext = context;
    }

    public OutcomeAdapter(){

    }

    public List<Outcome> getListOutcome(){
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView description;
        public TextView value;
        public TextView numberDay;
        public TextView dateDay;



        public ViewHolder(View v){
            super(v);
            description= v.findViewById(R.id.description);
            value= v.findViewById(R.id.value);
            numberDay= v.findViewById(R.id.number_day);
            dateDay= v.findViewById(R.id.date_day);

        }
    }

    @Override
    public OutcomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_outcome,parent,false);
        OutcomeAdapter.ViewHolder vh = new OutcomeAdapter.ViewHolder(v);

        return vh;
    }


    private void clearViewHolder(OutcomeAdapter.ViewHolder vh){
        if(vh.description!=null)
            vh.description.setText(null);
        if(vh.value!=null)
            vh.value.setText(null);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final OutcomeAdapter.ViewHolder holder, final int position){
        clearViewHolder(holder);

        final Outcome current=getItem(position);
        holder.value.setText("$"+ValuesHelper.get().getIntegerQuantity(current.value));
        holder.description.setText(current.description);


        holder.dateDay.setText(DateHelper.get().getNameDay(current.created));
        holder.numberDay.setText(DateHelper.get().getDayEvent(current.created));


    }


}
