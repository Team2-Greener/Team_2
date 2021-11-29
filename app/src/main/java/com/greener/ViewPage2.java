package com.greener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class ViewPage2 extends Fragment {

    private View view;
    private ImageView Image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_2page, container, false);

        Image = (ImageView)view.findViewById(R.id.page_image2);

        //개인정보 DB에서 받아오기
        Glide.with(getContext())
                .load(StoreDetailView.pageImage2)
                .into(Image);

        return view;
    }

}
