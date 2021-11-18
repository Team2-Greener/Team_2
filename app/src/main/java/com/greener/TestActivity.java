package com.greener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity{

    ListView listView;
    MyListAdapter myListAdapter;
    ArrayList<ListViewItem> list_itemArrayList;
    ArrayList<String> list_addressList;
    ArrayList<String> list_address;
    String image_name, name_eng, address;
    int index=0;//address 하나씩 읽어서 리스트뷰에 넣기위해서

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        listView = findViewById(R.id.testListview);

        list_itemArrayList = new ArrayList<>();
        list_addressList = new ArrayList<>();
        list_address = new ArrayList<>();

        /*list_itemArrayList.add(new ListViewItem("https://cdn-images-1.medium.com/fit/c/36/36/0*HgJ2Psmia7PjQsp9.jpg","보라돌이","제목1"));
        list_itemArrayList.add(new ListViewItem("https://cdn-images-1.medium.com/fit/c/36/36/0*HgJ2Psmia7PjQsp9.jpg","뚜비","제목2"));
        list_itemArrayList.add(new ListViewItem("https://cdn-images-1.medium.com/fit/c/36/36/0*HgJ2Psmia7PjQsp9.jpg","나나","제목3"));*/

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("hotel");
        boolean calledAlready = false;
        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true); // 다른 인스턴스보다 먼저 실행되어야 한다.
            calledAlready = true;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Log.d("DATABASE ADDRESS : ", database.toString());
        DatabaseReference databaseRef = database.getReference("호텔");
        // Read from the database
        databaseRef.child("주소").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 클래스 모델이 필요?
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    //MyFiles filename = (MyFiles) fileSnapshot.getValue(MyFiles.class);
                    //하위키들의 value를 어떻게 가져오느냐???
                    image_name = fileSnapshot.getKey();
                    Log.d("NAME IS : ", image_name);
                    address = fileSnapshot.getValue(String.class);
                    Log.i("TAG: ADDRESS is ", address);
                    list_addressList.add(address);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read hotel key", databaseError.toException());
            }

        });
        Handler handler = new Handler();
        int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if(!list_addressList.isEmpty())//checking if the data is loaded or not
                {
                    String msg2 = String.valueOf(list_addressList.size());
                    Toast.makeText(TestActivity.this, "address no : "+ msg2, Toast.LENGTH_SHORT).show();
                }
                else
                    handler.postDelayed(this, delay);
            }
        }, delay);
        databaseRef.child("이름").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 클래스 모델이 필요?
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    //MyFiles filename = (MyFiles) fileSnapshot.getValue(MyFiles.class);
                    //하위키들의 value를 어떻게 가져오느냐???
                    image_name = fileSnapshot.getKey();
                    Log.d("NAME IS : ", image_name);
                    name_eng = fileSnapshot.getValue(String.class);
                    Log.i("TAG: value is ", name_eng);
                    Log.d("ADD HOTEL : ",name_eng);

                    if (pathReference == null) {
                        Toast.makeText(TestActivity.this, "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        StorageReference submitProfile = pathReference.child(name_eng+".jpg");
                        submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(TestActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                                list_itemArrayList.add(new ListViewItem(String.valueOf(uri),name_eng,list_addressList.get(index++)));
                                Log.d("STORAGE ADDRESS : ", submitProfile.toString());

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read hotel key", databaseError.toException());
            }
        });
        myListAdapter = new MyListAdapter(TestActivity.this, list_itemArrayList);

        String msg = String.valueOf(myListAdapter.getCount());
        Toast.makeText(TestActivity.this, msg, Toast.LENGTH_SHORT).show();

        listView.setAdapter(myListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TestActivity.this,list_itemArrayList.get(position).getTitle(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*private View view;
    String image_name, name_eng;
    ArrayList<com.greener.HotelActivity.Hotel> hotels;
    ListView customListView;
    private static CustomAdapter customAdapter;

   @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main, container, false);

        hotels = new ArrayList<>();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("hotel");
        boolean calledAlready = false;
        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true); // 다른 인스턴스보다 먼저 실행되어야 한다.
            calledAlready = true;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Log.d("DATABASE ADDRESS : ", database.toString());
        DatabaseReference databaseRef = database.getReference("호텔");
        // Read from the database
        databaseRef.child("이름").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 클래스 모델이 필요?
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    //MyFiles filename = (MyFiles) fileSnapshot.getValue(MyFiles.class);
                    //하위키들의 value를 어떻게 가져오느냐???
                    image_name = fileSnapshot.getKey();
                    Log.d("NAME IS : ", image_name);
                    name_eng = fileSnapshot.getValue(String.class);
                    Log.i("TAG: value is ", name_eng);
                    Log.d("ADD HOTEL : ",name_eng);

                    if (pathReference == null) {
                        Toast.makeText(container.getContext(), "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        StorageReference submitProfile = pathReference.child(name_eng+".jpg");
                        submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(container.getContext(), uri.toString(), Toast.LENGTH_SHORT).show();

                                hotels.add(new com.greener.HotelActivity.Hotel(uri.toString(),name_eng,"text예시" ));
                                Log.d("STORAGE ADDRESS : ", submitProfile.toString());

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read hotel key", databaseError.toException());
            }
        });

        customListView = view.findViewById(R.id.testListview);
        customAdapter = new CustomAdapter(getContext(),hotels);
        customListView.setAdapter(customAdapter);
        Toast.makeText(getContext(), "finish", Toast.LENGTH_SHORT).show();

        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                //각 아이템을 분간 할 수 있는 position과 뷰
                String selectedItem = (String) view.findViewById(R.id.title).getTag().toString();
                Toast.makeText(getContext(), "Clicked: " + position +" " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }*/
}