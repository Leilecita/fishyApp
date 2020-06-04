package com.leila.android.fishy.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leila.android.fishy.CurrentValuesHelper;
import com.leila.android.fishy.CustomLoadingListItemCreator;
import com.leila.android.fishy.DialogHelper;
import com.leila.android.fishy.R;
import com.leila.android.fishy.ValidatorHelper;
import com.leila.android.fishy.adapters.ProductAdapter;
import com.leila.android.fishy.network.ApiClient;
import com.leila.android.fishy.network.Error;
import com.leila.android.fishy.network.GenericCallback;
import com.leila.android.fishy.network.models.Product;
import com.leila.android.fishy.network.models.ReportProduct;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private String mQuery = "";
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackArrow();

        setTitle("Productos");

        mRecyclerView = this.findViewById(R.id.list_products);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ProductAdapter(this, new ArrayList<ReportProduct>());
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

        registerForContextMenu(mRecyclerView);
       //setHasOptionsMenu(true);
        implementsPaginate();

    }

    private void clearview(){
        mCurrentPage = 0;
        mAdapter.clear();
        hasMoreItems=true;
    }

    private void clearAndList(){
        clearview();
        listProducts(mQuery);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_products2, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView();
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Ingrese nombre");

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.requestFocus();
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.trim().toLowerCase().equals(mQuery)) {
                    mCurrentPage = 0;
                    mAdapter.clear();
                    listProducts(newText.trim().toLowerCase());
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu);

        return true;
    }


    private void createProduct(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_create_product, null);
        builder.setView(dialogView);

        final TextView name= dialogView.findViewById(R.id.product_name);
        final EditText price= dialogView.findViewById(R.id.product_price);
        final EditText wholsaler_price= dialogView.findViewById(R.id.wholesaler_price);
        final TextView stock= dialogView.findViewById(R.id.product_stock);
        final TextView cost= dialogView.findViewById(R.id.product_cost);
        final Button ok= dialogView.findViewById(R.id.ok);
        final TextView cancel= dialogView.findViewById(R.id.cancel);

        final AlertDialog dialog = builder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String priceProduct=price.getText().toString().trim();
                String wholsaerPriceProduct=wholsaler_price.getText().toString().trim();
                String stockProduct=stock.getText().toString().trim();
                String nameProduct=name.getText().toString().trim();
                String costProduct=cost.getText().toString().trim();

                if(!priceProduct.matches("") && !stockProduct.matches("") && !nameProduct.matches("") && !wholsaerPriceProduct.matches("")){

                        Product newProduct= new Product(nameProduct,Double.valueOf(priceProduct),Double.valueOf(wholsaerPriceProduct),Double.valueOf(stockProduct),Double.valueOf(costProduct));
                        ApiClient.get().postProduct(newProduct, new GenericCallback<Product>() {
                            @Override
                            public void onSuccess(Product data) {

                                clearAndList();

                                Toast.makeText(dialogView.getContext(),"Se ha creado el producto "+data.fish_name, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Error error) {
                                DialogHelper.get().showMessage("Error","Error al crear el producto",ProductsActivity.this);
                            }
                        });
                        dialog.dismiss();

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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void listProducts(final String query) {
        loadingInProgress=true;
        this.mQuery = query;
        final String newToken = UUID.randomUUID().toString();
        this.token =  newToken;
        ApiClient.get().getAliveProductsByPage(mCurrentPage, "alive",mQuery, new GenericCallback<List<ReportProduct>>() {
            @Override
            public void onSuccess(List<ReportProduct> data) {

                if(token.equals(newToken)) {

                    if (query == mQuery) {

                        if (data.size() == 0) {
                            hasMoreItems = false;
                        } else {
                            int prevSize = mAdapter.getItemCount();
                            mAdapter.pushList(data);
                            mCurrentPage++;
                            if (prevSize == 0) {
                                layoutManager.scrollToPosition(0);
                            }
                        }
                        loadingInProgress = false;
                    }
                }
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
        listProducts(mQuery);
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

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
