package com.hamstechonline.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.hamstechonline.R;
import com.hamstechonline.activities.HomePageActivity;
import com.hamstechonline.activities.MiniLessonsEnrolNowActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadAssignmentPopup {

    Context context;
    Dialog dialog;
    private final int PERM_READ_WRITE_STORAGE = 101;
    private static final int FILE_SELECT_CODE = 0;

    public UploadAssignmentPopup(Context context){
        this.context = context;
    }


    public void showLoadingDialog() {

        dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(true);
        //...that's the layout i told you will inflate later
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setContentView(R.layout.upload_assignment_popup);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        ImageView btnSubmit = dialog.findViewById(R.id.btnSubmit);

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissions()) {
                    Toast.makeText(context,"Please provide permissions to move ahead.", Toast.LENGTH_SHORT);
                    return;
                } else {
                    //uploadSuccessPopUp();
                    showFileChooser();
                }
            }
        });

    }

    public void hideDialog(){
        dialog.dismiss();
    }

    private  boolean checkPermissions() {
        int result;

        String[] permissions= new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(context,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            //ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERM_READ_WRITE_STORAGE);
            return false;
        }

        return true;
    }

    private void showFileChooser() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            //startActivityForResult(i, FILE_SELECT_CODE);
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }

    }


}
