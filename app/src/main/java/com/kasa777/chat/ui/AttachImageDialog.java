package com.kasa777.chat.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.kasa777.R;

public class AttachImageDialog {


    public static Dialog dialog;

    public static Dialog getDialog() {
        return dialog;
    }

    public static void ImageChooserDialog(Context context, String message, OnChooseImageSource onClickListener) {
        if (dialog != null) {
            dialog.dismiss();
        }

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_image_picker);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        LinearLayout llPickCamera = (LinearLayout) dialog.findViewById(R.id.tabCamera);
        LinearLayout llPickGallery = (LinearLayout) dialog.findViewById(R.id.tabGallery);


        llPickCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onCamera();
                dialog.dismiss();
            }
        });
        llPickGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onGAllery();
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    public interface OnChooseImageSource {
        public void onCamera();

        public void onGAllery();
    }


}