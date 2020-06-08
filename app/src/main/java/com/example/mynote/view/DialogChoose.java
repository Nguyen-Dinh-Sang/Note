package com.example.mynote.view;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;

import com.example.mynote.R;

import java.util.List;

public class DialogChoose {
    private static DialogChoose instance;

    public static DialogChoose getInstance() {
        if(instance==null)
            instance = new DialogChoose();
        return instance;
    }
    public void showDialog(List<String> options, String title , final Context context , final DialogChooseListener dialogChooseListener){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle(title);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.addAll(options);
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialogChooseListener.onDismiss();
            }
        });
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogChooseListener.onItemSelected(arrayAdapter.getItem(which));
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }
    public interface DialogChooseListener{
        void onItemSelected(String item);
        void onDismiss();
    }
}
