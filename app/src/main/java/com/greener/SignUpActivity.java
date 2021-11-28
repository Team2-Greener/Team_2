package com.greener;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.jung_oh.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private EditText username_edit, email_edit, password_edit, checkPWD_edit;
    Button register_button;
    private boolean isCompleted = false;
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username_edit = findViewById(R.id.inputName);
        email_edit = findViewById(R.id.inputEmailCheck);
        password_edit = findViewById(R.id.inputPassword);
        checkPWD_edit = findViewById(R.id.inputPasswordConfirm);
        register_button = findViewById(R.id.signupedButton);
        findViewById(R.id.signupedButton).setOnClickListener(onClickListener);
        findViewById(R.id.back_login).setOnClickListener(onClickListener);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            switch(view.getId()){
                case R.id.signupedButton:
                    signUp();
                    break;

                case R.id.back_login:
                    myStartActivity(LoginActivity.class);
                    finish();
                    break;
            }
        }
    };
    public void onBackPressed(){

        backPressHandler.onBackPressed("종료하려면 뒤로가기 버튼을 한번 더 누르세요", 3000);

    }

    private void signUp(){
        String name = username_edit.getText().toString().trim();
        String email = email_edit.getText().toString().trim();
        String password = password_edit.getText().toString().trim();
        String passwordCheck = checkPWD_edit.getText().toString().trim();


        if(name.length() > 0 && email.length() >0 && password.length() >0 && passwordCheck.length()>0){

            if(password.length() < 9){
                startToast("비밀번호를 9자리 이상 입력해 주십시오.");
            }
            else{
                isCompleted = true;
            }
        }
        if(isCompleted){
            if(password.equals(passwordCheck)){
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user =  firebaseAuth.getCurrentUser();
                            startToast("회원가입 성공");
                            myStartActivity(SignUpAcceptActivity.class);
                            myStartActivity(LoginActivity.class);
                            finish();
                        }
                        else{
                            if(task.getException() != null){
                                startToast("이미 등록된 이메일입니다.");
                            }
                        }
                    }
                });
            }else{
                startToast("비밀번호가 일치하지 않습니다.");
                checkPWD_edit.requestFocus();
            }
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(SignUpActivity.this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
            username_edit.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(SignUpActivity.this, "email을 입력하세요", Toast.LENGTH_SHORT).show();
            username_edit.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(SignUpActivity.this, "password를 입력하세요", Toast.LENGTH_SHORT).show();
            username_edit.requestFocus();
            return;
        }else if(TextUtils.isEmpty(passwordCheck)){
            Toast.makeText(SignUpActivity.this, "password를 한번 더 입력하세요", Toast.LENGTH_SHORT).show();
            username_edit.requestFocus();
            return;
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
