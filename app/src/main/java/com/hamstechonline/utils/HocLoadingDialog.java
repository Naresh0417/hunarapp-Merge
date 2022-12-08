package com.hamstechonline.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hamstechonline.R;

public class HocLoadingDialog {

    Context context;
    Dialog dialog;
    ImageView imageView;

    public HocLoadingDialog(Context context){
        this.context = context;
    }


    public void showLoadingDialog() {

        dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(true);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.loading_progress);
        imageView = dialog.findViewById(R.id.progressBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Glide.with(context)
                .asGif()
                .load(R.drawable.splash_loading_gif)
                .into(imageView);

        dialog.show();

    }

    public void hideDialog(){
        dialog.dismiss();
    }

    public String getNsdc(String isNsdc){
        try {
            if (isNsdc.equals("1")) {
                return "NSDC";
            } else {
                return "Non-NSDC";
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "Wrong Input";
    }
    public String getPayment(String payment){
        try {
            if (payment.equals("0")) {
                return "full";
            } else {
                return "part";
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "Wrong Input";
    }

}
