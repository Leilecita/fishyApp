package com.example.android.fishy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.fishy.DialogHelper;
import com.example.android.fishy.R;
import com.example.android.fishy.activities.CreateOrderActivity;
import com.example.android.fishy.activities.PhotoEdithActivity;
import com.example.android.fishy.activities.UserHistoryOrders;
import com.example.android.fishy.network.ApiClient;
import com.example.android.fishy.network.ApiUtils;
import com.example.android.fishy.network.Error;
import com.example.android.fishy.network.GenericCallback;
import com.example.android.fishy.network.models.Neighborhood;
import com.example.android.fishy.network.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leila on 19/7/18.
 */

public class UserAdapter extends BaseAdapter<User,UserAdapter.ViewHolder> {

    private Context mContext;
    private ArrayAdapter<String> adapter;
    private boolean validateNeigh;

    public UserAdapter(Context context, List<User> users){
        setItems(users);
        mContext = context;
        validateNeigh=false;
    }

    public UserAdapter(){

    }

    public List<User> getListUser(){
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView text_name;
        public ImageView create;
        public ImageView photo;


        public ViewHolder(View v){
            super(v);
            text_name= v.findViewById(R.id.text_name);
            create= v.findViewById(R.id.text_add);
            photo=v.findViewById(R.id.photo_user);

        }
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_item_user,parent,false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    private void clearViewHolder(UserAdapter.ViewHolder vh){
        if(vh.text_name!=null)
            vh.text_name.setText(null);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        clearViewHolder(holder);

        final User currentUser=getItem(position);

        if(currentUser.pendient_orders >0){
            holder.create.setImageResource(R.drawable.fishy_azul);
        }

        if(currentUser.image_url==null){
            Glide.with(mContext).load(R.drawable.person_color).into(holder.photo);
        }else{
            Glide.with(mContext).load(ApiUtils.getImageUrl(currentUser.image_url)).into(holder.photo);
            //System.out.println("carga foto, :"+currentUser.getName());
        }

        holder.text_name.setText(currentUser.name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createInfoDialog(currentUser,position);
            }
        });
        holder.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startCreateOrderActivity(currentUser);
            }
        });



        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.dialog_edith_photo, null);
                final ImageView photo_info=  dialogView.findViewById(R.id.image_user);
                Glide.with(mContext).load(ApiUtils.getImageUrl(currentUser.getImageUrl())).into(photo_info);
                ImageView edit= dialogView.findViewById(R.id.edit_photo);

                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        PhotoEdithActivity.start(mContext,currentUser);
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });


    }

    private void startCreateOrderActivity(User u){
        CreateOrderActivity.start(mContext,u);
    }

    private void startUserHistoryActivity(User u){
        UserHistoryOrders.start(mContext,u);
    }

    private void createInfoDialog(final User u, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_user_information, null);

        builder.setView(dialogView);

        final TextView name=  dialogView.findViewById(R.id.user_name);
        final TextView address=  dialogView.findViewById(R.id.user_address);
        final TextView neighbor=  dialogView.findViewById(R.id.user_neighboor);
        final TextView phone=  dialogView.findViewById(R.id.user_phone);
        final ImageView delete=  dialogView.findViewById(R.id.deleteuser);
        final ImageView edituser=  dialogView.findViewById(R.id.edituser);
        final ImageView history=  dialogView.findViewById(R.id.historyuser);
        final ImageView call=  dialogView.findViewById(R.id.phone);

        name.setText(u.getName());
        address.setText(u.getAddress());
        phone.setText(u.getPhone());
        neighbor.setText(u.neighborhood);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + u.phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        final AlertDialog dialog = builder.create();
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startUserHistoryActivity(u);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(u,position);
                dialog.dismiss();
            }
        });

        edituser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edithUser(u,position, new OnUserEditedCallback() {
                    @Override
                    public void onUserEdited(User newUser) {
                        name.setText(newUser.getName());
                        address.setText(newUser.getAddress());
                        phone.setText(newUser.getPhone());
                        neighbor.setText(newUser.getNeighborhood());
                    }
                });

            }
        });

        dialog.show();
    }

    private void deleteUser( final User u,final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.dialog_delete_user, null);
        builder.setView(dialogView);

        final TextView name=  dialogView.findViewById(R.id.user_name);
        final TextView address=  dialogView.findViewById(R.id.user_address);
        final TextView phone=  dialogView.findViewById(R.id.user_phone);
        final TextView cancel=  dialogView.findViewById(R.id.cancel);
        final Button ok=  dialogView.findViewById(R.id.ok);

        name.setText(u.getName());
        phone.setText(u.getPhone());
        address.setText(u.getAddress());

        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                ApiClient.get().deleteUser(u.id, new GenericCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        Toast.makeText(dialogView.getContext(), "El usuario "+u.getName()+" ha sido eliminado.", Toast.LENGTH_LONG).show();
                        removeItem(position);
                    }
                    @Override
                    public void onError(Error error) {
                        DialogHelper.get().showMessage("Error","El usuario a eliminar no existe",dialogView.getContext());
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

    private void edithUser(final User userToEdith, final int position, final OnUserEditedCallback callback){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_edith_user, null);

        builder.setView(dialogView);

        final EditText nameEdith=dialogView.findViewById(R.id.edith_name);
        final EditText addressEdith=dialogView.findViewById(R.id.edith_address);
        final EditText phoneEdith=dialogView.findViewById(R.id.edith_phone);
        final AutoCompleteTextView neighborEdith=dialogView.findViewById(R.id.edith_neighborhood);

        phoneEdith.setHint(userToEdith.getPhone());
        phoneEdith.setHintTextColor(mContext.getResources().getColor(R.color.word_info));
        addressEdith.setHint(userToEdith.getAddress());
        addressEdith.setHintTextColor(mContext.getResources().getColor(R.color.word_info));
        nameEdith.setHint(userToEdith.getName());
        nameEdith.setHintTextColor(mContext.getResources().getColor(R.color.word_info));

        neighborEdith.setHint(userToEdith.getNeighborhood());
        neighborEdith.setHintTextColor(mContext.getResources().getColor(R.color.word_info));

        listNeighborhoods(neighborEdith);

        final TextView cancel=  dialogView.findViewById(R.id.cancel);
        final Button ok=  dialogView.findViewById(R.id.ok);

        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                neighborEdith.clearFocus();

                final String nameNew= nameEdith.getText().toString().trim();
                String addressNew= addressEdith.getText().toString().trim();
                String phoneNew= phoneEdith.getText().toString().trim();
                String neighborNew= neighborEdith.getText().toString().trim();

                if(!nameNew.matches("")){
                    userToEdith.setName(nameNew);
                }
                if(!addressNew.matches("")){
                    userToEdith.setAddress(addressNew);
                }
                if(!phoneNew.matches("")){
                    userToEdith.setPhone(phoneNew);
                }
                if(!neighborNew.matches("")){
                    userToEdith.setNeighborhood(neighborNew);
                }


                if(!validateNeigh && !neighborNew.matches("") ){
                    Toast.makeText(mContext, "El barrio es inválido",Toast.LENGTH_LONG).show();

                }else{

                    ApiClient.get().putUser(userToEdith, new GenericCallback<User>() {
                        @Override
                        public void onSuccess(User data) {
                            notifyItemChanged(position);
                            userToEdith.setNeighborhood(data.getNeighborhood());

                            Toast.makeText(mContext, "El usuario ha sido editado",Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onError(Error error) {
                            DialogHelper.get().showMessage("Error","Error al editar usuario",mContext);
                        }
                    });

                    dialog.dismiss();
                    callback.onUserEdited(userToEdith);

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

    interface OnUserEditedCallback {
        void onUserEdited(User user);
    }

    private void listNeighborhoods(final AutoCompleteTextView neig){

        ApiClient.get().getNeighborhoods(new GenericCallback<List<Neighborhood>>() {
            @Override
            public void onSuccess(List<Neighborhood> data) {
                final List<String> listneighborhoods=createArray(data);
                adapter = new ArrayAdapter<String>(mContext,
                        android.R.layout.simple_dropdown_item_1line, listneighborhoods);
                neig.setThreshold(1);
                neig.setAdapter(adapter);
                neig.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b) {
                            String val = neig.getText() + "";

                            if(listneighborhoods.contains(val)){
                                validateNeigh=true;

                            }else{
                                validateNeigh=false;
                                neig.setError("Barrio inválido");
                            }
                        }else{
                        }
                    }
                });
            }

            @Override
            public void onError(Error error) {
                DialogHelper.get().showMessage("Error","No se han podido cargar los barrios",mContext);
            }
        });
    }
    private List<String> createArray(List<Neighborhood> list){
        List<String> listN=new ArrayList<>();
        for(int i=0; i < list.size();++i){
            if(list.get(i) != null && list.get(i).name != null){
                listN.add(list.get(i).getName());
            }
        }
        return listN;
    }
}
