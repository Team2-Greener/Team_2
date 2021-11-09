package com.greener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private static NaverMap naverMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //네이버 지도
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap)
    {
        this.naverMap = naverMap;

        // 건물 표시
       naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);

        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(33.38, 126.55),
                9
        );
        naverMap.setCameraPosition(cameraPosition);
    }

    // Life Cycle handling

    @Override
    public void onStart()
    {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}