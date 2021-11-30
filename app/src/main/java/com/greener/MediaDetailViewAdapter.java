package com.greener;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MediaDetailViewAdapter extends RecyclerView.Adapter<MediaDetailViewAdapter.MediaViewHolder> {

    private ArrayList<MediaDetailList> arrayList;
    private Context context;

    public MediaDetailViewAdapter(ArrayList<MediaDetailList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    // ViewHolder 생성
    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_detail_list, parent, false);
        MediaViewHolder holder = new MediaViewHolder(view);

        return holder;
    }

    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(Uri.parse(arrayList.get(position).getImageUri()))
                .into(holder.media_detail_image);
    }

    // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0); // RecyclerView의 size return
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder {
        ImageView media_detail_image;

        public MediaViewHolder (@NonNull View itemView) {
            super(itemView);

            this.media_detail_image = itemView.findViewById(R.id.media_detail_image);
        }

    }

}
