package com.greener;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MediaViewAdapter extends RecyclerView.Adapter<MediaViewAdapter.MediaViewHolder> {

    private ArrayList<MediaList> arrayList;
    private Context context;

    public MediaViewAdapter(ArrayList<MediaList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    // ViewHolder 생성
    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_list, parent, false);
        MediaViewHolder holder = new MediaViewHolder(view);

        return holder;
    }

    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(Uri.parse(arrayList.get(position).getImageUri()))
                .into(holder.media_image);
        holder.media_title.setText(arrayList.get(position).getTitleStr());
    }

    // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0); // RecyclerView의 size return
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder {
        ImageView media_image;
        TextView media_title;

        public MediaViewHolder (@NonNull View itemView) {
            super(itemView);

            this.media_image = itemView.findViewById(R.id.media_image);
            this.media_title = itemView.findViewById(R.id.media_title);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    Intent intent = new Intent(context, MediaDetailView.class);
                    intent.putExtra("Title", arrayList.get(position).getTitleStr());

                    MediaDetailView.Path = arrayList.get(position).getImageTitle();

                    context.startActivity(intent);

                }
            });
        }

    }

}
