package com.greener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HotelActivity extends Fragment {

    private View view;
    String image_name, name_eng;
    ArrayList<com.greener.HotelActivity.Hotel> hotels;
    ListView customListView;
    private static CustomAdapter customAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Hotel activity changed");

        view = inflater.inflate(R.layout.activity_hotel, container, false);

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

                                hotels.add(new Hotel(uri.toString(),name_eng,"text예시" ));
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

        customListView = view.findViewById(R.id.listview);
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
    }
    //data class
    static class Hotel {
        private String image_uri;
        private String title;
        private String text;

        public Hotel(String image_uri, String title, String text) {
            this.image_uri = image_uri;
            this.title = title;
            this.text = text;
        }

        public String getImage_uri() {
            return image_uri;
        }

        public String getTitle() {
            return title;
        }

        public String getText() {
            return text;
        }
    }
}