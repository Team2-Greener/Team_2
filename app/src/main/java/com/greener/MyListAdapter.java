package com.greener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter {
    Context context;
    ArrayList<ListViewItem> list_itemArrayList;

    ViewHolder viewholder;

    public MyListAdapter(Context context, ArrayList<ListViewItem> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }

    class ViewHolder{
        ImageView image;
        TextView title;
        TextView content;
    }


    @Override
    public int getCount() { return this.list_itemArrayList.size(); }

    @Override
    public Object getItem(int position) { return list_itemArrayList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_store_info,null);

            viewholder = new ViewHolder();

            viewholder.image = convertView.findViewById(R.id.image);
            viewholder.title = convertView.findViewById(R.id.title);
            viewholder.content = convertView.findViewById(R.id.content);

            convertView.setTag(viewholder);
        }else{
            viewholder = (ViewHolder)convertView.getTag();
        }
        Glide.with(context).load(list_itemArrayList.get(position).getImage()).into(viewholder.image);
        viewholder.title.setText(list_itemArrayList.get(position).getTitle());
        viewholder.content.setText(list_itemArrayList.get(position).getContent());

        return convertView;
    }
}

