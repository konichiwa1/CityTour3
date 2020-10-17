package com.example.citytour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
    private EditText resetEmailEditText;
    private  Button sendEmailButton;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        resetEmailEditText=findViewById(R.id.resetEmailEditText);
        sendEmailButton=findViewById(R.id.sendEmailButton);

        auth=FirebaseAuth.getInstance();

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }

    private void sendMail() {
        String mail= resetEmailEditText.getText().toString();
        if(TextUtils.isEmpty(mail)){
            Toast.makeText(getApplicationContext(),"Enter a valid Email address!!",Toast.LENGTH_SHORT).show();
        }else{
            auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Please check your Email account..",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotActivity.this,LoginActivity.class));
                        finish();
                    }else{
                        String message=task.getException().getMessage();
                        Toast.makeText(getApplicationContext(),"Error Occured" + message,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}