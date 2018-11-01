package com.example.android.fishy.activities;

import android.os.Bundle;

import com.example.android.fishy.Fragments.UsersFragment;
import com.example.android.fishy.R;

/**
 * Created by leila on 19/7/18.
 */

public class UserActivity extends BaseActivity {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackArrow();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new UsersFragment())
                .commit();

    }
}
