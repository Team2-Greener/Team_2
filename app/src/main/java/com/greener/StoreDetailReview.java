package com.greener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class StoreDetailReview extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase database, likedDatabase, saveDatabase;
    private DatabaseReference databaseReference, likedDatabaseReference, savedatabaseReference;
    private ViewPager2 Pager;
    private FragmentStateAdapter pagerAdapter;
    private int page_num = 0;
    private CircleIndicator3 Indicator;

    private Button Back, Review;
    private static Button Save;

    private String Name, Tel, Add;
    private String Image;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_detail_view);

        Back = findViewById(R.id.btn_go_back);
        Review = findViewById(R.id.store_detail_review);
        Save = findViewById(R.id.btn_to_save);

        Back.setOnClickListener(this);
        Review.setOnClickListener(this);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Save.isSelected() == true) {
                    Save.setSelected(false);

                    likedDatabaseReference = likedDatabase.getInstance().getReference();

                    likedDatabaseReference.child("user").child(MainActivity.uid).child("저장").child(Name).removeValue();
                } else if (Save.isSelected() == false) {
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

        Name = StoreDetailName.getText().toString();
        Tel = StoreDetailTelNum.getText().toString();
        Add = StoreDetailAddress.getText().toString();

        Pager = findViewById(R.id.store_ViewPager);

        pagerAdapter = new StoreDetailAdapter(this, page_num);
        Pager.setAdapter(pagerAdapter);

        Indicator = findViewById(R.id.indicator);
        Indicator.setViewPager(Pager);
        Indicator.createIndicators(page_num, 0);

        Pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        Pager.setCurrentItem(1000); //시작 지점
        Pager.setOffscreenPageLimit(4); //최대 이미지 수

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("상세정보").child(Name);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                Save.setSelected(false);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    StoreDetailList detailList = snapshot.getValue(StoreDetailList.class); // 만들어뒀던 User 객체에 데이터를 담는다.

                    String imageUrl = detailList.getImageUri();
                    num = detailList.getNum();

                    if (num == 1) {
                        Image = imageUrl;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("TestActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        String path = "user/" + MainActivity.uid + "/저장";

        saveDatabase = FirebaseDatabase.getInstance();
        savedatabaseReference = saveDatabase.getReference();
        Query query = savedatabaseReference.orderByChild(path).equalTo(Name);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Save.setSelected(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("TestActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        Pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    Pager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Indicator.animatePageSelected(position % page_num);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        int id = view.getId();

        if (id == R.id.btn_go_back) {
            int fragnum = MainActivity.fragNum;

            ((MainActivity) MainActivity.context_main).setFragment(fragnum);
            finish();
        }
        else if (id == R.id.store_detail_review) {
            intent = new Intent(this, StoreDetailReview.class);
            this.startActivity(intent);
        }

    }

}