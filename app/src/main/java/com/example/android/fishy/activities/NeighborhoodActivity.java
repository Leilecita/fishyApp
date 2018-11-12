package com.example.android.fishy.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fishy.DialogHelper;
import com.example.android.fishy.Events.EventOrderState;
import com.example.android.fishy.R;
import com.example.android.fishy.adapters.NeighborhoodAdapter;
import com.example.android.fishy.network.ApiClient;
import com.example.android.fishy.network.Error;
import com.example.android.fishy.network.GenericCallback;
import com.example.android.fishy.network.models.Neighborhood;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class NeighborhoodActivity extends BaseActivity {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_neighborhoods;
    }

    private RecyclerView mRecyclerView;
    private NeighborhoodAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackArrow();

        mRecyclerView = this.findViewById(R.id.list_neighborhoods);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new NeighborhoodAdapter(this, new ArrayList<Neighborhood>());
        mRecyclerView.setAdapter(mAdapter);
        //registerForContextMenu(mRecyclerView);

        FloatingActionButton addProduct = findViewById(R.id.add_neighborhood);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNeighborhood();
            }
        });

        listNeighborhoods();

    }



    private void listNeighborhoods(){
        ApiClient.get().getNeighborhoods(new GenericCallback<List<Neighborhood>>() {
            @Override
            public void onSuccess(List<Neighborhood> data) {
                mAdapter.setItems(data);
            }

            @Override
            public void onError(Error error) {

            }
        });
    }


    private void addNeighborhood() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.create_neoghborhood, null);
        builder.setView(dialogView);

        final TextView name = dialogView.findViewById(R.id.neighborhood_name);

        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final Button ok = dialogView.findViewById(R.id.ok);
        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Neighborhood n = new Neighborhood(name.getText().toString());
                ApiClient.get().postNeighborhood(n, new GenericCallback<Neighborhood>() {
                    @Override
                    public void onSuccess(Neighborhood data) {

                        mAdapter.pushItem(data);
                    }

                    @Override
                    public void onError(Error error) {
                        DialogHelper.get().showMessage("Error", "No se ha podido crear el barrio", getBaseContext());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_neighborhood, menu);
        return true;
    }

    private void loadNeighborhood(){
        String name=null;
        if(mAdapter.isChecked()){
            name=mAdapter.getItem(mAdapter.getPosChecked()).name;
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",name);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                loadNeighborhood();
                return true;
            case android.R.id.home:
                loadNeighborhood();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
