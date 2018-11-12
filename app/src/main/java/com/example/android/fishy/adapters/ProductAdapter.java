package com.example.android.fishy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fishy.DialogHelper;
import com.example.android.fishy.Events.EventOrderState;
import com.example.android.fishy.Events.EventProductState;
import com.example.android.fishy.R;
import com.example.android.fishy.ValidatorHelper;
import com.example.android.fishy.network.ApiClient;
import com.example.android.fishy.network.Error;
import com.example.android.fishy.network.GenericCallback;
import com.example.android.fishy.network.models.Product;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class ProductAdapter  extends BaseAdapter<Product,ProductAdapter.ViewHolder> {

    private Context mContext;

    public ProductAdapter(Context context, List<Product> products){
        setItems(products);
        mContext = context;
    }

    public ProductAdapter(){

    }

    public List<Product> getListProduct(){
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView name;
        public TextView price;
        public TextView stock;
        public TextView soldedCant;
        public ImageView options;


        public ViewHolder(View v){
            super(v);
            name= v.findViewById(R.id.name_product);
            price= v.findViewById(R.id.price_product);
            stock= v.findViewById(R.id.stock_product);
            options=v.findViewById(R.id.options);
        }
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_item_product,parent,false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    private void clearViewHolder(ProductAdapter.ViewHolder vh){
        if(vh.name!=null)
            vh.name.setText(null);
        if(vh.price!=null)
            vh.price.setText(null);
        if(vh.stock!=null)
            vh.stock.setText(null);
        if(vh.soldedCant!=null)
            vh.soldedCant.setText(null);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        clearViewHolder(holder);

        final Product currentProduct=getItem(position);

        holder.name.setText(currentProduct.getFish_name());
        holder.price.setText("$"+String.valueOf(currentProduct.price));
        holder.stock.setText(getIntegerQuantity(currentProduct.stock));

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mContext, holder.options);
                popup.getMenuInflater().inflate(R.menu.menu_products, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_delete:
                                deleteProduct(currentProduct, position);
                                return true;
                            case R.id.menu_edit:
                                edithProduct(currentProduct, position,holder);

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

    private void deleteProduct(final Product p,final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_delete_product, null);
        builder.setView(dialogView);

        final TextView name = dialogView.findViewById(R.id.name);
        final TextView price = dialogView.findViewById(R.id.price);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final Button ok = dialogView.findViewById(R.id.ok);

        name.setText(p.fish_name);
        price.setText(getIntegerQuantity(p.price));
        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ApiClient.get().deleteProduct(p.id, new GenericCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        removeItem(position);
                        Toast.makeText(mContext, "Se ha eliminado el producto "+p.fish_name, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Error error) {
                        DialogHelper.get().showMessage("Error","Error al eliminar el producto "+p.fish_name,mContext);
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

    private void edithProduct(final Product p,final int position, final ViewHolder holder){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_edith_product, null);
        builder.setView(dialogView);


        final TextView edit_name= dialogView.findViewById(R.id.edit_name);
        final TextView edit_price= dialogView.findViewById(R.id.edit_price);
        final TextView edit_stock= dialogView.findViewById(R.id.edit_stock);
        final TextView cancel= dialogView.findViewById(R.id.cancel);
        final Button ok= dialogView.findViewById(R.id.ok);


        edit_name.setHint(p.getFish_name());
        edit_name.setHintTextColor(mContext.getResources().getColor(R.color.colorDialogButton));
        edit_price.setHint(getIntegerQuantity(p.getPrice()));
        edit_price.setHintTextColor(mContext.getResources().getColor(R.color.colorDialogButton));
        edit_stock.setHint(getIntegerQuantity(p.getStock()));
        edit_stock.setHintTextColor(mContext.getResources().getColor(R.color.colorDialogButton));

        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName=edit_name.getText().toString().trim();
                String productPrice=edit_price.getText().toString().trim();
                String productStock=edit_stock.getText().toString().trim();

                boolean isDataValid=true;

                if(!productName.matches("")){
                    p.setFish_name(productName);
                }

                if(!productPrice.matches("")) {
                    if (ValidatorHelper.get().isTypeDouble(productPrice)) {
                        p.setPrice(Double.valueOf(productPrice));
                    }else {
                        isDataValid=false;
                        Toast.makeText(dialogView.getContext(), " Tipo de precio no valido ", Toast.LENGTH_LONG).show();
                    }
                }

                if(!productStock.matches("")) {
                    if (ValidatorHelper.get().isTypeDouble(productStock)) {
                        p.setStock(Double.valueOf(productStock));
                    }else {
                        isDataValid=false;
                        Toast.makeText(dialogView.getContext(), " Tipo de stock no valido ", Toast.LENGTH_LONG).show();
                    }
                }

                if(isDataValid){
                    updateItem(position,p);

                    ApiClient.get().putProduct(p, new GenericCallback<Product>() {
                        @Override
                        public void onSuccess(Product data) {
                            EventBus.getDefault().post(new EventProductState(p.id,"edited",p.stock));
                            Toast.makeText(dialogView.getContext(), " El producto "+data.fish_name +" ha sido modificado ", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(Error error) {
                            DialogHelper.get().showMessage("Error"," Error al modificar producto",dialogView.getContext());
                        }
                    });

                    dialog.dismiss();
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
    private String getIntegerQuantity(Double val){
        String[] arr=String.valueOf(val).split("\\.");
        int[] intArr=new int[2];
        intArr[0]=Integer.parseInt(arr[0]);
        intArr[1]=Integer.parseInt(arr[1]);

        if(intArr[1] == 0){
            return String.valueOf(intArr[0]);
        }else{
            return String.valueOf(val);
        }

    }

}
