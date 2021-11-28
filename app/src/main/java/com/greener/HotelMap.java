package com.greener;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HotelMap extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    private ArrayList<StoreList> arrayList = new ArrayList<>();
    private List<Address> addrList = null;
    private String addrstr;
    private DatabaseReference databaseReference;
    private NaverMap naverMap;
    private FusedLocationSource mLocationSource;
    private InfoWindow mInfoWindow;

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

        Geocoder geocoder = new Geocoder(getContext());

        mInfoWindow = new InfoWindow();
        OverlayImage image = OverlayImage.fromResource(R.drawable.ic_place_marker);
        arrayList = new ArrayList<>();

        databaseReference = MainActivity.database.getReference("호텔");
        Executor executor = Executors.newFixedThreadPool(100);
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            List<Marker> markers = new ArrayList<>();

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    arrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        StoreList HotelList = snapshot.getValue(StoreList.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                        arrayList.add(HotelList); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                        System.out.println("name : "+HotelList.getNameStr()+", addr : "+HotelList.getAddressStr());

                        addrList = null;
                        String straddr = HotelList.getNameStr();
                        LatLng latlng = null;

                        try {
                            addrList = geocoder.getFromLocationName(straddr, 10);
                            while(addrList.size() == 0) {
                                addrList = geocoder.getFromLocationName(straddr, 1);
                            }
                            if(addrList.size() > 0) {
                                Address addr = addrList.get(0);
                                latlng = new LatLng(addr.getLatitude(), addr.getLongitude());
                                System.out.println("lat : "+addr.getLatitude()+", lng : "+ addr.getLongitude());
                            }
                        } catch (Exception e) {
                            System.out.print(e.getMessage());
                        }

                        Marker marker = new Marker();
                        marker.setPosition(latlng); // 마커 위치 찍기
                        // 커스텀 마커
                        marker.setWidth(100);
                        marker.setHeight(100);
                        marker.setIcon(image); // 마커 이미지 넣기
                        marker.setCaptionText(HotelList.getNameStr());


                        /*
                        marker.setOnClickListener(new Overlay.OnClickListener(){

                            @Override
                            public boolean onClick(@NonNull Overlay overlay) {
                                mInfoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getContext())
                                {
                                    @NonNull
                                    @Override
                                    public CharSequence getText(@NonNull InfoWindow infoWindow)
                                    {
                                        return HotelList.getNameStr();
                                    }
                                });
                                mInfoWindow.open(marker);
                                return false;
                            }
                        });

                         */
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 디비를 가져오던중 에러 발생 시
                    Log.e("TestActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                }
            });

            handler.post(() -> {
                for(Marker marker : markers){
                    marker.setMap(naverMap); // 마커 지도에 넣기
                }
            });
        });

        return view;
    }


    public void onMapReady(@NonNull NaverMap naverMap) {

        this.naverMap = naverMap;
        naverMap.setLocationSource(mLocationSource);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(false); // 기본값 : true
        uiSettings.setLocationButtonEnabled(true); // 기본값 : false
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.actionbar_map_action, menu);
    }

}