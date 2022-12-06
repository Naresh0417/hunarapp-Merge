package com.hamstechonline.activities.dialogs;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import com.hamstechonline.datamodel.SaveCommentReport;
import com.hamstechonline.datamodel.UserBlockReport;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentBlockReportDialoge {
    Context context;
    Dialog dialog;
    int comment_id;
    List<String> hocOptions = new ArrayList<>();
    RecyclerView hocItemsList;
    String mobile;
    ApiInterface apiService;
    CommentReportDialoge reportDialoge;
    String comment;

    public CommentBlockReportDialoge(Context context, int comment_id, String mobile,String comment){
        this.context = context;
        this.comment_id = comment_id;
        this.mobile = mobile;
        this.comment = comment;
    }

    public void showLoadingDialog() {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.comment_report_list);

        TextView txtReportTitle = dialog.findViewById(R.id.txtReportTitle);
        TextView txtDescription = dialog.findViewById(R.id.txtDescription);
        TextView txtReportComment = dialog.findViewById(R.id.txtReportComment);
        TextView txtBlockUser = dialog.findViewById(R.id.txtBlockUser);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        txtReportTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", comment);
                clipboard.setPrimaryClip(clip);
            }
        });

        txtReportComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                reportDialoge = new CommentReportDialoge(context,comment_id,mobile);
                reportDialoge.showLoadingDialog();
            }
        });

        txtBlockUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getBlockApi(comment_id);
            }
        });

        dialog.show();
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

    public void getReportApi(int postId,int position) {
        SaveCommentReport savePostReport = new SaveCommentReport("Hamstech", context.getResources().getString(R.string.lblApiKey),
                postId,hocOptions.get(position),mobile);

        Call<SaveCommentReport> call = apiService.getCommentPostReport(savePostReport);
        call.enqueue(new Callback<SaveCommentReport>() {
            @Override
            public void onResponse(Call<SaveCommentReport> call, Response<SaveCommentReport> response) {
                OnlineSuccessfulPopUp(context);
            }

            @Override
            public void onFailure(Call<SaveCommentReport> call, Throwable t) {

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
