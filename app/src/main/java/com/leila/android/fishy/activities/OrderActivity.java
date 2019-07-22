package com.leila.android.fishy.activities;

import android.os.Bundle;

import com.leila.android.fishy.Fragments.OrdersFragment;
import com.leila.android.fishy.R;

public class OrderActivity extends BaseActivity {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackArrow();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new OrdersFragment())
                .commit();

    }
}