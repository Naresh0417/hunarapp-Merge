package com.hamstechonline.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hamstechonline.R;

public class FaqsAdapter extends RecyclerView.Adapter<FaqsAdapter.ViewHolder> {

    Context context;
    String[] questions;
    String[] answers;

    public FaqsAdapter(Context context,String[] questions,String[] answers){
        this.context=context;
        this.questions = questions;
        this.answers = answers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.faqs_item_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {
            holder.txtTitle.setText(questions[position]);
            holder.txtDescription.setText(answers[position]);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return questions.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,txtDescription;
        public ViewHolder(@NonNull View view) {
            super(view);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtDescription = view.findViewById(R.id.txtDescription);
        }
    }
}
