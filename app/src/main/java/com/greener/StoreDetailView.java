package com.greener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;

import java.util.ArrayList;

public class StoreDetailView extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<String> arrayList;
    private FirebaseDatabase database, likedDatabase;
    private DatabaseReference databaseReference, likedDatabaseReference;
    private RecyclerView.Adapter adapter;
    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    private ShopMain shop_main;
    private HotelMain hotel_main;
    private LikedMain liked_main;

    private Button Back, Review;

    private String Name, Tel, Add;
    private String Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_detail_view);

        Button Back = findViewById(R.id.btn_go_back);
        Button Review = findViewById(R.id.store_detail_review);
        Button Save = findViewById(R.id.btn_to_save);

        Back.setOnClickListener(this);
        Review.setOnClickListener(this);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Save.isSelected() == true) {
                    Save.setSelected(false);

                    likedDatabaseReference = likedDatabase.getInstance().getReference();

                    likedDatabaseReference.child("user").child(MainActivity.uid).child("저장").child(Name).removeValue();
                }
                else {
                    Save.setSelected(true);

                    StoreList liked = new StoreList(Add, Tel, Image, Name);

                    likedDatabaseReference = likedDatabase.getInstance().getReference();

                    likedDatabaseReference.child("user").child(MainActivity.uid).child("저장").child(Name).setValue(liked);
                }
            }
        });

        TextView StoreDetailName = findViewById(R.id.store_detail_name);
        TextView StoreDetailTelNum = findViewById(R.id.store_detail_telNum);
        TextView StoreDetailAddress = findViewById(R.id.store_detail_address);

        Intent intent = getIntent();

        StoreDetailName.setText(intent.getStringExtra("Name"));
        StoreDetailTelNum.setText(intent.getStringExtra("TelNum"));
        StoreDetailAddress.setText(intent.getStringExtra("Address"));

        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        Name = StoreDetailName.getText().toString();
        Tel = StoreDetailTelNum.getText().toString();
        Add = StoreDetailAddress.getText().toString();

        adapter = new StoreDetailAdapter(arrayList, this);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("상세정보").child(Name);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    StoreDetailList detailList = snapshot.getValue(StoreDetailList.class); // 만들어뒀던 User 객체에 데이터를 담는다.

                    String imageUrl = detailList.getImageUri();
                    int num = detailList.getNum();

                    if(num == 1) {
                       Image = imageUrl;
                    }
                    arrayList.add(imageUrl); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("TestActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        sliderViewPager = findViewById(R.id.store_ViewPager);
        layoutIndicator = findViewById(R.id.layoutIndicators);

        sliderViewPager.setOffscreenPageLimit(1);
        sliderViewPager.setAdapter(adapter);

        sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });

        setupIndicators(arrayList.size());

        shop_main = new ShopMain();
        hotel_main = new HotelMain();
        liked_main = new LikedMain();
    }

    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.indicator_inactive));
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.indicator_inactive
                ));
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        int id = view.getId();

        if(id == R.id.btn_go_back) {
            setFragment(MainActivity.fragNum);
        }
        else if(id == R.id.store_detail_review) {
            intent = new Intent(this, StoreDetailReview.class);
            this.startActivity(intent);
        }
    }

    private void setFragment(int n){
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        switch(n){
            case 0:
                transaction.replace(R.id.store_detail_scroll, shop_main);    //shop_main으로 이동
                transaction.commit();   //상태 save
                break;
            case 1:
                transaction.replace(R.id.main_frame, hotel_main);   //hotel_main으로 이동
                transaction.commit();
                break;
            case 2:
                transaction.replace(R.id.main_frame, liked_main);   //liked_main으로 이동
                transaction.commit();
                break;
        }
    }
}