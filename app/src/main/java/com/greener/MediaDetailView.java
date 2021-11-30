package com.greener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MediaDetailView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MediaDetailList> arrayList = new ArrayList<MediaDetailList>();
    private RecyclerView.Adapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private String Title;
    public static String Path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_detail_view);

        TextView MediaDetailTitle = findViewById(R.id.media_detail_title);

        Intent intent = getIntent();

        MediaDetailTitle.setText(intent.getStringExtra("Title"));

        recyclerView = (RecyclerView)findViewById(R.id.media_detail_recyclerView);
        recyclerView.setHasFixedSize(true);     //리사이클러뷰 성능 강화

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<MediaDetailList>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        Title = MediaDetailTitle.getText().toString();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("환경정보").child(Path);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    MediaDetailList detailList = snapshot.getValue(MediaDetailList.class); // 만들어뒀던 User 객체에 데이터를 담는다.

                    arrayList.add(detailList); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("TestActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        adapter = new MediaDetailViewAdapter(arrayList, this);
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
    }

}