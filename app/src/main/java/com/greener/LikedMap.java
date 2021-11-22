package com.greener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LikedMap extends Fragment {

    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ListViewItem> arrayList = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Liked Map changed");

        view = inflater.inflate(R.layout.liked_map, container, false);

        recyclerView = view.findViewById(R.id.recyclerView); // 아디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        adapter = new CustomAdapter(arrayList, getActivity());


            //layoutManager = new LinearLayoutManager(this);// 한줄
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);//두줄
            recyclerView.setLayoutManager(gridLayoutManager);
            arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

            database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

            databaseReference = database.getReference("호텔"); // DB 테이블 연결
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                    arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                        ListViewItem user = snapshot.getValue(ListViewItem.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                        arrayList.add(user); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                    }
                    adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 디비를 가져오던중 에러 발생 시
                    Log.e("TestActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                }
            });

            recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

        return view;
    }

}