package com.greener;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StoreViewAdapter extends RecyclerView.Adapter<StoreViewAdapter.StoreViewHolder> {

    private ArrayList<StoreList> arrayList;
    private Context context;

    public StoreViewAdapter(ArrayList<StoreList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    // ViewHolder 생성
    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_list, parent, false);
        StoreViewHolder holder = new StoreViewHolder(view);

        return holder;
    }

    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Log.d("STORE VALUE ",arrayList.get(position).getImageUri());
        Glide.with(holder.itemView)
                .load(Uri.parse(arrayList.get(position).getImageUri()))
                .into(holder.store_image);
        holder.store_name.setText(arrayList.get(position).getNameStr());
        holder.store_address.setText(arrayList.get(position).getAddressStr());

    }

    // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0); // RecyclerView의 size return
    }

    public class StoreViewHolder extends RecyclerView.ViewHolder {
        ImageView store_image;
        TextView store_name;
        TextView store_address;

        public StoreViewHolder (@NonNull View itemView) {
            super(itemView);

            this.store_image = itemView.findViewById(R.id.store_image);
            this.store_name = itemView.findViewById(R.id.store_name);
            this.store_address = itemView.findViewById(R.id.store_address);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    Intent intent = new Intent(context, StoreDetailView.class);
                    intent.putExtra("Name", arrayList.get(position).getNameStr());
                    intent.putExtra("TelNum", arrayList.get(position).getCallStr());
                    intent.putExtra("Address", arrayList.get(position).getAddressStr());

                    MainActivity.saveX = arrayList.get(position).getX();
                    MainActivity.saveY = arrayList.get(position).getY();

                    context.startActivity(intent);

                }
            });
        }
    }
}
