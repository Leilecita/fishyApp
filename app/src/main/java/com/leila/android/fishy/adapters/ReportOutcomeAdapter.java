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
import com.leila.android.fishy.ValuesHelper;
import com.leila.android.fishy.network.models.Outcome;
import com.leila.android.fishy.network.models.ReportOutcome;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportOutcomeAdapter extends BaseAdapter<ReportOutcome, ReportOutcomeAdapter.ViewHolder>  implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    /* private OnAddItemListener onAddItemOrderLister = null;

     public void setOnAddItemListener(OnAddItemListener lister){
         onAddItemOrderLister = lister;
     }
 */
    private Context mContext;


    public ReportOutcomeAdapter(Context context, List<ReportOutcome> outcomes){
        setItems(outcomes);
        mContext = context;
    }

    public ReportOutcomeAdapter(){

    }

    @Override
    public long getHeaderId(int position) {
        if(position>= getItemCount()){
            return -1;
        } else {
            Date date =  DateHelper.get().parseDate(DateHelper.get().onlyDateComplete(getItem(position).created));
            return  date.getTime();
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

            final ReportOutcome e = getItem(position);
            String dateToShow = DateHelper.get().getNameMonth(e.created).substring(0,3);
            String year= DateHelper.get().getOnlyYear(e.created);

            String amountByMonth= ValuesHelper.get().getIntegerQuantity(e.amountByMonth);

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



    public List<ReportOutcome> getListOutcome(){
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView description;
        public TextView value;
        public  RecyclerView listOutcomes;



        public ViewHolder(View v){
            super(v);
            description= v.findViewById(R.id.description);
            value= v.findViewById(R.id.value);

            listOutcomes= v.findViewById(R.id.list_outcomes);

        }
    }

    @Override
    public ReportOutcomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.report_item_outcome,parent,false);
        ReportOutcomeAdapter.ViewHolder vh = new ReportOutcomeAdapter.ViewHolder(v);

        return vh;
    }


    private void clearViewHolder(ReportOutcomeAdapter.ViewHolder vh){
        if(vh.description!=null)
            vh.description.setText(null);
        if(vh.value!=null)
            vh.value.setText(null);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ReportOutcomeAdapter.ViewHolder holder, final int position){
        clearViewHolder(holder);

        final ReportOutcome current=getItem(position);

        final OutcomeAdapter adapterOutcome = new OutcomeAdapter(mContext,new ArrayList<Outcome>());
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(mContext);
        holder.listOutcomes.setLayoutManager(layoutManager);
        holder.listOutcomes.setAdapter(adapterOutcome);

        adapterOutcome.setItems(current.listByMonth);



    }




}
