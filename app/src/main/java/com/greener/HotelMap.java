package com.greener;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HotelMap extends Fragment {

    private ArrayList<StoreList> arrayList = new ArrayList<>();
    private DatabaseReference databaseReference;

    private Geocoder geocoder;

    private MapView mapView;
    private NaverMap naverMap;
    private InfoWindow mInfoWindow;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Hotel Map changed");

        setHasOptionsMenu(true);

        view = inflater.inflate(R.layout.hotel_map, container, false);

        mInfoWindow = new InfoWindow();

        arrayList = new ArrayList<>();

        databaseReference = MainActivity.database.getReference("호텔");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    StoreList HotelList = snapshot.getValue(StoreList.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    arrayList.add(HotelList); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                    // System.out.println(HotelList.getNameStr());
                    // System.out.println(HotelList.getAddressStr());

                    List<Address> addrList = null;
                    try {
                        addrList = geocoder.getFromLocationName(HotelList.getAddressStr(), 10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String []splitStr = addrList.get(0).toString().split(",");
                    String addr = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소

                    String lat = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                    String lng = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도

                    double dLat = Double.parseDouble(lat);
                    double dLng = Double.parseDouble(lng);
                    System.out.println("lat "+lat+", lng : "+lng);

                    Marker marker = new Marker(); // 마커 생성
                    marker.setPosition(new LatLng(dLat, dLng)); // 마커 위치 찍기
                    marker.setMap(naverMap); // 마커 지도에 넣기

                    // 커스텀 마커
                    marker.setWidth(100);
                    marker.setHeight(100);
                    marker.setIcon(OverlayImage.fromResource(R.drawable.ic_place_marker)); // 마커 이미지 넣기
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("TestActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        mapView = (MapView)container.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);



        return view;
    }


    public void onMapReady(@NonNull NaverMap naverMap) {

        this.naverMap = naverMap;

        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(37.5670135, 126.9783740), 13);
        naverMap.setCameraPosition(cameraPosition);

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