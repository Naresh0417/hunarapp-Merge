package com.hamstechonline.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hamstechonline.R;

import java.util.Arrays;
import java.util.List;

public class CentersAdapter extends RecyclerView.Adapter<CentersAdapter.ViewHolder> {

    Context context;
    List branchNames;
    List branchAddress;
    int[] locationMaps = {R.drawable.hyd_map,R.drawable.vizag_map,R.drawable.indore_map};
    String[] branchPhone = {"9010100240","9010100240","9010100240"};
    double[] lat = {17.4278036,17.725479,22.745844};
    double[] longt = {78.4523621,83.316673,75.893098};
    float latitude,longitude;

    public CentersAdapter(Context context){
        this.context=context;

        branchNames = Arrays.asList(context.getResources().getStringArray(R.array.branchNames));
        branchAddress = Arrays.asList(context.getResources().getStringArray(R.array.branchAddress));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.centers_adapters, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        try {
            holder.txtBranchName.setText(branchNames.get(position).toString());
            holder.txtCenterAddress.setText(branchAddress.get(position).toString());
            holder.txtPhone.setPaintFlags(holder.txtPhone.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            //holder.txtPhone.setText("Phone: "+Html.fromHtml("<u>"+branchPhone[position]+"</u>"));
            holder.txtPhone.setText(context.getResources().getString(R.string.lblPhone)+": "+branchPhone[position]);
            Glide.with(context)

                    .load(locationMaps[position])
                    //.placeholder(R.drawable.duser1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.imgLocation);
            holder.txtPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+context.getResources().getString(R.string.lblPhone1)));
                    context.startActivity(intent);
                }
            });
            holder.imgLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    latitude = (float) lat[position];
                    longitude = (float) longt[position];

                    String map = "http://maps.google.co.in/maps?q="+latitude+","+longitude+"("+"Hamstech Online Services Pvt Ltd"+")";

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                    context.startActivity(intent);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        //return branchNames.size();
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLocation;
        TextView txtPhone,txtBranchName,txtCenterAddress;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgLocation = view.findViewById(R.id.imgLocation);
            txtPhone = view.findViewById(R.id.txtPhone);
            txtBranchName = view.findViewById(R.id.txtBranchName);
            txtCenterAddress = view.findViewById(R.id.txtCenterAddress);
        }
    }
}
