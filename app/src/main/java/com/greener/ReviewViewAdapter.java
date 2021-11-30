package com.greener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewViewAdapter extends RecyclerView.Adapter<ReviewViewAdapter.ReviewViewHolder> {
    private ArrayList<ReviewList> arrayList;
    private Context context;

    public ReviewViewAdapter(ArrayList<ReviewList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list, parent, false);
        ReviewViewHolder holder = new ReviewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.username.setText(arrayList.get(position).getUsername());
        holder.text.setText(arrayList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0); // RecyclerView의 size return
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView text;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            this.username=itemView.findViewById(R.id.show_review_username);
            this.text=itemView.findViewById(R.id.show_review_text);

        }
    }
}
