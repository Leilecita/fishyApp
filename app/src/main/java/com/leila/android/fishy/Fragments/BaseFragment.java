package com.leila.android.fishy.Fragments;

import android.support.v4.app.Fragment;

import com.leila.android.fishy.Interfaces.OnFloatingButton;
import com.leila.android.fishy.Interfaces.Refreshable;
import com.leila.android.fishy.R;


public class BaseFragment extends Fragment implements Refreshable,OnFloatingButton {

    public void onRefresh(){
    }

    public void onClickButton(){}


    public int getIconButton(){
        return R.drawable.add_order;
    }

    public int getVisibility(){return 0;}

}


