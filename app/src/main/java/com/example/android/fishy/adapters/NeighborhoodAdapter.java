package com.example.android.fishy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fishy.DialogHelper;
import com.example.android.fishy.R;
import com.example.android.fishy.network.ApiClient;
import com.example.android.fishy.network.Error;
import com.example.android.fishy.network.GenericCallback;
import com.example.android.fishy.network.models.Neighborhood;

import java.util.List;

public class NeighborhoodAdapter extends BaseAdapter<Neighborhood,NeighborhoodAdapter.ViewHolder> {

    private Context mContext;
    private boolean isChecked;
    private Integer posChecked;

    public NeighborhoodAdapter(Context context, List<Neighborhood> neighborhoods) {
        setItems(neighborhoods);
        mContext = context;
        isChecked=false;
        posChecked=null;
    }

    public NeighborhoodAdapter() {

    }

    public Integer getPosChecked(){
        return posChecked;
    }
    public boolean isChecked(){
        return isChecked;
    }

    public List<Neighborhood> getList() {
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView options;
        public CheckBox select;


        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            options = v.findViewById(R.id.options);
            select = v.findViewById(R.id.checkbox);
        }
    }

    @Override
    public NeighborhoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_item_neighborhood, parent, false);
        NeighborhoodAdapter.ViewHolder vh = new NeighborhoodAdapter.ViewHolder(v);

        return vh;
    }

    private void clearViewHolder(NeighborhoodAdapter.ViewHolder vh) {
        if (vh.name != null)
            vh.name.setText(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final NeighborhoodAdapter.ViewHolder holder, final int position) {
        clearViewHolder(holder);

        final Neighborhood currentNeigh = getItem(position);
        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isChecked){
                    holder.select.setChecked(true);
                    isChecked=true;
                    posChecked=position;
                }else if (position==posChecked){
                    holder.select.setChecked(false);
                    isChecked=false;
                    posChecked=null;
                }else if(isChecked && position!=posChecked){
                    holder.select.setChecked(false);
                }
            }
        });


        holder.name.setText(currentNeigh.name);
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mContext, holder.options);
                popup.getMenuInflater().inflate(R.menu.menu_products, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_delete:
                                deleteNeighborhood(currentNeigh, position);
                                return true;
                            case R.id.menu_edit:
                               // edithNeighborhood(currentNeigh, position, holder);

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

    private void deleteNeighborhood(final Neighborhood n, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_delete_neighborhood, null);
        builder.setView(dialogView);
        final TextView name = dialogView.findViewById(R.id.name);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final Button ok = dialogView.findViewById(R.id.ok);

        name.setText(n.name);
        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiClient.get().deleteNeighborhood(n.id, new GenericCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        removeItem(position);
                        Toast.makeText(mContext, "Se ha eliminado el barrio " + name.getText().toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Error error) {
                        DialogHelper.get().showMessage("Error", "Error al eliminar el barrio " + name.getText().toString(), mContext);
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