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

public class SettingMain extends Fragment {

    private View view;
    public static String uid;
    public static String userEmail;

    private FirebaseAuth firebaseAuth;

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

        firebaseAuth = FirebaseAuth.getInstance();

        //현재 유저의 uid 받아오기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            userEmail=user.getEmail();
            setting_userid.setText(uid);
            setting_email.setText(userEmail);
        }
        else setting_isLogin.setText("로그인 정보가 없습니다.");

        setting_logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Toast.makeText(view.getContext(),"로그아웃",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(view.getContext(), "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
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