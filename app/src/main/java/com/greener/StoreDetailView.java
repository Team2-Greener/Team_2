package com.greener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class StoreDetailView extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<String> arrayList;
    private ArrayList<StoreList> storeLists;
    private FirebaseDatabase database, likedDatabase, saveDatabase, imageDatabase;
    private DatabaseReference databaseReference, likedDatabaseReference, savedatabaseReference, imageReference;
    private RecyclerView.Adapter adapter;
    private ViewPager2 sliderViewPager;

    private Button Back, Review;
    private static Button Save;

    private String Name, Tel, Add;
    private String Image, path;
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

                    savedatabaseReference = saveDatabase.getInstance().getReference();
                    savedatabaseReference.child(path).child("save").removeValue();

                } else if (Save.isSelected() == false) {
                    Save.setSelected(true);

                    StoreList liked = new StoreList(Add, Tel, Image, Name);

                    likedDatabaseReference = likedDatabase.getInstance().getReference();

                    likedDatabaseReference.child("user").child(MainActivity.uid).child("저장").child(Name).setValue(liked);

                    savedatabaseReference = saveDatabase.getInstance().getReference();
                    savedatabaseReference.child(path).child("save").setValue("true");
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

        path = "user/" + MainActivity.uid + "/정보/" + Name;

        databaseReference = database.getInstance().getReference();
        databaseReference.child(path).child("save");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                if(dataSnapshot.exists()) {
                    Save.setSelected(true);
                }
                else {
                    Save.setSelected(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("TestActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        adapter = new StoreDetailAdapter(arrayList, this);

        imageReference = imageDatabase.getInstance().getReference();

        imageReference.child("상세정보");
        imageReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    StoreDetailList detailList = snapshot.getValue(StoreDetailList.class);

                    String imageUri = detailList.getImageUri();
                    num = detailList.getNum();

                    if (num == 1) {
                        Image = imageUri;
                    }
                    arrayList.add(imageUri); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
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

        sliderViewPager.setOffscreenPageLimit(1);
        sliderViewPager.setAdapter(adapter);

        sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        int id = view.getId();

        if(id == R.id.btn_go_back) {
            int fragnum = MainActivity.fragNum;

            ((MainActivity)MainActivity.context_main).setFragment(fragnum);
            finish();
        }
        else if(id == R.id.store_detail_review) {
            intent = new Intent(this, StoreDetailReview.class);
            this.startActivity(intent);

            finish();
        }
    }
}