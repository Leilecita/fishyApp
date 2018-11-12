package com.example.android.fishy.network;

import android.util.Log;

import com.example.android.fishy.network.models.AmountResult;
import com.example.android.fishy.network.models.ItemOrder;
import com.example.android.fishy.network.models.Neighborhood;
import com.example.android.fishy.network.models.Order;
import com.example.android.fishy.network.models.Product;
import com.example.android.fishy.network.models.reportsOrder.SummaryDay;
import com.example.android.fishy.network.models.User;

import com.example.android.fishy.network.models.reportsOrder.ReportOrder;
import com.example.android.fishy.network.models.reportsOrder.ValuesDay;
import com.example.android.fishy.network.models.reportsOrder.ValuesOrderReport;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApiClient {

    private static ApiClient INSTANCE = new ApiClient();

    private ApiClient(){}

    public static ApiClient get(){
        return INSTANCE;
    }

    public void getAmountByOrder(Long order_id,GenericCallback<AmountResult> callback){
        handleRequest( ApiUtils.getAPIService().getAmountByOrder("amount", order_id), callback);
    }

    public void finishOrder(Long order_id,GenericCallback<Order> callback){
        handleRequest( ApiUtils.getAPIService().finishOrder("finish", order_id), callback);
    }

    public void getValuesOrderReport(String deliver_date,GenericCallback<ValuesOrderReport> callback){
        handleRequest( ApiUtils.getAPIService().getValuesOrdersReport("getOrdersValues", deliver_date), callback);
    }

    public void getTotalOrdersPendients(GenericCallback<ValuesOrderReport> callback){
        handleRequest( ApiUtils.getAPIService().getTotalOrdersPendient("getTotalOrdersPendient"), callback);
    }

    public void getOrders(Integer page,String deliver_date,String zone,String time,String query,GenericCallback<List<ReportOrder>> callback){
        handleRequest( ApiUtils.getAPIService().getOrders("listAndSearchOrders",page, deliver_date,zone,time,query), callback);
    }

    public void getSummaryDay(String deliver_date,GenericCallback<List<SummaryDay>> callback){
        handleRequest( ApiUtils.getAPIService().getSummaryDay("summaryDay", deliver_date), callback);
    }

    public void getValuesDay(String deliver_date,GenericCallback<ValuesDay> callback){
        handleRequest( ApiUtils.getAPIService().getValuesDay("summaryDayValues", deliver_date), callback);
    }

   /* public void getOrdersByZoneAndTime(String deliver_date,String zone,String time,GenericCallback<List<ReportOrder>> callback){
        handleRequest( ApiUtils.getAPIService().getOrdersByZoneAndTime("zoneAndTime", deliver_date,zone,time), callback);
    }*/

    public void updatePriority(Long order_id,Integer priority,GenericCallback<Order> callback){
        handleRequest( ApiUtils.getAPIService().updatePriority("priority", order_id,priority), callback);
    }

    public void getOrdersReport(String deliver_date,GenericCallback<List<ReportOrder>> callback){
        handleRequest( ApiUtils.getAPIService().getOrdersReport(deliver_date), callback);
    }

    public void getOrdersReportByUserId(Long user_id,GenericCallback<List<ReportOrder>> callback){
        handleRequest( ApiUtils.getAPIService().getOrdersReportByUserId(user_id), callback);
    }

    public void getOrdersReportByUserIdByPage(Integer page,Long user_id ,GenericCallback<List<ReportOrder>> callback){
        handleRequest( ApiUtils.getAPIService().getOrdersReportByUserIdByPage(page,user_id), callback);
    }


    public void getProducts(final GenericCallback<List<Product>> callback){
        handleRequest( ApiUtils.getAPIService().getProducts(), callback);
    }

    public void getAliveProducts(String state,final GenericCallback<List<Product>> callback){
        handleRequest( ApiUtils.getAPIService().getAliveProducts(state), callback);
    }

    public void getProduct(Long id,final GenericCallback<Product> callback){
        handleRequest( ApiUtils.getAPIService().getProduct(id), callback);
    }

    public void postProduct(Product product,GenericCallback<Product> callback){
        handleRequest( ApiUtils.getAPIService().postProduct(product), callback);
    }

    public void putProduct(Product product,GenericCallback<Product> callback){
        handleRequest( ApiUtils.getAPIService().putProduct(product), callback);
    }

    public void deleteProduct(Long id, final GenericCallback<Void> callback){
        handleDeleteRequest( ApiUtils.getAPIService().deleteProduct(id), callback);
    }

    public void getItemsOrder(final GenericCallback<List<ItemOrder>> callback){
        handleRequest( ApiUtils.getAPIService().getItemsOrder(), callback);
    }

    public void getItemsOrderByOrderId(Long orderId, final GenericCallback<List<ItemOrder>> callback){
        handleRequest( ApiUtils.getAPIService().getItemsOrderByOrderId(orderId), callback);
    }

    public void getItemOrder(Long id,final GenericCallback<ItemOrder> callback){
        handleRequest( ApiUtils.getAPIService().getItemOrder(id), callback);
    }

    public void postItemOrder(ItemOrder itemOrder,GenericCallback<ItemOrder> callback){
        handleRequest( ApiUtils.getAPIService().postItemOrder(itemOrder), callback);
    }

    public void putItemOrder(ItemOrder itemOrder,GenericCallback<ItemOrder> callback){
        handleRequest( ApiUtils.getAPIService().putItemOrder(itemOrder), callback);
    }

    public void deleteItemOrder(Long id, final GenericCallback<Void> callback){
        handleDeleteRequest( ApiUtils.getAPIService().deleteItemOrder(id), callback);
    }


    public void getOrders(final GenericCallback<List<Order>> callback){
        handleRequest( ApiUtils.getAPIService().getOrders(), callback);
    }

    public void getOrder(Long id,final GenericCallback<Order> callback){
        handleRequest( ApiUtils.getAPIService().getOrder(id), callback);
    }

    public void postOrder(Order order,GenericCallback<Order> callback){
        handleRequest( ApiUtils.getAPIService().postOrder(order), callback);
    }

    public void putOrder(Order order,GenericCallback<Order> callback){
        handleRequest( ApiUtils.getAPIService().putOrder(order), callback);
    }

    public void deleteOrder(Long id, final GenericCallback<Void> callback){
        handleDeleteRequest( ApiUtils.getAPIService().deleteOrder(id), callback);
    }

    public void getUser(Long id,final GenericCallback<User> callback){
        handleRequest( ApiUtils.getAPIService().getUser(id), callback);
    }

    public void searchUsers(String query, Integer page,final GenericCallback<List<User>> callback ){
        handleRequest( ApiUtils.getAPIService().getUsersByPage(page,query), callback);

    }

    public void getUsersByPage(Integer page, final GenericCallback<List<User>> callback){
        handleRequest( ApiUtils.getAPIService().getUsersByPage(page,null), callback);
    }

    public void getUsers(final GenericCallback<List<User>> callback){
        handleRequest( ApiUtils.getAPIService().getUsers(), callback);
    }

    public void postUser(User user,GenericCallback<User> callback){
        handleRequest( ApiUtils.getAPIService().postUser(user), callback);
    }

    public void putUser(User user,GenericCallback<User> callback){
        handleRequest( ApiUtils.getAPIService().putUser(user), callback);
    }

    public void deleteUser(Long id, final GenericCallback<Void> callback){
        handleDeleteRequest( ApiUtils.getAPIService().deleteUser(id), callback);
    }

    public void getNeighborhoods(final GenericCallback<List<Neighborhood>> callback){
        handleRequest( ApiUtils.getAPIService().getNeighborhoods(), callback);
    }

    public void existNeighborhood(String name, final GenericCallback<Neighborhood> callback){
        handleRequest( ApiUtils.getAPIService().existNeighborhood(name), callback);
    }

    public void postNeighborhood(Neighborhood neighborhood,GenericCallback<Neighborhood> callback){
        handleRequest( ApiUtils.getAPIService().postNeighborhood(neighborhood), callback);
    }

    public void deleteNeighborhood(Long id, final GenericCallback<Void> callback){
        handleDeleteRequest( ApiUtils.getAPIService().deleteNeighborhood(id), callback);
    }

    public void putNeighborhood(Neighborhood n,GenericCallback<Neighborhood> callback){
        handleRequest( ApiUtils.getAPIService().putNeighborhood(n), callback);
    }


    private <T> void handleRequest(Observable<Response<T>> request, final GenericCallback<T> callback){
        request.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<T>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Error error = new Error();
                        error.result = "error";
                        error.message = "Generic Error";
                        e.printStackTrace();
                        if( e instanceof HttpException){
                            try {
                                String body = ((HttpException) e).response().errorBody().string();
                                Gson gson = new Gson();
                                error =  gson.fromJson(body,Error.class);
                            }catch (Exception e1){
                                e1.printStackTrace();
                            }
                        }
                        callback.onError(error);
                    }

                    @Override
                    public void onNext(Response<T>  response) {
                        callback.onSuccess(response.data);
                    }
                });
    }

    private void handleDeleteRequest(Observable<ResponseBody> request, final GenericCallback<Void> callback){
        request.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Error error = new Error();
                        error.result = "error";
                        error.message = "Generic Error";
                        e.printStackTrace();
                        Log.e("RETRO", e.getMessage());
                        if( e instanceof HttpException){
                            try {
                                String body = ((HttpException) e).response().errorBody().string();
                                Gson gson = new Gson();
                                error =  gson.fromJson(body,Error.class);
                                Log.e("RETRO", body);
                            }catch (Exception e1){
                                e1.printStackTrace();
                            }
                        }
                        callback.onError(error);
                    }

                    @Override
                    public void onNext(ResponseBody response) {
                        callback.onSuccess(null);
                    }
                });
    }


}
