package com.leila.android.fishy;

import android.view.View;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Created by leila on 21/11/17.
 */

public class CustomOnItemSelectedListener implements OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {


        if(parent.getItemAtPosition(pos).toString().equals("Borrar")){

        }
       /* Toast.makeText(parent.getContext(),
                "Preimpeso seleccionado : \n" + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
