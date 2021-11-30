package com.greener;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StoreDetailView extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private MapView mapView;
    private NaverMap naverMap;
    private OverlayImage image = OverlayImage.fromResource(R.drawable.ic_place_marker);

    private ArrayList<String> arrayList;
    private ArrayList<ReviewList> arrayList_show_review;
    private FirebaseDatabase database, likedDatabase, saveDatabase;
    private DatabaseReference databaseReference, likedReference, databaseReference_show_review, saveReference;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();;
    private RecyclerView.Adapter adapter, adapter_show_review;
    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;
    private RecyclerView recyclerView_show_review;
    private RecyclerView.LayoutManager layoutManager, layoutManager_show_review;

    private Button Back, Review, Map, write_reivew;
    private FrameLayout mapFrame,reviewFrame;
    private static Button Save;

    private String Name, Tel, Add, X, Y;
    private String Image, path;
    private Double x, y;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_detail_view);

        Back = findViewById(R.id.btn_go_back);
        Map = findViewById(R.id.store_detail_map);
        Review = findViewById(R.id.store_detail_review);
        Save = findViewById(R.id.btn_to_save);
        write_reivew = findViewById(R.id.btn_write_review);

        mapFrame = findViewById(R.id.map_frame);
        reviewFrame = findViewById(R.id.review_frame);

        Back.setOnClickListener(this);
        Map.setOnClickListener(this);
        Review.setOnClickListener(this);

        X = MainActivity.saveX;
        Y = MainActivity.saveY;
        x = Double.parseDouble(X);
        y = Double.parseDouble(Y);

        // System.out.println("X : "+X+", Y : "+Y);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Save.isSelected() == true) {
                    Save.setSelected(false);

                    likedReference = likedDatabase.getInstance().getReference();

                    likedReference.child(path).removeValue();
                }
                else if(Save.isSelected() == false){
                    Save.setSelected(true);

                    StoreList liked = new StoreList(Add, Tel, Image, Name, X, Y);

                    likedReference = likedDatabase.getInstance().getReference();

                    likedReference.child(path).setValue(liked);
                }
            }
        });

        TextView StoreDetailName = findViewById(R.id.store_detail_name);
        TextView StoreDetailTelNum = findViewById(R.id.store_detail_telNum);
        TextView StoreDetailAddress = findViewById(R.id.store_detail_address);

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

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

        path = "user/" + MainActivity.uid + "/저장/" + Name;

        databaseReference = database.getInstance().getReference().child(path);
        databaseReference.addValueEventListener(new ValueEventListener() {
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

        recyclerView_show_review = findViewById(R.id.review_recyclerview);
        recyclerView_show_review.setHasFixedSize(true);     //리사이클러뷰 성능 강화
        layoutManager_show_review = new LinearLayoutManager(this);
        recyclerView_show_review.setLayoutManager(layoutManager_show_review);

        arrayList_show_review = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        databaseReference_show_review = database.getReference("reviews/"+Name); // DB 테이블 연결
        databaseReference_show_review.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList_show_review.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    ReviewList review = snapshot.getValue(ReviewList.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    arrayList_show_review.add(review); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter_show_review.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("StoreDetailView_review", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
        //Toast.makeText(this,arrayList_show_review.size(),Toast.LENGTH_SHORT).show();
        adapter_show_review = new ReviewViewAdapter(arrayList_show_review, this);
        recyclerView_show_review.setAdapter(adapter_show_review); // 리사이클러뷰에 어댑터 연결

        LocalDateTime now = LocalDateTime.now();
        String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"));
        write_reivew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout linear = (LinearLayout) View.inflate(StoreDetailView.this, R.layout.dialog_review, null);
                new AlertDialog.Builder(StoreDetailView.this) .setView(linear)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                EditText url = (EditText) linear.findViewById(R.id.edit_review);
                                String value = url.getText().toString();
                                ReviewList review = new ReviewList();
                                //review uid 저장
                                review.setUid(firebaseAuth.getUid());
                                //review name 저장
                                databaseReference = database.getReference("users"); // DB 테이블 연결
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                                            UsersList u = snapshot.getValue(UsersList.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                                            if(u.getUid().equals(firebaseAuth.getUid())) {
                                                int size = u.getName().length();
                                                String username = u.getName().substring(0,3);
                                                String result=username.concat("****");
                                                review.setUsername(result);
                                                review.setText(value);
                                                dialog.dismiss();
                                                // database에 저장
                                                String path = "reviews/"+Name+"/"+now.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH:mm:ss"));;
                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path); // DB 테이블 연결
                                                databaseReference.setValue(review);
                                                Toast.makeText(StoreDetailView.this,"리뷰가 저장되었습니다.",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // 디비를 가져오던중 에러 발생 시
                                        Log.e("StoreDetailView_edit", String.valueOf(databaseError.toException())); // 에러문 출력
                                    }
                                });



                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });


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
            onBackPressed();
        }
        else if(id == R.id.store_detail_review) {
            mapFrame.setVisibility(View.GONE);
            reviewFrame.setVisibility(View.VISIBLE);
            /*
            intent = new Intent(this, StoreDetailReview.class);
            this.startActivity(intent);

            finish();

             */
        }
        else if(id == R.id.store_detail_map){
            reviewFrame.setVisibility(View.GONE);
            mapFrame.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        LatLng latlng = new LatLng(x,y);

        CameraPosition cameraPosition = new CameraPosition(latlng, 15);
        naverMap.setCameraPosition(cameraPosition);

        Marker marker = new Marker();
        marker.setPosition(latlng);
        marker.setWidth(100);
        marker.setHeight(100);
        marker.setIcon(image); // 마커 이미지 넣기
        marker.setCaptionText(Name);
        marker.setMap(naverMap);
    }
}