package com.greener;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class StoreDetailAdapter extends RecyclerView.Adapter<StoreDetailAdapter.ViewHolder> {

    private ArrayList<String> arrayList;
    private Context context;

    public StoreDetailAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    // ViewHolder 생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_detail_list, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position))
                .into(holder.store_detail_image);
    }

    // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
    @Override
    public int getItemCount() {
        return arrayList.size(); // RecyclerView의 size return
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView store_detail_image;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);

            this.store_detail_image = itemView.findViewById(R.id.store_detail_image);
        }
    }
}
