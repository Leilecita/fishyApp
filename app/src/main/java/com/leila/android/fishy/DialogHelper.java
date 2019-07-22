package com.leila.android.fishy;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by leila on 2/7/18.
 */

public class DialogHelper {
    private static DialogHelper INSTANCE = new DialogHelper();

    private DialogHelper(){

    }

    public static DialogHelper get(){
        return INSTANCE;
    }

    public void showMessage(String titleState,String mensaje,Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_helper, null);
        builder.setView(dialogView);

        TextView mens= dialogView.findViewById(R.id.mensaje);
        mens.setText(mensaje);

        TextView title= dialogView.findViewById(R.id.titleState);
        title.setText(titleState);

        final TextView ok= dialogView.findViewById(R.id.ok);

        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
