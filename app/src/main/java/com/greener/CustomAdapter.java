package com.greener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener {

    private Context context;
    private List list;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
    }

    class ViewHolder {
        public TextView tv_title;
        public TextView tv_text;
        public ImageView iv_image;
    }

    public CustomAdapter(Context context, ArrayList list){
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.layout_store_info, parent, false);
        }

        viewHolder = new ViewHolder();
        viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.image);
        viewHolder.tv_title = (TextView) convertView.findViewById(R.id.title);
        viewHolder.tv_text = (TextView) convertView.findViewById(R.id.text);

        final com.greener.HotelActivity.Hotel hotel = (HotelActivity.Hotel) list.get(position);
        viewHolder.tv_title.setText(hotel.getTitle());
        viewHolder.tv_text.setText(hotel.getText());
        Glide
                .with(context)
                .load(hotel.getImage_uri())
                .centerCrop()
                .apply(new RequestOptions().override(250, 350))
                .into(viewHolder.iv_image);
        viewHolder.tv_title.setTag(hotel.getTitle());


//        //아이템 클릭 방법2 - 클릭시 아이템 반전 효과가 안 먹힘
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, " " + actor.getName(), Toast.LENGTH_SHORT).show();
//            }
//        });

        //Return the completed view to render on screen
        return convertView;
    }
}