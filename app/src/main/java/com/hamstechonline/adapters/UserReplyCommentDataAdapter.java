package com.hamstechonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamstechonline.R;
import com.hamstechonline.datamodel.Reply;

import java.util.List;


public class UserReplyCommentDataAdapter extends RecyclerView.Adapter<UserReplyCommentDataAdapter.ViewHolder>{
    Context context;
    List<Reply> replies;

    public UserReplyCommentDataAdapter (Context context,List<Reply> replies) {
        this.context = context;
        this.replies = replies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.buzz_comments_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.txtUserName.setText(replies.get(position).getName());
            holder.txtComment.setText(replies.get(position).getComment());
            //holder.imgProfile.setText(replies.get(position).getProfilepic());

            holder.imgProfile.setVisibility(View.GONE);

                /*if (!dataBuzz.get(position).getProfile_pic().equalsIgnoreCase("")) {
                    Glide.with(context)
                            .load(dataBuzz.get(position).getProfile_pic())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.ic_launcher)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(holder.imgProfile);
                }*/

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtComment, imgProfile,txtCommentReply,txtPost;
        RelativeLayout inputCommentFields;
        EditText userInputComment;
        RecyclerView listComments;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgProfile = view.findViewById(R.id.imgProfile);
            txtUserName = view.findViewById(R.id.txtUserName);
            txtComment = view.findViewById(R.id.txtComment);
            txtCommentReply = view.findViewById(R.id.txtCommentReply);
            inputCommentFields = view.findViewById(R.id.inputCommentFields);
            txtPost = view.findViewById(R.id.txtPost);
            userInputComment = view.findViewById(R.id.userInputComment);
            listComments = view.findViewById(R.id.listComments);
        }

    }

}
