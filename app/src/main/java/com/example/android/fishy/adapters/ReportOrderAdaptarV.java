package com.example.android.fishy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.android.fishy.DialogHelper;
import com.example.android.fishy.Interfaces.ItemTouchHelperAdapter;
import com.example.android.fishy.Interfaces.OnStartDragListener;
import com.example.android.fishy.R;
import com.example.android.fishy.activities.CreateOrderActivity;
import com.example.android.fishy.network.ApiClient;
import com.example.android.fishy.network.Error;
import com.example.android.fishy.network.GenericCallback;
import com.example.android.fishy.network.models.Order;
import com.example.android.fishy.network.models.reportsOrder.ReportItemOrder;
import com.example.android.fishy.network.models.reportsOrder.ReportOrder;

import java.util.ArrayList;
import java.util.List;


public class ReportOrderAdaptarV extends  BaseAdapter<ReportOrder,ReportOrderAdaptarV.ViewHolder> implements ItemTouchHelperAdapter {

    private Context mContext;
    private boolean mOnlyAdress;
    private boolean mHistoryuser;
    private OnStartDragListener mDragStartListener;

    public ReportOrderAdaptarV(Context context, List<ReportOrder> orders, OnStartDragListener dragListner){
        setItems(orders);
        mContext = context;
        mDragStartListener = dragListner;
        mOnlyAdress=false;
        mHistoryuser=false;
    }

    public ReportOrderAdaptarV(){

    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView name;
        public TextView address;
        public TextView amount;
        public ImageView listItemsOrder;
        public ImageView phone;
        public ImageView mens;
        public TextView state;
        public TextView priority;
        public ImageView options;
        public ImageView stateImage;

        public ViewHolder(View v){
            super(v);
            name= v.findViewById(R.id.user_name);
            address= v.findViewById(R.id.user_address);
            amount= v.findViewById(R.id.amount_order);
            listItemsOrder= v.findViewById(R.id.list);
            phone= v.findViewById(R.id.phone);
            mens= v.findViewById(R.id.mens);
            state= v.findViewById(R.id.state);
            stateImage= v.findViewById(R.id.stateImage);
            options= v.findViewById(R.id.options);
            priority= v.findViewById(R.id.priority);
        }
    }

    @Override
    public ReportOrderAdaptarV.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v;
        if(mOnlyAdress){
            v = LayoutInflater.from(mContext).inflate(R.layout.car_item_address,parent,false);
        }else{
            v = LayoutInflater.from(mContext).inflate(R.layout.card_item_order,parent,false);

        }
        //  View v = LayoutInflater.from(mContext).inflate(R.layout.card_item_order,parent,false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    public void setOnlyAddress(boolean val){
        this.mOnlyAdress=val;
    }


    public void setHistoryUser(boolean val){
        this.mHistoryuser=val;

    }
    public boolean getOnlyAddress(){return this.mOnlyAdress;}

    @Override
    public void onItemDismiss(int position) {
        //  removeItem(position);
        // notifyItemRemoved(position);
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        ReportOrder repOrder= getList().get(fromPosition);
        ReportOrder newOrder= repOrder;

        getList().remove(fromPosition);
        getList().add(toPosition,newOrder);
        notifyItemMoved(fromPosition,toPosition);


        return true;
    }


    private void clearViewHolder(ReportOrderAdaptarV.ViewHolder vh){

        if(vh.address!=null)
            vh.address.setText(null);
        if(vh.priority!=null)
            vh.priority.setText(null);
        if(!mOnlyAdress){
            if(vh.amount!=null)
                vh.amount.setText(null);
            if(vh.name!=null)
                vh.name.setText(null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        clearViewHolder(holder);
        final ReportOrder r= getItem(position);

        holder.address.setText(r.getUser_address());
        holder.priority.setText(String.valueOf(r.priority));
        if(r.state.equals("pendiente")){
            holder.stateImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pendient_red));
        }else if (r.state.equals("entregado")){
            holder.stateImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.done_green));
        }else{
            holder.state.setTextColor(mContext.getResources().getColor(R.color.borrador));
        }
        if(mHistoryuser){
            holder.priority.setText(r.deliver_date);
        }
        if(!mOnlyAdress){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showOrderInfo(r,position);
                    return false;
                }
            });
        }

        if(mOnlyAdress){
            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getActionMasked() ==  MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });


           /* holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPriority(r,position,holder);
                }
            });*/

        }else{
            holder.name.setText(r.getUser_name());
            holder.amount.setText(String.valueOf(r.total_amount));
            holder.listItemsOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showItemsList(r);
                }
            });
            holder.phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + r.phone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
            holder.mens.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendWhatsapp("hola",r.phone);
                }
            });
            holder.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(mContext, holder.options);
                    popup.getMenuInflater().inflate(R.menu.menu_transaction, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_edit:
                                    finishOrder(r,position);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show();
                }
            });
        }
    }


    private void finishOrder(final ReportOrder r, final Integer position){

        ApiClient.get().finishOrder(r.order_id, new GenericCallback<Order>() {
            @Override
            public void onSuccess(Order data) {
                r.state=data.state;
                updateItem(position,r);
                System.out.println("se ha actualizado");
            }

            @Override
            public void onError(Error error) {
                DialogHelper.get().showMessage("Error","No se pudo finalizar el pedido",mContext);
            }
        });

    }
    private void deleteOrder(final ReportOrder r, final Integer position){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_delete_order, null);
        builder.setView(dialogView);

        final TextView name=  dialogView.findViewById(R.id.user_name);
        final TextView delivery=  dialogView.findViewById(R.id.deliver_date);
        final TextView amount=  dialogView.findViewById(R.id.total_amount);
        final TextView cancel=  dialogView.findViewById(R.id.cancel);
        final Button ok=  dialogView.findViewById(R.id.ok);

        name.setText(r.getUser_name());
        delivery.setText(r.deliver_date);
        amount.setText(String.valueOf(r.total_amount));

        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ApiClient.get().deleteOrder(r.order_id, new GenericCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        removeItem(position);
                    }
                    @Override
                    public void onError(Error error) {
                        DialogHelper.get().showMessage("Error","No se pudo eliminar el pedido",mContext);
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

    private void sendWhatsapp(String text, String phone){
        Uri uri = Uri.parse("smsto:" + phone);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        i.putExtra(Intent.EXTRA_TEXT,text);
        //i.setType("text/plain");
        //i.setPackage("com.whatsapp");
        mContext.startActivity(Intent.createChooser(i, ""));
    }

    private void showOrderInfo(final ReportOrder r,final Integer position){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_order_information, null);
        builder.setView(dialogView);
        final TextView name=  dialogView.findViewById(R.id.user_name);
        final TextView zone=  dialogView.findViewById(R.id.user_neighboor);
        final TextView delivery_date=  dialogView.findViewById(R.id.deliver_date);
        final ImageView deleteOrder=  dialogView.findViewById(R.id.deleteuser);

        name.setText(r.getUser_name());
        zone.setText(r.neighborhood);
        delivery_date.setText(r.deliver_date);


        final AlertDialog dialog = builder.create();

        deleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                deleteOrder(r,position);
            }
        });

        dialog.show();

    }
    private void startEdithOrderActivity(ReportOrder reportorder){
        CreateOrderActivity.startEdithOrder(mContext,reportorder);
    }
    private void showItemsList(final ReportOrder r){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_show_items, null);
        builder.setView(dialogView);
        final TextView name=  dialogView.findViewById(R.id.user_name);

        RecyclerView recycler= dialogView.findViewById(R.id.list_items);
        ReportItemOrderAdapter adapter= new ReportItemOrderAdapter(mContext,new ArrayList<ReportItemOrder>());
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(mContext);;
        recycler.setLayoutManager(layoutManager);

        recycler.setAdapter(adapter);
        adapter.setItems(r.items);
        TextView amount= dialogView.findViewById(R.id.amount);
        amount.setText(String.valueOf(r.total_amount));
        name.setText(r.getUser_name());

        final TextView cancel=  dialogView.findViewById(R.id.cancel);
        final Button ok=  dialogView.findViewById(R.id.ok);
        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startEdithOrderActivity(r);
            }
        });
        dialog.show();
    }
}