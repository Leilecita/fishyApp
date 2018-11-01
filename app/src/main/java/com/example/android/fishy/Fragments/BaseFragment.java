package com.example.android.fishy.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.fishy.Interfaces.OnFloatingButton;
import com.example.android.fishy.Interfaces.Refreshable;
import com.example.android.fishy.Interfaces.Searcheable;
import com.example.android.fishy.R;


public class BaseFragment extends Fragment implements Refreshable,OnFloatingButton {

    public void onRefresh(){
    }

    public void onClickButton(){}


    public int getIconButton(){
        return R.drawable.add_order;
    }

    public int getVisibility(){return 0;}

}


