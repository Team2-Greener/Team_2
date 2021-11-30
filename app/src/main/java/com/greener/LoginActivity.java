package com.greener;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.greener.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.greener.MainActivity;

public class LoginActivity extends AppCompatActivity {
    private String TAG = "LoginActivity";
    private FirebaseAuth firebaseAuth;
    private BackPressHandler backPressHandler = new BackPressHandler(this);
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if(SaveSharedPreference.getUserName(LoginActivity.this).length() == 0) {
            // call Login Activity
        } else {
            // Call Next Activity
            intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("STD_NUM", SaveSharedPreference.getUserName(this));
            Toast.makeText(this,"로그인 되었습니다.", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            this.finish();
        }
        findViewById(R.id.login_loginButton).setOnClickListener(onClickListener);
        findViewById(R.id.login_signupButton).setOnClickListener(onClickListener);
        findViewById(R.id.login_findPwBtn).setOnClickListener(onClickListener);

        firebaseAuth = firebaseAuth.getInstance();
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.login_signupButton:
                    myStartActivity(SignUpActivity.class);
                    finish();
                    break;

                case R.id.login_loginButton:
                    login();
                    break;
                case R.id.login_findPwBtn:
                    myStartActivity(FindPasswordActivity.class);
                    finish();
                    break;
            }
        }
    };
    @Override
    public void onBackPressed(){
        backPressHandler.onBackPressed("종료하려면 뒤로가기 버튼을 한번 더 누르세요", 3000);
    }
    private void login(){
        String email = ((EditText)findViewById(R.id.login_email)).getText().toString().trim();
        String password = ((EditText)findViewById(R.id.login_password)).getText().toString().trim();
        CheckBox checkBox = findViewById(R.id.autoLoginCheck);

        if(checkBox.isChecked() == true) SaveSharedPreference.setUserName(LoginActivity.this, email);

        if(email.length() >0 && password.length() >0){
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        startToast("로그인 성공");
                        myStartActivity(MainActivity.class);
                        finish();

                    }else{
                        if(task.getException() != null){
                            startToast("Email 또는 비밀번호를 확인하세요");
                        }
                    }
                }
            });
        }else{
            startToast("Email 또는 비밀번호를 입력해주세요");
        }
    }
    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

}