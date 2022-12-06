package com.hamstechonline.activities.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hamstechonline.R;
import com.hamstechonline.activities.HomePageActivity;
import com.hamstechonline.datamodel.SaveCommentReport;
import com.hamstechonline.datamodel.SavePostReport;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentReportDialoge {
    Context context;
    Dialog dialog;
    int comment_id;
    List<String> hocOptions = new ArrayList<>();
    HocOptionsAdapter hocOptionsAdapter;
    RecyclerView hocItemsList;
    String mobile;
    ApiInterface apiService;

    public CommentReportDialoge(Context context, int comment_id, String mobile){
        this.context = context;
        this.comment_id = comment_id;
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

        dialog.setContentView(R.layout.report_layout);

        hocItemsList = dialog.findViewById(R.id.hocItemsList);
        //dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        hocOptions.add("It's spam");
        hocOptions.add("Sale of illegal or regulated goods");
        hocOptions.add("Hate speech or symbols");
        hocOptions.add("False information");
        hocOptions.add("Nudity or Sexual activity");
        hocOptionsAdapter = new HocOptionsAdapter(context,hocOptions);
        hocItemsList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        hocItemsList.setAdapter(hocOptionsAdapter);
        dialog.show();
    }

    public class HocOptionsAdapter extends RecyclerView.Adapter<HocOptionsAdapter.ViewHolder> {

        Context context;
        List<String> hocOptions;

        public HocOptionsAdapter(Context context, List<String> hocOptions) {
            this.context = context;
            this.hocOptions = hocOptions;
        }

        @NonNull
        @Override
        public HocOptionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.report_item_list, parent, false);
            HocOptionsAdapter.ViewHolder vh = new HocOptionsAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final HocOptionsAdapter.ViewHolder holder, int position) {
            try {
                holder.txtItemName.setText(hocOptions.get(position));

                holder.txtItemName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getReportApi(comment_id,position);
                        dialog.dismiss();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return hocOptions.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtItemName;

            public ViewHolder(@NonNull View view) {
                super(view);
                txtItemName = view.findViewById(R.id.txtItemName);
            }

        }
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
