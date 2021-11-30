package com.greener;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    public static FirebaseDatabase database;

    private BottomNavigationView bottomNavigationView;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    private ShopMain shop_main;
    private HotelMain hotel_main;
    private LikedMain liked_main;
    private MediaMain media_main;
    private SettingMain setting_main;
    private ShopMap shop_map;
    private HotelMap hotel_map;
    private LikedMap liked_map;

    private MenuItem search_menu;
    public static int fragNum;

    public static String uid, saveX, saveY;
    public static Context context_main;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //현재 유저의 uid 받아오기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

        context_main = this;

        fragNum = 0;
        //Toolbar 추가하기
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //화면 전환
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.shop_menu:
                        setFragment(0);
                        fragNum = 0;
                        break;
                    case R.id.hotel_menu:
                        setFragment(1);
                        fragNum = 1;
                        break;
                    case R.id.liked_menu:
                        setFragment(2);
                        fragNum = 2;
                        break;
                    case R.id.media_menu:
                        setFragment(3);
                        fragNum = 3;
                        break;
                    case R.id.setting_menu:
                        setFragment(4);
                        fragNum = 4;
                        break;
                }
                return false;
            }
        });
        shop_main = new ShopMain();
        hotel_main = new HotelMain();
        liked_main = new LikedMain();
        media_main = new MediaMain();
        setting_main = new SettingMain();
        shop_map = new ShopMap();
        hotel_map = new HotelMap();
        liked_map = new LikedMap();

        setFragment(fragNum);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //toolbar menu 구성
        getMenuInflater().inflate(R.menu.actionbar_list_action, menu);

        //검색버튼 등록
        search_menu = menu.findItem(R.id.search_action);
        //searchView 크기 꽉차도록
        SearchView searchView = (SearchView) search_menu.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setQueryHint("검색어를 입력하세요");

        //검색 클릭하면 SearchMain으로 이동
        search_menu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getApplicationContext(), SearchMain.class);
                startActivity(intent);

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.search_action) {
            //todo search 구현 && bottombar 지우기
        }
        else if(id == R.id.list_view_action) {
            System.out.println("list_view call");

            setFragment(fragNum);
        }
        else if(id == R.id.map_view_action) {
            System.out.println("map_view call");

            setFragment(fragNum+5);
        }
        return true;
    }

    // Life Cycle handling

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }


    //Fragment 이동
    public void setFragment(int n){
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        switch(n){
            case 0:
                transaction.replace(R.id.main_frame, shop_main);    //shop_main으로 이동
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
            case 3:
                transaction.replace(R.id.main_frame, media_main);   //media_main으로 이동
                transaction.commit();
                break;
            case 4:
                transaction.replace(R.id.main_frame, setting_main); //setting_main으로 이동
                transaction.commit();
                break;
            case 5:
                transaction.replace(R.id.main_frame, shop_map);
                transaction.commit();
                break;
            case 6:
                transaction.replace(R.id.main_frame, hotel_map);
                transaction.commit();
                break;
            case 7:
                transaction.replace(R.id.main_frame, liked_map);
                transaction.commit();
                break;
        }
    }
}