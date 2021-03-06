package com.example.android.fishy.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.fishy.Fragments.BaseFragment;
import com.example.android.fishy.Fragments.OrdersFragment;
import com.example.android.fishy.Fragments.SummaryDayFragment;
import com.example.android.fishy.Fragments.UsersFragment;

import java.util.ArrayList;

/**
 * Created by leila on 19/7/18.
 */

public class PageAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private ArrayList<BaseFragment> mFragments;

    public PageAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
            mFragments = new ArrayList<>();
          //  mFragments.add(new SpendingFragment());
            mFragments.add(new UsersFragment());
            mFragments.add(new OrdersFragment());
            mFragments.add(new SummaryDayFragment());
           // mFragments.add(new PreimpresoFragment().setChangeListener(this));
           // mFragments.add(new MistakeFragment());
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

      /*  @Override
        public void onChange(Fragment fragment) {
            notifyDataSetChanged();
        }
*/
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {


            if (position == 0) {
                return "Clientes";
            }else if(position == 1){
                return"Pedidos";

            }else{
                return"Resumen";
            }
        }
    }