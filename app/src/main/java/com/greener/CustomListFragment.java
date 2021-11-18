package com.greener;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.ListFragment;

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

public class CustomListFragment extends ListFragment {
  /*  ListViewAdapter adapter ;
    String image_name,name_eng;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();

        // Adapter 생성 및 Adapter 지정.
        adapter = new ListViewAdapter() ;
        setListAdapter(adapter) ;

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

                    if (pathReference == null) {
                        Toast.makeText(context, "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        StorageReference submitProfile = pathReference.child(name_eng+".jpg");
                        Log.d("STORAGE ADDRESS : ", submitProfile.toString());

                        submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(context)
                                        .load(uri)
                                        .into(new CustomTarget<Drawable>() {
                                            @Override
                                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                adapter.addItem(resource, image_name, name_eng);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                            }
                                        });
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

        return super.onCreateView(inflater, container, savedInstanceState);
    }*/
}
