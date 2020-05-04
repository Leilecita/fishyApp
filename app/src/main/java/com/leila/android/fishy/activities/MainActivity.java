package com.leila.android.fishy.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.os.Bundle;


import com.leila.android.fishy.Fragments.BaseFragment;
import com.leila.android.fishy.R;
import com.leila.android.fishy.adapters.PageAdapter;
import com.leila.android.fishy.network.ApiClient;

import android.support.design.widget.TabLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends BaseActivity {
    PageAdapter mAdapter;
    TabLayout mTabLayout;

    FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPager viewPager =  findViewById(R.id.viewpager);
        mAdapter = new PageAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);

        mTabLayout =  findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(viewPager);
        mTabLayout.setSelectedTabIndicatorHeight(10);



        button= findViewById(R.id.fab_agregarTod);
       // input=findViewById(R.id.input);

        actionFloatingButton();
        setImageButton();
        setVisibilityButton();
        //setInputType();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setImageButton();
                actionFloatingButton();
                setVisibilityButton();
          //      setInputType();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_products:
                startProductsActivity();
                return true;
            case R.id.action_statistics:
                startStatisticActivity();
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startProductsActivity(){
        startActivity(new Intent(MainActivity.this, ProductsActivity.class));
    }

    private void startStatisticActivity(){
        startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
    }


    public void actionFloatingButton(){
        int position = mTabLayout.getSelectedTabPosition();
        final Fragment f = mAdapter.getItem(position);

            if(f instanceof BaseFragment){
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((BaseFragment)f).onClickButton();
                    }
                });
            }
    }


    public void refreshFragment(){
        int position = mTabLayout.getSelectedTabPosition();
        Fragment f = mAdapter.getItem(position);
        if( f instanceof BaseFragment){
            ((BaseFragment)f).onRefresh();
        }
    }

 /*   public void onClickAction(){
        int position = mTabLayout.getSelectedTabPosition();
        final Fragment f = mAdapter.getItem(position);

        if(f instanceof BaseFragment){
            input.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((BaseFragment)f).onClickAction();
                }
            });
        }

    }*/

   /* public void setInputType(){
        int position = mTabLayout.getSelectedTabPosition();
        Fragment f = mAdapter.getItem(position);

        if( f instanceof BaseFragment){
            input.setHint(((BaseFragment)f).getHint());

        }

    }*/

    public void setVisibilityButton(){
        int position = mTabLayout.getSelectedTabPosition();
        Fragment f = mAdapter.getItem(position);

        if( f instanceof BaseFragment){
            button.setVisibility(((BaseFragment)f).getVisibility());
        }


    }
    public void setImageButton(){
        int position = mTabLayout.getSelectedTabPosition();
        Fragment f = mAdapter.getItem(position);

            if( f instanceof BaseFragment){
                button.setImageResource(((BaseFragment)f).getIconButton());
            }



    }
}