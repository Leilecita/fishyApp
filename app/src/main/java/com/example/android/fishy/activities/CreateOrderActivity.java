package com.example.android.fishy.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fishy.DialogHelper;
import com.example.android.fishy.Interfaces.OnAddItemListener;
import com.example.android.fishy.R;
import com.example.android.fishy.adapters.ItemOrderAdapter;
import com.example.android.fishy.adapters.ProductAdapter;
import com.example.android.fishy.adapters.ProductOrderAdapter;
import com.example.android.fishy.network.ApiClient;
import com.example.android.fishy.network.Error;
import com.example.android.fishy.network.GenericCallback;
import com.example.android.fishy.network.models.AmountResult;
import com.example.android.fishy.network.models.ItemOrder;
import com.example.android.fishy.network.models.Order;
import com.example.android.fishy.network.models.Product;
import com.example.android.fishy.network.models.User;
import com.example.android.fishy.network.models.reportsOrder.ReportOrder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateOrderActivity extends BaseActivity implements OnAddItemListener {

    String mDeliverDate;
    String mDeliveryTime;
    Order mOrder;

    TextView deliverDate;
    TextView deliveryTime;
    TextView userName;

    TextView totalAmount;
    boolean mEdithOrder;

    List<ItemOrder> listItemsToEdith;

    public static void start(Context mContext, User user){
        Intent i=new Intent(mContext, CreateOrderActivity.class);
        i.putExtra("ID",user.id);
        i.putExtra("NAME",user.getName());
        i.putExtra("DIRECCION",user.address);
        mContext.startActivity(i);
    }

    public static void startEdithOrder(Context mContext, ReportOrder reportOrder){
        Intent i=new Intent(mContext, CreateOrderActivity.class);
        i.putExtra("ORDERID",reportOrder.order_id);
        i.putExtra("DELIVERDATE",reportOrder.deliver_date);
        i.putExtra("USERNAME",reportOrder.name);
        mContext.startActivity(i);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_create_order;
    }

    private RecyclerView mRecyclerView;
    private RecyclerView mItemRecyclerView;
    private ProductOrderAdapter mAdapter;
    private ItemOrderAdapter mItemAdapter;
    private GridLayoutManager gridlayoutmanager;

    private RecyclerView.LayoutManager layoutManagerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackArrow();

        mEdithOrder=false;
        mOrder=null;
        mDeliverDate=null;
        mDeliveryTime=null;

        mRecyclerView = this.findViewById(R.id.list_products);
        gridlayoutmanager=new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(gridlayoutmanager);
        mAdapter = new ProductOrderAdapter(this, new ArrayList<Product>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnAddItemListener(this);

        mItemRecyclerView = this.findViewById(R.id.list_items);
        layoutManagerItem = new LinearLayoutManager(this);
        mItemRecyclerView.setLayoutManager(layoutManagerItem);
        mItemAdapter = new ItemOrderAdapter(this, new ArrayList<ItemOrder>());
        mItemRecyclerView.setAdapter(mItemAdapter);
        mItemAdapter.setOnAddItemListener(this);

        totalAmount= findViewById(R.id.total_amount);
        userName= findViewById(R.id.name);
        deliverDate=findViewById(R.id.deliver_date);
        deliveryTime=findViewById(R.id.delivery_time);

        initActivity();
        // createOrder();


        deliverDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDeliverDate();
            }
        });
        deliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDeliveryTime();
            }
        });

    }

    private void selectDeliveryTime(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] items = {
                "Mañana", "Mediodía", "Tarde"
        };

        builder.setTitle("Elige el horario de entrega");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                deliveryTime.setText(items[item]);
                mDeliveryTime=(items[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void editItems(){

        List<ItemOrder> list =mItemAdapter.getListItem();
        for (int i =0; i < list.size();++i){
            ApiClient.get().putItemOrder(list.get(i), new GenericCallback<ItemOrder>() {
                @Override
                public void onSuccess(ItemOrder data) {

                }

                @Override
                public void onError(Error error) {

                }
            });
        }

    }

    private void initActivity(){
        Long order_id= getIntent().getLongExtra("ORDERID",-1);
        if(order_id != -1){ //esta editando
            mEdithOrder=true;

            mDeliverDate=getIntent().getStringExtra("DELIVERDATE");
            ApiClient.get().getOrder(order_id, new GenericCallback<Order>() {
                @Override
                public void onSuccess(Order data) {
                    mOrder=data;
                    mItemAdapter.setIsUpdating(true);
                    onAddItemToOrder();
                    listProducts();
                    deliverDate.setText(mDeliverDate);
                    userName.setText(getIntent().getStringExtra("USERNAME"));
                }

                @Override
                public void onError(Error error) {

                }
            });

        }else{
            System.out.println("se crea orden");
            userName.setText(getIntent().getStringExtra("NAME"));

            createOrder();
        }

    }

    private void getAmount(){
        ApiClient.get().getAmountByOrder(mOrder.id, new GenericCallback<AmountResult>() {
            @Override
            public void onSuccess(AmountResult data) {
                totalAmount.setText(String.valueOf(data.total));
            }

            @Override
            public void onError(Error error) {

            }
        });

    }

    public void onAddItemToOrder(){

        mItemAdapter.clear();
        getAmount();
        listItems();
    }

    //todo borrar todos los pedidos con ordern nro mIdOrder
    private void deleteOrderAndAllItems(){
        if(!mEdithOrder){
            //si se cancela el pedido cuando se CREA la orden , se borra la orden y todos los items.
            //si se estaba editando no es necesario

            ApiClient.get().deleteOrder(mOrder.id, new GenericCallback<Void>() {
                @Override
                public void onSuccess(Void data) {

                }

                @Override
                public void onError(Error error) {
                    DialogHelper.get().showMessage("Error","No se ha podido cancelar el pedido",getBaseContext());
                }
            });

        }


    }

    private void createOrder(){
        Long userid= getIntent().getLongExtra("ID",-1);
        Order order=new Order(userid,"","","borrador","");

        ApiClient.get().postOrder(order, new GenericCallback<Order>() {
            @Override
            public void onSuccess(Order data) {
                mOrder=data;
                listProducts();

            }

            @Override
            public void onError(Error error) {

            }
        });
    }

    private void listProducts(){
        ApiClient.get().getProducts(new GenericCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> data) {
                mAdapter.setOrderId(mOrder.id);
                mAdapter.setItems(data);
            }

            @Override
            public void onError(Error error) {

            }
        });
    }

    private void listItems(){

       if(mOrder!=null){
           ApiClient.get().getItemsOrderByOrderId(mOrder.id, new GenericCallback<List<ItemOrder>>() {
               @Override
               public void onSuccess(List<ItemOrder> data) {

                   mItemAdapter.setItems(data);
                   //si quiero editar
                   listItemsToEdith=data;
               }

               @Override
               public void onError(Error error) {

               }
           });
       }
    }

    private void selectDeliverDate(){

        final DatePickerDialog datePickerDialog;
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        datePickerDialog = new DatePickerDialog(CreateOrderActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        String sdayOfMonth = String.valueOf(dayOfMonth);
                        if (sdayOfMonth.length() == 1) {
                            sdayOfMonth = "0" + dayOfMonth;
                        }

                        String smonthOfYear = String.valueOf(monthOfYear + 1);
                        if (smonthOfYear.length() == 1) {
                            smonthOfYear = "0" + smonthOfYear;
                        }
                        mDeliverDate=sdayOfMonth+"-"+smonthOfYear+"-"+year;
                        deliverDate.setText(mDeliverDate);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void putDeliverDate(){
        if(mOrder!=null){
            mOrder.setDeliver_date(mDeliverDate);
            mOrder.state="pendiente";
            if(mDeliveryTime!= null){
                mOrder.delivery_time=mDeliveryTime;
            }else{
                mOrder.delivery_time="Horario";
            }

            ApiClient.get().putOrder(mOrder, new GenericCallback<Order>() {
                @Override
                public void onSuccess(Order data) {
                    Toast.makeText(CreateOrderActivity.this,"Fecha de entrega: "+mDeliverDate,Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(Error error) {
                    DialogHelper.get().showMessage("Error","No se ha podido guardar la fecha",getBaseContext());
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if(mDeliverDate!=null){
                    putDeliverDate();
                    if(!mEdithOrder){
                        Toast.makeText(CreateOrderActivity.this,"El pedido ha sido creado para "+getIntent().getStringExtra("NAME"),Toast.LENGTH_LONG).show();
                    }else{
                        editItems();
                        Toast.makeText(CreateOrderActivity.this,"El pedido ha sido actualizado ",Toast.LENGTH_LONG).show();
                    }
                    finish();
                }else{
                    Toast.makeText(this, "No se ha elegido día de entrega",Toast.LENGTH_LONG).show();
                }
                return true;
            case android.R.id.home:
                    showDialogCancelOrder();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogCancelOrder(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_cancel_order, null);
        builder.setView(dialogView);

        TextView cancel= dialogView.findViewById(R.id.cancel);
        final TextView ok= dialogView.findViewById(R.id.ok);
        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOrderAndAllItems();
                finish();
               // NavUtils.navigateUpFromSameTask(CreateOrderActivity.this);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           showDialogCancelOrder();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
