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