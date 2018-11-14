package com.example.android.fishy.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fishy.CurrentValuesHelper;
import com.example.android.fishy.CustomLoadingListItemCreator;
import com.example.android.fishy.DialogHelper;
import com.example.android.fishy.R;
import com.example.android.fishy.ValidatorHelper;
import com.example.android.fishy.adapters.ProductAdapter;
import com.example.android.fishy.adapters.UserAdapter;
import com.example.android.fishy.network.ApiClient;
import com.example.android.fishy.network.Error;
import com.example.android.fishy.network.GenericCallback;
import com.example.android.fishy.network.models.Product;
import com.example.android.fishy.network.models.User;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends BaseActivity implements Paginate.Callbacks{

    @Override
    public int getLayoutRes() {
        return R.layout.activity_products;
    }

    private RecyclerView mRecyclerView;
    private ProductAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //pagination
    private boolean loadingInProgress;
    private Integer mCurrentPage;
    private Paginate paginate;
    private boolean hasMoreItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackArrow();

        mRecyclerView = this.findViewById(R.id.list_products);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ProductAdapter(this, new ArrayList<Product>());
        mRecyclerView.setAdapter(mAdapter);
        //registerForContextMenu(mRecyclerView);

        FloatingActionButton addProduct= findViewById(R.id.add_product);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProduct();
            }
        });

        loadingInProgress=false;
        mCurrentPage=0;
        hasMoreItems = true;

        implementsPaginate();

       // listProducts();
    }

    private void createProduct(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_create_product, null);
        builder.setView(dialogView);

        final TextView name= dialogView.findViewById(R.id.product_name);
        final EditText price= dialogView.findViewById(R.id.product_price);
        final TextView stock= dialogView.findViewById(R.id.product_stock);
        final Button ok= dialogView.findViewById(R.id.ok);
        final TextView cancel= dialogView.findViewById(R.id.cancel);


        final AlertDialog dialog = builder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String priceProduct=price.getText().toString().trim();
                String stockProduct=stock.getText().toString().trim();
                String nameProduct=name.getText().toString().trim();

                if(!priceProduct.matches("") && !stockProduct.matches("") && !nameProduct.matches("")){
                    if(ValidatorHelper.get().isTypeDouble(priceProduct) && ValidatorHelper.get().isTypeInteger(stockProduct)){

                        Product newProduct= new Product(nameProduct,Double.valueOf(priceProduct),Double.valueOf(stockProduct));
                        ApiClient.get().postProduct(newProduct, new GenericCallback<Product>() {
                            @Override
                            public void onSuccess(Product data) {
                                mAdapter.pushItem(data);
                                Toast.makeText(dialogView.getContext(),"Se ha creado el producto "+data.fish_name, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Error error) {
                                DialogHelper.get().showMessage("Error","Error al crear el producto",ProductsActivity.this);
                            }
                        });
                        dialog.dismiss();

                    }else{
                        Toast.makeText(dialogView.getContext(),"Tipo de dato no valido ", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(dialogView.getContext()," Complete todo los datos por favor ", Toast.LENGTH_LONG).show();
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

   /* private void listProducts(){
        ApiClient.get().getAliveProducts("alive",new GenericCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> data) {
                mAdapter.setItems(data);
            }

            @Override
            public void onError(Error error) {

            }
        });
    }*/

    private void listProducts() {
        loadingInProgress=true;
        ApiClient.get().getAliveProductsByPage(mCurrentPage, "alive", new GenericCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> data) {

                if (data.size() == 0) {
                    hasMoreItems = false;
                }else{
                    int prevSize = mAdapter.getItemCount();
                    mAdapter.pushList(data);
                    mCurrentPage++;
                    if(prevSize == 0){
                        layoutManager.scrollToPosition(0);
                    }
                }
                loadingInProgress = false;

            }

            @Override
            public void onError(Error error) {
                loadingInProgress = false;
            }
        });
    }

    private void implementsPaginate(){

        loadingInProgress=false;
        mCurrentPage=0;
        hasMoreItems = true;

        paginate= Paginate.with(mRecyclerView,this)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(new CustomLoadingListItemCreator())
                .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
                    @Override
                    public int getSpanSize() {
                        return 0;
                    }
                })
                .build();
    }

    @Override
    public void onLoadMore() {
        listProducts();
    }

    @Override
    public boolean isLoading() {
        return loadingInProgress;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return !hasMoreItems;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
