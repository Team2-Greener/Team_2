package com.greener;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    private ShopActivity shop_main;
    private HotelActivity hotel_main;
    private LikedActivity liked_main;
    private MediaActivity media_main;
    private SettingActivity setting_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //타이틀 바 없애기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //화면 전환
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.shop_menu:
                        setFragment(0);
                        break;
                    case R.id.hotel_menu:
                        setFragment(1);
                        break;
                    case R.id.liked_menu:
                        setFragment(2);
                        break;
                    case R.id.media_menu:
                        setFragment(3);
                        break;
                    case R.id.setting_menu:
                        setFragment(4);
                        break;
                }
                return false;
            }
        });
        shop_main = new ShopActivity();
        hotel_main = new HotelActivity();
        liked_main = new LikedActivity();
        media_main = new MediaActivity();
        setting_main = new SettingActivity();
        setFragment(0);
    }

    private void setFragment(int n){
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        switch(n){
            case 0:
                transaction.replace(R.id.main_frame, shop_main);
                transaction.commit();   //save
                break;
            case 1:
                transaction.replace(R.id.main_frame, hotel_main);
                transaction.commit();
                break;
            case 2:
                transaction.replace(R.id.main_frame, liked_main);
                transaction.commit();
                break;
            case 3:
                transaction.replace(R.id.main_frame, media_main);
                transaction.commit();
                break;
            case 4:
                transaction.replace(R.id.main_frame, setting_main);
                transaction.commit();
                break;
        }
    }
}