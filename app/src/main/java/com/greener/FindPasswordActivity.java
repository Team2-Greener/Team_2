package com.greener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindPasswordActivity extends AppCompatActivity {
    private String TAG = "FindPasswordActivity";

    private FirebaseAuth firebaseAuth;
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.findPwOkBtn).setOnClickListener(onClickListener);
        findViewById(R.id.back_login).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.findPwOkBtn:
                    send();
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

    private void send() {
        String email = ((EditText) findViewById(R.id.findPwEmail)).getText().toString().trim();
        String name = ((EditText) findViewById(R.id.findPwName)).getText().toString().trim();

        if(name.length() > 0 && email.length() > 0) {
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startToast("이메일을 보냈습니다.");
                                myStartActivity(LoginActivity.class);
                                finish();
                            }
                        }
                    });
        }else{
            startToast("이메일 또는 이름을 입력해주세요.");
        }
    }

    private  void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private  void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}