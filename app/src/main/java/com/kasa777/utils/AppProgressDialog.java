package com.kasa777.utils;

import android.app.Dialog;
import android.view.Window;

import com.kasa777.R;


public class AppProgressDialog {


    public static void show(Dialog mProgressDialog) {
        try {
            if (mProgressDialog.isShowing())
                return;
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mProgressDialog.setContentView(R.layout.google_progres_bar);
            mProgressDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showBankFetching(Dialog mProgressDialog) {
        try {
            if (mProgressDialog.isShowing())
                return;
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mProgressDialog.setContentView(R.layout.fetch_bank_dialog);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hide(Dialog mProgressDialog) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}