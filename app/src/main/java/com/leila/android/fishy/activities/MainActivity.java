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
import android.widget.TextView;


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

        actionFloatingButton();
        setImageButton();
        setVisibilityButton();


        for (int i = 0; i < mAdapter.getCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.tab_text);
                View v= tab.getCustomView();
                TextView t= v.findViewById(R.id.text1);

                setTextByPosition(t,i);

            }
        }


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                View im=tab.getCustomView();
                TextView t=im.findViewById(R.id.text1);
                t.setTextColor(getResources().getColor(R.color.white));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View im=tab.getCustomView();
                TextView t=im.findViewById(R.id.text1);
                t.setTextColor(getResources().getColor(R.color.word_clear));

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setImageButton();
                actionFloatingButton();
                setVisibilityButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void setTextByPosition(TextView t, Integer i) {
        if (i == 0) {
            t.setText("CLIENTES");
            t.setTextColor(getResources().getColor(R.color.white));
        } else if (i == 1) {
            t.setText("PEDIDOS");
            t.setTextColor(getResources().getColor(R.color.word_clear));
        } else if (i == 2) {
            t.setText("RESUMEN");
            t.setTextColor(getResources().getColor(R.color.word_clear));
        } else {
            t.setText("GASTOS");
            t.setTextColor(getResources().getColor(R.color.word_clear));
        }
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
            case R.id.action_ventas:
                startIncomesActivity();
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

    private void startIncomesActivity(){
        startActivity(new Intent(MainActivity.this, IncomesActivity.class));
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
