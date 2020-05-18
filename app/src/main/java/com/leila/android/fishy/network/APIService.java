package com.leila.android.fishy.network;

import com.leila.android.fishy.network.models.AmountResult;
import com.leila.android.fishy.network.models.Income;
import com.leila.android.fishy.network.models.ItemOrder;
import com.leila.android.fishy.network.models.Neighborhood;
import com.leila.android.fishy.network.models.Order;
import com.leila.android.fishy.network.models.Outcome;
import com.leila.android.fishy.network.models.Product;
import com.leila.android.fishy.network.models.ReportIncome;
import com.leila.android.fishy.network.models.ReportOutcome;
import com.leila.android.fishy.network.models.ReportProduct;
import com.leila.android.fishy.network.models.ReportUsers;
import com.leila.android.fishy.network.models.reportsOrder.SummaryDay;
import com.leila.android.fishy.network.models.User;
import com.leila.android.fishy.network.models.reportsOrder.ReportOrder;
import com.leila.android.fishy.network.models.reportsOrder.ValuesDay;
import com.leila.android.fishy.network.models.reportsOrder.ValuesOrderReport;
import com.leila.android.fishy.network.models.reportsStatistics.ReportStatistics;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;
/**
 * Created by leila on 17/7/18.
 */

public interface APIService {

    @GET("items_order.php")
    Observable<Response<AmountResult>>  getAmountByOrder(@Query("method") String method, @Query("order_id") Long order_id);

    @GET("orders.php")
    Observable<Response<Order>>  finishOrder(@Query("method") String method, @Query("order_id") Long order_id);

    @GET("orders.php")
    Observable<Response<Order>>  send_account(@Query("method") String method,@Query("state") String state, @Query("order_id") Long order_id);

    @GET("orders.php")
    Observable<Response<Order>>  done_payment(@Query("method") String method,@Query("state") String state, @Query("order_id") Long order_id,@Query("debt_value") Double debt_value);

    @GET("orders.php")
    Observable<Response<ValuesOrderReport>>  getValuesOrdersReport(@Query("method") String method, @Query("deliver_date") String date);

    @GET("orders.php")
    Observable<Response<ValuesOrderReport>>  getTotalOrdersPendient(@Query("method") String method);

    @GET("orders.php")
    Observable<Response<List<ReportStatistics>>>  getReportStatistics(@Query("method") String method);

    @GET("orders.php")
    Observable<Response<List<ReportOrder>>>  searchOrders(@Query("method") String method, @Query("deliver_date") String date, @Query("query") String query);


    @GET("orders.php")
    Observable<Response<List<ReportOrder>>>  getOrdersByZoneAndTime(@Query("method") String method, @Query("deliver_date") String date, @Query("zone") String zone, @Query("time") String time);

    @GET("orders.php")
    Observable<Response<List<ReportOrder>>>  getOrders(@Query("method") String method,@Query("page") Integer page, @Query("deliver_date") String date, @Query("zone") String zone, @Query("time") String time, @Query("query") String query);


    @GET("orders.php")
    Observable<Response<Order>>  updatePriority(@Query("method") String method, @Query("order_id") Long order_id, @Query("priority") Integer priority);
/*
    @GET("products.php")
    Observable<Response<List<Product>>> getProducts();

    @GET("products.php")
    Observable<Response<List<Product>>> getAliveProducts(@Query("state") String state);

*/

    //PRODUCTS

    @GET("products.php")
    Observable<Response<List<ReportProduct>>> getAliveProductsByPage(@Query("method") String method, @Query("page") Integer page, @Query("state") String state);

    @GET("products.php")
    Observable<Response<Product>> getProduct(@Query("id") Long id);

    @POST("products.php")
    Observable<Response<Product>> postProduct(@Body Product product);

    @PUT("products.php")
    Observable<Response<Product>> putProduct(@Body Product product);

    @DELETE("products.php")
    Observable<ResponseBody>  deleteProduct(@Query("id") Long id);

    //ITEMS_ORDER

    @GET("items_order.php")
    Observable<Response<List<ItemOrder>>> getItemsOrder();

    @GET("items_order.php")
    Observable<Response<List<ItemOrder>>> getItemsOrderByOrderId(@Query("order_id") Long id);

    @GET("items_order.php")
    Observable<Response<ItemOrder>> getItemOrder(@Query("id") Long id);

    @POST("items_order.php")
    Observable<Response<ItemOrder>> postItemOrder(@Body ItemOrder itemOrder);


    @PUT("items_order.php")
    Observable<Response<ItemOrder>> putItemOrder(@Body ItemOrder itemOrder);


    @DELETE("items_order.php")
    Observable<ResponseBody>  deleteItemOrder(@Query("id") Long id);

    //ORDERS

    @GET("orders.php")
    Observable<Response<Order>> getOrder(@Query("id") Long id);

    @POST("orders.php")
    Observable<Response<Order>> postOrder(@Body Order product);

    @PUT("orders.php")
    Observable<Response<Order>> putOrder(@Body Order product);

    @DELETE("orders.php")
    Observable<ResponseBody>  deleteOrder(@Query("id") Long id);


/*    @GET("orders.php")
    Observable<Response<List<ReportOrder>>>  getOrdersReport( @Query("deliver_date") String date);

    @GET("orders.php")
    Observable<Response<List<ReportOrder>>>  getOrdersReportByUserId( @Query("user_id") Long user_id);
    */

    @GET("orders.php")
    Observable<Response<List<SummaryDay>>>  getSummaryDay(@Query("method") String method, @Query("deliver_date") String date);

    @GET("orders.php")
    Observable<Response<ValuesDay>>  getValuesDay(@Query("method") String method, @Query("deliver_date") String date);

    @GET("orders.php")
    Observable<Response<List<ReportOrder>>>  getOrdersReportByUserIdByPage(@Query("page") Integer page, @Query("user_id") Long user_id);


    //USERS

    @GET("users.php")
    Observable<Response<List<User>>> getUsers();

    @GET("users.php")
    Observable<Response<ReportUsers>> getUsersByPage(@Query("method") String method, @Query("page") Integer page, @Query("query") String query );




    @GET("users.php")
    Observable<Response<User>> getUser(@Query("id") Long id);

    @POST("users.php")
    Observable<Response<User>> postUser(@Body User user);

    @PUT("users.php")
    Observable<Response<User>> putUser(@Body User user);

    @DELETE("users.php")
    Observable<ResponseBody>  deleteUser(@Query("id") Long id);

    @GET("neighborhoods.php")
    Observable<Response<List<Neighborhood>>> getNeighborhoods();

    @GET("neighborhoods.php")
    Observable<Response<List<Neighborhood>>> getNeighborhoodsByPage(@Query("page") Integer page);

    @GET("neighborhoods.php")
    Observable<Response<Neighborhood>> existNeighborhood(@Query("name") String name);

    @POST("neighborhoods.php")
    Observable<Response<Neighborhood>> postNeighborhood(@Body Neighborhood neighborhood);

    @PUT("neighborhoods.php")
    Observable<Response<Neighborhood>> putNeighborhood(@Body Neighborhood neighborhood);

    @DELETE("neighborhoods.php")
    Observable<ResponseBody>  deleteNeighborhood(@Query("id") Long id);

    //outcomes

    @GET("outcomes.php")
    Observable<Response<List<ReportOutcome>>> getOutcomes(@Query("method") String method,@Query("page") Integer page,@Query("type") String type);

    @POST("outcomes.php")
    Observable<Response<Outcome>> postOutcome(@Body Outcome outcome);

    @PUT("outcomes.php")
    Observable<Response<Outcome>> putOutcome(@Body Outcome outcome);

    @DELETE("outcomes.php")
    Observable<ResponseBody>  deleteOutcome(@Query("id") Long id);

    //incomes

    @GET("incomes.php")
    Observable<Response<List<ReportIncome>>> getIncomes(@Query("method") String method, @Query("page") Integer page,@Query("type") String type);

    @POST("incomes.php")
    Observable<Response<Income>> postIncome(@Body Income income);

}
