package com.leila.android.fishy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leila.android.fishy.DateHelper;
import com.leila.android.fishy.DialogHelper;
import com.leila.android.fishy.R;

import com.leila.android.fishy.ValuesHelper;
import com.leila.android.fishy.network.ApiClient;
import com.leila.android.fishy.network.Error;
import com.leila.android.fishy.network.GenericCallback;
import com.leila.android.fishy.network.models.Outcome;
import com.leila.android.fishy.network.models.ReportOutcome;
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
        public TextView type;
        public TextView value;
        public TextView numberDay;
        public TextView dateDay;



        public ViewHolder(View v){
            super(v);
            type= v.findViewById(R.id.type);
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
        if(vh.type!=null)
            vh.type.setText(null);
        if(vh.value!=null)
            vh.value.setText(null);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final OutcomeAdapter.ViewHolder holder, final int position){
        clearViewHolder(holder);

        final Outcome current=getItem(position);
        holder.value.setText("$"+ValuesHelper.get().getIntegerQuantity(current.value));
        holder.type.setText(current.type);
        holder.description.setText(current.description);

        holder.dateDay.setText(DateHelper.get().getNameDay(current.created));
        holder.numberDay.setText(DateHelper.get().getDayEvent(current.created));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteOutcome(current,position);
                return false;
            }
        });

    }

    private void deleteOutcome(final Outcome current, final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.delete_outcome, null);
        builder.setView(dialogView);

        final TextView descr = dialogView.findViewById(R.id.description);
        final TextView value = dialogView.findViewById(R.id.value);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final Button ok = dialogView.findViewById(R.id.ok);

        descr.setText(current.description);
        value.setText(ValuesHelper.get().getIntegerQuantity(current.value));
        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ApiClient.get().deleteOutcome(current.id, new GenericCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        removeItem(position);
                        Toast.makeText(mContext, "Se ha eliminado el gasto "+current.description, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Error error) {
                        DialogHelper.get().showMessage("Error","Error al eliminar el gasto "+current.description,mContext);
                    }
                });

                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }



}
