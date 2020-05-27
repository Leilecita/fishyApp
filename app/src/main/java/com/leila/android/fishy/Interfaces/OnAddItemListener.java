package com.leila.android.fishy.Interfaces;

public interface OnAddItemListener {

        void onAddItemToOrder(Long item_id,Long product_id,Long order_id, Double quantity, boolean create,
                              String product_name, String price_type, Double price,Double cost);

        void reloadProducts();


}
