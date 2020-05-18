package com.leila.android.fishy.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leila.android.fishy.DateHelper;
import com.leila.android.fishy.R;
import com.leila.android.fishy.network.models.Income;
import com.leila.android.fishy.network.models.ReportIncome;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportIncomeAdapter extends BaseAdapter<ReportIncome, ReportIncomeAdapter.ViewHolder>  implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {


    private Context mContext;


    public ReportIncomeAdapter(Context context, List<ReportIncome> outcomes){
        setItems(outcomes);
        mContext = context;
    }

    public ReportIncomeAdapter(){

    }

    @Override
    public long getHeaderId(int position) {
        // Log.d("ALE","Position: "+position + " COunt: " + getItemCount());
        if(position>= getItemCount()){
            //   Log.d("ALE","ERROR Position: "+position + " COunt: " + getItemCount());
            return -1;
        } else {
            Date date =  DateHelper.get().parseDate(DateHelper.get().onlyDateComplete(getItem(position).created));
            return  date.getMonth();
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }
    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position < getItemCount()) {
            LinearLayout linear = (LinearLayout) holder.itemView;

            final ReportIncome e = getItem(position);
            String dateToShow = DateHelper.get().getNameMonth(e.created).substring(0,3);
            String year= DateHelper.get().getOnlyYear(e.created);

            String amountByMonth= String.valueOf(e.amountByMonth);

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

                            RelativeLayout rel1= (RelativeLayout) v2;

                            int coutn3=rel1.getChildCount();

                            for(int k=0;k< coutn3; ++k){
                                v3=rel1.getChildAt(k);

                                if(k==0){
                                    TextView t= (TextView) v3;
                                    t.setText(dateToShow);
                                }else if(k==1){
                                    TextView t2= (TextView) v3;
                                    t2.setText(year);
                                }

                            }


                        }else if(j == 1){
                            TextView t2= (TextView) v2;
                            t2.setText("$ "+amountByMonth);
                        }
                    }
                }
            }
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView description;
        public TextView value;
        public  RecyclerView listIncomes;
        public  RecyclerView listPersonalIncomes;

        public ViewHolder(View v){
            super(v);
            description= v.findViewById(R.id.description);
            value= v.findViewById(R.id.value);

            listIncomes= v.findViewById(R.id.list_incomes);
            listPersonalIncomes= v.findViewById(R.id.personalIncomesList);
        }
    }

    @Override
    public ReportIncomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.report_item_income,parent,false);
        ReportIncomeAdapter.ViewHolder vh = new ReportIncomeAdapter.ViewHolder(v);

        return vh;
    }


    private void clearViewHolder(ReportIncomeAdapter.ViewHolder vh){
        if(vh.description!=null)
            vh.description.setText(null);
        if(vh.value!=null)
            vh.value.setText(null);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ReportIncomeAdapter.ViewHolder holder, final int position){
        clearViewHolder(holder);

        final ReportIncome current=getItem(position);

        final IncomeAdapter adapterIncome = new IncomeAdapter(mContext,new ArrayList<Income>());
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(mContext);
        holder.listIncomes.setLayoutManager(layoutManager);
        holder.listIncomes.setAdapter(adapterIncome);
        adapterIncome.setItems(current.listByMonth);

        final IncomeAdapter adapterPersonalIncome = new IncomeAdapter(mContext,new ArrayList<Income>());
        RecyclerView.LayoutManager layoutManagerPers= new LinearLayoutManager(mContext);
        holder.listPersonalIncomes.setLayoutManager(layoutManagerPers);
        holder.listPersonalIncomes.setAdapter(adapterPersonalIncome);
        adapterPersonalIncome.setItems(current.personalIncomesList);

    }
}