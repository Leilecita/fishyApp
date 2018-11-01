package com.example.android.fishy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fishy.DialogHelper;
import com.example.android.fishy.Interfaces.OnAddItemListener;
import com.example.android.fishy.R;

import com.example.android.fishy.ValidatorHelper;
import com.example.android.fishy.activities.CreateOrderActivity;
import com.example.android.fishy.activities.CreateUserActivity;
import com.example.android.fishy.activities.ProductsActivity;
import com.example.android.fishy.network.ApiClient;
import com.example.android.fishy.network.Error;
import com.example.android.fishy.network.GenericCallback;
import com.example.android.fishy.network.models.ItemOrder;
import com.example.android.fishy.network.models.Product;
import com.example.android.fishy.network.models.reportsOrder.ReportItemOrder;
import com.example.android.fishy.network.models.reportsOrder.ReportOrder;

import java.util.ArrayList;
import java.util.List;
import android.view.WindowManager.LayoutParams;
public class ProductOrderAdapter  extends BaseAdapter<Product,ProductOrderAdapter.ViewHolder> {

    private OnAddItemListener onAddItemOrderLister = null;

    public void setOnAddItemListener(OnAddItemListener lister){
        onAddItemOrderLister = lister;
    }

    private Context mContext;
    private Long mOrderId;

    public ProductOrderAdapter(Context context, List<Product> products){
        setItems(products);
        mContext = context;
        mOrderId=90L;
    }

    public void setOrderId(Long orderId){
        mOrderId=orderId;
    }

    public ProductOrderAdapter(){

    }


    public List<Product> getListProduct(){
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

        final Product currentProduct=getItem(position);

        holder.name.setText(currentProduct.getFish_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createItemOrder(currentProduct);
            }
        });
    }

    private void createItemOrder(final Product p){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_set_quantity, null);
        builder.setView(dialogView);

        final TextView name = dialogView.findViewById(R.id.fish_name);
        final TextView quantity = dialogView.findViewById(R.id.quantity);
        final TextView price = dialogView.findViewById(R.id.price);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final Button ok = dialogView.findViewById(R.id.ok);

        name.setText(p.fish_name);
        price.setText("$"+String.valueOf(p.price));

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fishQuantity=quantity.getText().toString().trim();

                if(!fishQuantity.matches("") && ValidatorHelper.get().isTypeDouble(fishQuantity)){

                    if(p.stock>= Double.valueOf(fishQuantity)){

                        ItemOrder itemOrder=new ItemOrder(p.id,mOrderId,Double.valueOf(fishQuantity));
                        ApiClient.get().postItemOrder(itemOrder, new GenericCallback<ItemOrder>() {
                            @Override
                            public void onSuccess(ItemOrder data) {
                                if(onAddItemOrderLister!=null){
                                    onAddItemOrderLister.onAddItemToOrder();
                                }
                            }

                            @Override
                            public void onError(Error error) {
                                DialogHelper.get().showMessage(p.fish_name,"Error al cargar el producto",mContext);
                            }
                        });

                        dialog.dismiss();


                    }else{
                        loadStock(p.fish_name);
                       // DialogHelper.get().showMessage(p.fish_name,"No hay stock disponible para este producto",mContext);

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

    private void loadStock(String fish_name){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_start_products_activity, null);
        builder.setView(dialogView);

        final TextView name=  dialogView.findViewById(R.id.name);
        name.setText(fish_name);

        final TextView cancel=  dialogView.findViewById(R.id.cancel);
        final TextView ok=  dialogView.findViewById(R.id.ok);
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
                startProductsActivity();
            }
        });
        dialog.show();
    }

    private void startProductsActivity(){

        
        mContext.startActivity(new Intent(mContext, ProductsActivity.class));
    }

}




