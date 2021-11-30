package com.greener;

import android.content.Intent;
import android.icu.text.IDNA;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HotelMap extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    private DatabaseReference databaseReference;
    private NaverMap naverMap;
    private FusedLocationSource mLocationSource;
    private InfoWindow mInfoWindow;
    private FirebaseDatabase database;
    private OverlayImage image = OverlayImage.fromResource(R.drawable.ic_place_marker);

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mLocationSource = new FusedLocationSource(this, 100);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Hotel Map changed");
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.hotel_map, container, false);

        mapView = (MapView)view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mLocationSource = new FusedLocationSource(this, 100);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("호텔"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    StoreList HotelList = snapshot.getValue(StoreList.class); // 만들어뒀던 User 객체에 데이터를 담는다.

                    Double x = Double.parseDouble(HotelList.getX());
                    Double y = Double.parseDouble(HotelList.getY());
                    LatLng ll = new LatLng(x, y);
                    setmark(HotelList.getNameStr(), ll);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("TestActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        return view;
    }


    private void setmark(String name, LatLng latlng) {
        Marker marker = new Marker();
        marker.setPosition(latlng); // 마커 위치 찍기
        marker.setWidth(100);
        marker.setHeight(100);
        marker.setIcon(image); // 마커 이미지 넣기
        marker.setCaptionText(name);


        marker.setOnClickListener(new Overlay.OnClickListener(){
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                mInfoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getContext())
                {
                    @NonNull
                    @Override
                    public CharSequence getText(@NonNull InfoWindow infoWindow)
                    {
                        /*
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("tel:"+calNum));
                        startActivity(intent);

                         */
                        return name;
                    }
                });
                mInfoWindow.open(marker);
                return false;
            }
        });

        /*
        marker.setOnClickListener(overlay -> {
            mInfoWindow.setAdapter(new InfoWindow.DefaultViewAdapter(getContext()) {
                @NonNull
                @Override
                protected View getContentView(@NonNull InfoWindow infoWindow) {
                    Marker marker1 = infoWindow.getMarker();
                    PlaceInfo info = (PlaceInfo) marker1.getTag();
                    View view = View.inflate(getContext(), R.layout.view_info_window, null);
                    ((TextView) view.findViewById(R.id.txttitle)).setText(name);
                    ((TextView) view.findViewById(R.id.txtaddr)).setText(addr);
                    ((TextView) view.findViewById(R.id.txttel)).setText(calNum);
                    return view;
                }
            });
            return false;
        });


        marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                mInfoWindow.setAdapter(new InfoWindow.ViewAdapter() {
                    @NonNull
                    @Override
                    public View getView(@NonNull InfoWindow infoWindow) {
                        View view = View.inflate(getContext(), R.layout.view_info_window, null);
                        ((TextView) view.findViewById(R.id.txttitle)).setText(name);
                        ((TextView) view.findViewById(R.id.txtaddr)).setText(addr);
                        ((TextView) view.findViewById(R.id.txttel)).setText(calNum);
                        return view;
                    }
                });
                return false;
            }
        });

         */
        marker.setMap(naverMap);
    }

    public void onMapReady(@NonNull NaverMap naverMap) {

        this.naverMap = naverMap;
        naverMap.setLocationSource(mLocationSource);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(false); // 기본값 : true
        uiSettings.setLocationButtonEnabled(true); // 기본값 : false

        mInfoWindow = new InfoWindow();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.actionbar_map_action, menu);
    }

    /*
    @Override
    public boolean onClick(@NonNull Overlay overlay) {

        if (overlay instanceof Marker) {
            Marker marker = (Marker) overlay;
            if (marker.getInfoWindow() != null) {
                mInfoWindow.close();
                Toast.makeText(this.getContext(), "InfoWindow Close.", Toast.LENGTH_LONG).show();
            }
            else {
                mInfoWindow.open(marker);
                Toast.makeText(this.getContext(), "InfoWindow Open.", Toast.LENGTH_LONG).show();
            }
            return true;
        }


        return false;
    }

     */
}