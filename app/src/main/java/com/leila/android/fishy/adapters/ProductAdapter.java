package com.leila.android.fishy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.leila.android.fishy.DialogHelper;
import com.leila.android.fishy.Events.EventProductState;
import com.leila.android.fishy.R;
import com.leila.android.fishy.ValidatorHelper;
import com.leila.android.fishy.network.ApiClient;
import com.leila.android.fishy.network.Error;
import com.leila.android.fishy.network.GenericCallback;
import com.leila.android.fishy.network.models.Product;
import com.leila.android.fishy.network.models.ReportProduct;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ProductAdapter  extends BaseAdapter<ReportProduct,ProductAdapter.ViewHolder> {

    private Context mContext;

    public ProductAdapter(Context context, List<ReportProduct> products){
        setItems(products);
        mContext = context;
    }

    public ProductAdapter(){

    }

    public List<ReportProduct> getListProduct(){
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView name;
        public TextView price;
        public TextView mayor_price;
        public TextView stock;
        public TextView soldedCant;
        public TextView product_cost;
        public ImageView options;
        public ImageView rosario;
        public ImageView mardel;

        public TextView stock2;
        public LinearLayout loadBothStock;

        public ViewHolder(View v){
            super(v);
            name= v.findViewById(R.id.name_product);
            price= v.findViewById(R.id.price_product);
            stock= v.findViewById(R.id.stock_product);
            product_cost= v.findViewById(R.id.cost_product);
            options=v.findViewById(R.id.options);
            loadBothStock=v.findViewById(R.id.load_both);
            rosario=v.findViewById(R.id.rosario);
            mardel=v.findViewById(R.id.mardel);
            mayor_price=v.findViewById(R.id.price_product_wholersale);

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
        if(vh.mayor_price!=null)
            vh.mayor_price.setText(null);
        if(vh.stock!=null)
            vh.stock.setText(null);
        if(vh.stock2!=null)
            vh.stock2.setText(null);
        if(vh.soldedCant!=null)
            vh.soldedCant.setText(null);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        clearViewHolder(holder);

        final ReportProduct currentProduct=getItem(position);

        holder.name.setText(currentProduct.fish_name);
        holder.product_cost.setText(getIntegerQuantity(currentProduct.product_cost));

        System.out.println("servidor" + currentProduct.serverStock2);

        holder.price.setText(getIntegerQuantity(currentProduct.price));
        if(currentProduct.wholesaler_price!=null)
        holder.mayor_price.setText(String.valueOf(currentProduct.wholesaler_price));

        System.out.println("produc name"+currentProduct.fish_name);
        System.out.println("s1"+currentProduct.stock);
        System.out.println("s2"+currentProduct.stock2);
        holder.stock.setText(getIntegerQuantity(currentProduct.stock + currentProduct.stock2));

        if(currentProduct.load_both_stocks.equals("true")){
           holder.mardel.setVisibility(View.VISIBLE);
           holder.rosario.setVisibility(View.VISIBLE);
        }else{
          if(currentProduct.serverStock2.equals("Rosario")){
              holder.rosario.setVisibility(View.GONE);
          }else{
              holder.mardel.setVisibility(View.GONE);
          }

        }

        holder.loadBothStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentProduct.load_both_stocks.equals("false"))
                Toast.makeText(mContext,"Este producto no existe en la app "+currentProduct.serverStock2+" o esta mal cargado el nombre",Toast.LENGTH_LONG ).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
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

    private void deleteProduct(final ReportProduct p,final int position){

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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void edithProduct(final ReportProduct p,final int position, final ViewHolder holder){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_edith_product, null);
        builder.setView(dialogView);


        final TextView edit_name= dialogView.findViewById(R.id.edit_name);
        final TextView edit_price= dialogView.findViewById(R.id.edit_price);
        final TextView edit_wholesaler_price= dialogView.findViewById(R.id.edit_wholesaler_price);
        final TextView edit_stock= dialogView.findViewById(R.id.edit_stock);
        final TextView edit_cost= dialogView.findViewById(R.id.edit_product_cost);
        final TextView stock2= dialogView.findViewById(R.id.stock2);

        final TextView text= dialogView.findViewById(R.id.textServer);
        final TextView cancel= dialogView.findViewById(R.id.cancel);
        final Button ok= dialogView.findViewById(R.id.ok);

        final TextView stockEdithable= dialogView.findViewById(R.id.stock_edithable);
        final TextView stockNoEdithable= dialogView.findViewById(R.id.stock_no_edithable);

        if(p.serverStock2.equals("Rosario")){
            stockNoEdithable.setText("Stock Ros");
            stockEdithable.setText("Stock MDP");
        }else{
            stockNoEdithable.setText("Stock MDP");
            stockEdithable.setText("Stock Ros");
        }

        edit_cost.setText(String.valueOf(p.product_cost));
        edit_name.setText(p.fish_name);
        edit_name.setTextColor(mContext.getResources().getColor(R.color.colorDialogButton));
        edit_price.setText(getIntegerQuantity(p.price));
        edit_price.setTextColor(mContext.getResources().getColor(R.color.colorDialogButton));

        if(p.wholesaler_price != null){
            edit_wholesaler_price.setText(getIntegerQuantity(p.wholesaler_price));
        }else{
            edit_wholesaler_price.setHint("0.0");
        }

        edit_wholesaler_price.setTextColor(mContext.getResources().getColor(R.color.colorDialogButton));

        edit_stock.setText(getIntegerQuantity(p.stock));
        edit_stock.setTextColor(mContext.getResources().getColor(R.color.colorDialogButton));

        stock2.setText(getIntegerQuantity(p.stock2));
        text.setText("Este valor se modifica en la aplicacion "+p.serverStock2);

        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName=edit_name.getText().toString().trim();
                String productPrice=edit_price.getText().toString().trim();
                String productWholesalerPrice=edit_wholesaler_price.getText().toString().trim();
                String productStock=edit_stock.getText().toString().trim();
                String costProduct=edit_cost.getText().toString().trim();

                boolean isDataValid=true;

                if(!productName.matches("")){
                    p.fish_name=productName;
                }
                if(!costProduct.matches("")){
                    p.product_cost=Double.valueOf(costProduct);
                }

                if(!productPrice.matches("")) {
                    p.price=Double.valueOf(productPrice);
                }

                if(!productWholesalerPrice.matches("")) {
                    p.wholesaler_price=Double.valueOf(productWholesalerPrice);
                }

                if(!productStock.matches("")) {
                    p.stock=Double.valueOf(productStock);
                }

                if(isDataValid){
                    updateItem(position,p);

                    Product p2= new Product(p.fish_name,p.price,p.wholesaler_price,p.stock,p.product_cost);
                    p2.id=p.id;

                    ApiClient.get().putProduct(p2, new GenericCallback<Product>() {
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    private String getIntegerQuantity(Double val){

       return String.valueOf(val);
     /*   String[] arr=String.valueOf(val).split("\\.");
        int[] intArr=new int[2];
        intArr[0]=Integer.parseInt(arr[0]);
        intArr[1]=Integer.parseInt(arr[1]);

        if(intArr[1] == 0){
            return String.valueOf(intArr[0]);
        }else{
            return String.valueOf(val);
        }*/

    }
}
