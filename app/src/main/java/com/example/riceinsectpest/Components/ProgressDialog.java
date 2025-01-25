package com.example.riceinsectpest.Components;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.riceinsectpest.R;

class ProgressDialog {
    Activity activity;
    AlertDialog alertDialog;

    ProgressDialog(Activity myActivity){
        activity = myActivity;
    }

    void startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_dialog,null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    void dismissDialog(){
        alertDialog.dismiss();
    }
}
