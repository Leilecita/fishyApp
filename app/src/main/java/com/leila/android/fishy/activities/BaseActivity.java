package com.leila.android.fishy.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.leila.android.fishy.R;

/**
 * Created by leila on 18/7/18.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( getLayoutRes());

    }


    public abstract int getLayoutRes();

    public void showBackArrow(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setContentView(int layoutResId) {
        setContentView(getLayoutInflater().inflate(layoutResId, null));
    }

    @Override
    public void setContentView( View view ) {
        ViewGroup root = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_base, null);
        ViewGroup contentFrame = root.findViewById(R.id.main_content);
        contentFrame.addView(view,0);
        super.setContentView(root);
    }

}
