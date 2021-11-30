package com.greener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;

public class SearchMain extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private RecyclerView.Adapter adapter;
    private ArrayList<StoreList> arrayList = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String searchStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);

        Intent searchIntent = getIntent();
        searchStr = searchIntent.getStringExtra("검색 내용");

        // System.out.println("검색 내용 : "+ searchStr);

        recyclerView = findViewById(R.id.hotel_recyclerView); // 아디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();
        // arrayList.clear();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("샵");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot shoplist : snapshot.getChildren()){
                    StoreList ShopList = shoplist.getValue(StoreList.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    if(ShopList.getNameStr().contains(searchStr)) {
                        System.out.println(ShopList.getNameStr());

                        arrayList.add(ShopList);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TestActivity", String.valueOf(error.toException())); // 에러문 출력
            }
        });

        databaseReference = database.getReference("호텔");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot hotellist : snapshot.getChildren()){
                    StoreList HotelList = hotellist.getValue(StoreList.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    if(HotelList.getNameStr().contains(searchStr)) {
                        System.out.println(HotelList.getNameStr());

                        arrayList.add(HotelList);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TestActivity", String.valueOf(error.toException())); // 에러문 출력
            }
        });

        for(int i = 0; i < arrayList.size(); i++){
            System.out.println("arraylist : "+arrayList.get(i).getNameStr());
        }

        adapter = new StoreViewAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);
    }
}