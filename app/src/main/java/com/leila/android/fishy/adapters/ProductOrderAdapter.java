package com.leila.android.fishy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.leila.android.fishy.DialogHelper;
import com.leila.android.fishy.Events.EventProductState;
import com.leila.android.fishy.Interfaces.OnAddItemListener;
import com.leila.android.fishy.Interfaces.OnStartActivity;
import com.leila.android.fishy.R;

import com.leila.android.fishy.ValidatorHelper;
import com.leila.android.fishy.network.ApiClient;
import com.leila.android.fishy.network.Error;
import com.leila.android.fishy.network.GenericCallback;
import com.leila.android.fishy.network.models.ItemOrder;
import com.leila.android.fishy.network.models.ReportProduct;
import com.leila.android.fishy.types.PriceType;

import java.util.List;
import android.view.WindowManager.LayoutParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ProductOrderAdapter  extends BaseAdapter<ReportProduct,ProductOrderAdapter.ViewHolder> {

    private OnAddItemListener onAddItemOrderLister = null;

    private OnStartActivity onStart= null;

    public void setOnAddItemListener(OnAddItemListener lister){
        onAddItemOrderLister = lister;
    }

    public void setOnStart(OnStartActivity lister){
        onStart=lister;
    }

    private Context mContext;
    private Long mOrderId;

    public ProductOrderAdapter(Context context, List<ReportProduct> products){
        setItems(products);
        mContext = context;
        mOrderId=90L;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(EventProductState event){
        for(int i=0;i<getListProduct().size();++i){
            if(getListProduct().get(i).id.equals(event.getIdProduct())){
                if(event.mState.equals("edited")){
                    getListProduct().get(i).stock=event.getStock();
                    updateItem(i,getListProduct().get(i));
                }
            }
        }
    }


    public void setOrderId(Long orderId){
        mOrderId=orderId;
    }

    public Long getOrderId(){return mOrderId;}

    public ProductOrderAdapter(){

    }


    public List<ReportProduct> getListProduct(){
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView name;
        public TextView cant;



        public ViewHolder(View v){
            super(v);
            name= v.findViewById(R.id.name_product);

            cant= v.findViewById(R.id.cant);

        }
    }

    @Override
    public ProductOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_select_product,parent,false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    private void clearViewHolder(ProductOrderAdapter.ViewHolder vh){
        if(vh.name!=null)
            vh.name.setText(null);
        if(vh.cant!=null)
            vh.cant.setText(null);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        clearViewHolder(holder);

        final ReportProduct currentProduct=getItem(position);

        holder.name.setText(currentProduct.fish_name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createItemOrder(currentProduct);
            }
        });
    }

    private String getPriceTypeSelected(CheckBox check_min,CheckBox cheack_may){
        if(check_min.isChecked()){
            return PriceType.MINORISTA.getName();
        }else{
            return PriceType.MAYORISTA.getName();
        }
    }

    private Double getPriceSelected(CheckBox check_min,CheckBox cheack_may,ReportProduct p){
        if(check_min.isChecked()){
            return p.price;
        }else{
            return p.wholesaler_price;
        }
    }

    private void createItemOrder(final ReportProduct p){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_set_quantity, null);
        builder.setView(dialogView);

        final TextView name = dialogView.findViewById(R.id.fish_name);
        final TextView quantity = dialogView.findViewById(R.id.quantity);
        final TextView price = dialogView.findViewById(R.id.price);
        final TextView wholesaler_price = dialogView.findViewById(R.id.wholesaler_price);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final Button ok = dialogView.findViewById(R.id.ok);
        final CheckBox check_min = dialogView.findViewById(R.id.check_min);
        final CheckBox check_may = dialogView.findViewById(R.id.check_may);

        name.setText(p.fish_name);
        price.setText("$"+String.valueOf(p.price));
        wholesaler_price.setText("$"+p.wholesaler_price);

        check_min.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(check_min.isChecked()){
                    check_min.setChecked(true);
                    check_may.setChecked(false);
                }else{
                    check_min.setChecked(false);
                }
            }
        });

        check_may.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(check_may.isChecked()){
                    check_may.setChecked(true);
                    check_min.setChecked(false);
                }else{
                    check_may.setChecked(false);
                }
            }
        });


        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fishQuantity=quantity.getText().toString().trim();

                if(!fishQuantity.matches("")){

                    if(p.stock>= Double.valueOf(fishQuantity)){

                      //  ItemOrder itemOrder=new ItemOrder(p.id,mOrderId,Double.valueOf(fishQuantity)

                        ItemOrder itemOrder=new ItemOrder(p.id,mOrderId,Double.valueOf(fishQuantity)
                                ,p.fish_name,getPriceTypeSelected(check_min,check_may),
                                getPriceSelected(check_min,check_may,p),p.product_cost);

                            ApiClient.get().postItemOrder(itemOrder, new GenericCallback<ItemOrder>() {
                                @Override
                                public void onSuccess(ItemOrder data) {

                                    Toast.makeText(mContext, "Se ha agregado el producto "+name.getText().toString(),Toast.LENGTH_SHORT).show();

                                    if(onAddItemOrderLister!=null){
                                        onAddItemOrderLister.onAddItemToOrder(data.id,data.product_id,data.order_id,data.quantity,true,
                                                data.product_name,data.price_type,data.price,data.product_cost);
                                    }
                                }

                                @Override
                                public void onError(Error error) {
                                    DialogHelper.get().showMessage(p.fish_name,"Error al cargar el producto",mContext);
                                }
                            });


                        dialog.dismiss();
                    }else{
                        dialog.dismiss();
                        loadStock(p.fish_name,p.id);
                    }
                }else{
                    Toast.makeText(mContext,"Dato inválido",Toast.LENGTH_LONG).show();
                }
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

    private void loadStock(String fish_name,final Long id_product){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_start_products_activity, null);
        builder.setView(dialogView);

        final TextView name=  dialogView.findViewById(R.id.name);
        name.setText(fish_name);

        final TextView cancel=  dialogView.findViewById(R.id.cancel);
        final TextView ok=  dialogView.findViewById(R.id.ok);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startProductsActivity(id_product);
            }
        });
        dialog.show();
    }

    private void startProductsActivity(Long id_product){

        if(onStart!=null){
            onStart.onStartProducts(id_product);
        }

      //  Intent intent = new Intent(mContext, ProductsActivity.class);
       // ((Activity) mContext).startActivityForResult(intent, 1);

    }


}




