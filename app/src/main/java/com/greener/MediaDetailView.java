package com.greener;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MediaDetailView extends AppCompatActivity {

    private ArrayList<StoreList> arrayList = new ArrayList<StoreList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_detail_view);

        //TextView MediaDetailTitle = findViewById(R.id.media_detail_title);
        //TextView MediaDetailContent = findViewById(R.id.media_detail_content);
        //ImageView MediaDetailImage = findViewById(R.id.media_detail_image);

        Intent intent = getIntent();

        //MediaDetailTitle.setText(intent.getStringExtra("Path"));
        //MediaDetailContent.setText(intent.getStringExtra("Title"));


    }
}