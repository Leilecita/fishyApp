package com.leila.android.fishy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
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
import com.leila.android.fishy.DialogHelper;
import com.leila.android.fishy.Events.EventOrderState;
import com.leila.android.fishy.R;
import com.leila.android.fishy.activities.CreateOrderActivity;
import com.leila.android.fishy.activities.PhotoEdithActivity;
import com.leila.android.fishy.activities.UserHistoryOrders;
import com.leila.android.fishy.network.ApiClient;
import com.leila.android.fishy.network.ApiUtils;
import com.leila.android.fishy.network.Error;
import com.leila.android.fishy.network.GenericCallback;
import com.leila.android.fishy.network.models.Neighborhood;
import com.leila.android.fishy.network.models.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

/**
 * Created by leila on 19/7/18.
 */

public class UserAdapter extends BaseAdapter<User,UserAdapter.ViewHolder> {

    private Context mContext;
    private ArrayAdapter<String> adapter;
    private boolean validateNeigh;

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

    public UserAdapter(Context context, List<User> users){
        setItems(users);
        mContext = context;
        validateNeigh=false;

    }

    @Subscribe
    public void onEvent(EventOrderState event){
        for(int i=0;i<getListUser().size();++i){
            if(getListUser().get(i).equals(event.getIdUser())){
                if(event.mState.equals("deleted")){
                    getListUser().get(i).pendient_orders-=1;
                }else if(event.mState.equals("created")){
                    getListUser().get(i).pendient_orders+=1;
                }
                updateItem(i,getListUser().get(i));
            }
        }
    }
    public UserAdapter(){

    }

    public List<User> getListUser(){
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView text_name;
        public CircleImageView create;
        public ImageView photo;
        public ImageView note;
        public ImageView debt;


        public ViewHolder(View v){
            super(v);
            text_name= v.findViewById(R.id.text_name);
            create= v.findViewById(R.id.text_add);
            photo=v.findViewById(R.id.photo_user);
            note=v.findViewById(R.id.note);
            debt=v.findViewById(R.id.debt);
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
        if(vh.create!=null){
            vh.create.setImageResource(android.R.color.transparent);
            vh.create.setBorderWidth(0);
            vh.create.setBorderColor(mContext.getResources().getColor(R.color.white));
        }
        if(vh.note!=null)
            vh.note.setImageResource(android.R.color.transparent);
        if(vh.debt!=null)
            vh.debt.setImageResource(android.R.color.transparent);

    }

    private Drawable getDrawableFirstLetter(User user){

        //get first letter of each String item
        String firstLetter = String.valueOf(user.name.charAt(0));
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(user);
        //int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(100)
                .height(100)
                .endConfig()
                .buildRound(firstLetter, color);
        return drawable;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        clearViewHolder(holder);

        final User currentUser=getItem(position);

        if(currentUser.pendient_orders >0){
            holder.create.setBorderWidth(4);
            holder.note.setImageResource(R.drawable.prueba3);
            holder.create.setBorderColor(mContext.getResources().getColor(R.color.FishyLetra));
           // holder.create.setImageResource(R.drawable.fishy_santi2);
            Glide.with(mContext).load(R.drawable.fishy_santi2).into(holder.create);
        }else{
           // holder.create.setImageResource(R.drawable.fishy_santi);
            Glide.with(mContext).load(R.drawable.fishy_santi).into(holder.create);
        }


        holder.photo.setImageDrawable(getDrawableFirstLetter(currentUser));

       /* if(currentUser.image_url==null){
            Glide.with(mContext).load(R.drawable.person_color).into(holder.photo);
        }else{
            Glide.with(mContext).load(ApiUtils.getImageUrl(currentUser.image_url)).into(holder.photo);
        }*/

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

        if(currentUser.defaulter.equals("true")){
            holder.debt.setImageDrawable(mContext.getResources().getDrawable(R.drawable.money));
        }


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


        phoneEdith.setText(userToEdith.getPhone());
        phoneEdith.setTextColor(mContext.getResources().getColor(R.color.word_info));
        addressEdith.setText(userToEdith.getAddress());
        addressEdith.setTextColor(mContext.getResources().getColor(R.color.word_info));
        nameEdith.setText(userToEdith.getName());
        nameEdith.setTextColor(mContext.getResources().getColor(R.color.word_info));
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

                    userToEdith.setName(nameNew);
                    userToEdith.setAddress(addressNew);
                    userToEdith.setPhone(phoneNew);
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
