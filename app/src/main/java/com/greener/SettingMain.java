package com.greener;

import static com.greener.SaveSharedPreference.getSharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SettingMain extends Fragment {

    private View view;
    public static String uid;
    public static String userEmail;
    private String username;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference, deleteReference;
    private ArrayList<UsersList> arrayList;

    private TextView setting_userid;
    private TextView setting_email;
    private TextView setting_isLogin;
    private Button setting_logoutButton;
    private Button setting_delete;

    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Setting activity changed");

        setHasOptionsMenu(true);

        view = inflater.inflate(R.layout.setting_main, container, false);

        setting_isLogin = view.findViewById(R.id.setting_isLogin);
        setting_userid = view.findViewById(R.id.setting_userid);
        setting_email = view.findViewById(R.id.setting_email);
        setting_logoutButton = view.findViewById(R.id.setting_logoutButton);
        setting_delete = view.findViewById(R.id.setting_delete);
        arrayList = new ArrayList<>(); // User ????????? ?????? ????????? ????????? (??????????????????)
        //?????? ????????? uid ????????????
        uid = firebaseAuth.getUid();

        databaseReference = database.getReference("users"); // DB ????????? ??????
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // ?????????????????? ????????????????????? ???????????? ???????????? ???
                arrayList.clear(); // ?????? ?????????????????? ?????????????????? ?????????
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // ??????????????? ????????? List??? ????????????
                    UsersList u = snapshot.getValue(UsersList.class); // ??????????????? User ????????? ???????????? ?????????.
                    if(u.getUid().equals(uid)) {
                        username = u.getName();
                        setting_userid.setText(username);
                    }
                    arrayList.add(u); // ?????? ??????????????? ?????????????????? ?????? ????????????????????? ?????? ??????
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // ????????? ??????????????? ?????? ?????? ???
                Log.e("LikedMain", String.valueOf(databaseError.toException())); // ????????? ??????
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail=user.getEmail();
            setting_email.setText(userEmail);
        }
        else setting_isLogin.setText("????????? ????????? ????????????.");

        setting_logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Toast.makeText(view.getContext(),"????????????",Toast.LENGTH_SHORT).show();
                SaveSharedPreference.clearUserName(view.getContext());

                intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
                removeFragment(SettingMain.this);

            }
        });

        setting_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(view.getContext(), "????????? ?????? ???????????????.", Toast.LENGTH_LONG).show();
                            }
                        });
                String path = "users/"+firebaseAuth.getUid();
                deleteReference = database.getInstance().getReference();
                deleteReference.child(path).removeValue();
                intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
                removeFragment(SettingMain.this);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.actionbar, menu);
    }

    private void removeFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
            final FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.remove(fragment);
            mFragmentTransaction.commit();
            fragment.onDestroy();
            fragment.onDetach();
            fragment = null;
        }
    }
}