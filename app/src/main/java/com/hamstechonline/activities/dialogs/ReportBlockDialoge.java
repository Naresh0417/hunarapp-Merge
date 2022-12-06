package com.hamstechonline.activities.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hamstechonline.R;
import com.hamstechonline.activities.BuzzActivity;
import com.hamstechonline.datamodel.SavePostReport;
import com.hamstechonline.datamodel.UserBlockReport;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportBlockDialoge {
    Context context;
    Dialog dialog;
    int Postid;
    List<String> hocOptions = new ArrayList<>();
    RecyclerView hocItemsList;
    String mobile;
    ApiInterface apiService;
    ReportDialoge reportDialoge;

    public ReportBlockDialoge(Context context, int Postid, String mobile){
        this.context = context;
        this.Postid = Postid;
        this.mobile = mobile;
    }

    public void showLoadingDialog() {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.report_block_layout);

        TextView txtReportTitle = dialog.findViewById(R.id.txtReportTitle);
        TextView txtDescription = dialog.findViewById(R.id.txtDescription);

        hocItemsList = dialog.findViewById(R.id.hocItemsList);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        txtReportTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                reportDialoge = new ReportDialoge(context,Postid,mobile);
                reportDialoge.showLoadingDialog();
            }
        });

        txtDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getBlockApi(Postid);
            }
        });

        dialog.show();
    }

    public void getReportApi(int postId,int position) {
        SavePostReport savePostReport = new SavePostReport("Hamstech", context.getResources().getString(R.string.lblApiKey),
                postId,hocOptions.get(position),mobile);

        Call<SavePostReport> call = apiService.getSavePostReport(savePostReport);
        call.enqueue(new Callback<SavePostReport>() {
            @Override
            public void onResponse(Call<SavePostReport> call, Response<SavePostReport> response) {
                OnlineSuccessfulPopUp(context);
            }

            @Override
            public void onFailure(Call<SavePostReport> call, Throwable t) {

            }
        });
    }

    public void getBlockApi(int postId) {
        UserBlockReport savePostReport = new UserBlockReport("Hamstech", context.getResources().getString(R.string.lblApiKey),
                postId,mobile);

        Call<UserBlockReport> call = apiService.getBlockUser(savePostReport);
        call.enqueue(new Callback<UserBlockReport>() {
            @Override
            public void onResponse(Call<UserBlockReport> call, Response<UserBlockReport> response) {
                OnlineSuccessfulPopUp(context);
            }

            @Override
            public void onFailure(Call<UserBlockReport> call, Throwable t) {

            }
        });
    }

    public void OnlineSuccessfulPopUp(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.report_success_dialoge);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
