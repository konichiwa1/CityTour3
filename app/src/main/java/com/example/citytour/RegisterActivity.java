package com.example.citytour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEditText,passwordEditText;
    private Button registeredButton;
    FirebaseAuth auth;
    String email_txt;
    String password_txt;
    InputMethodManager imm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        registeredButton=findViewById(R.id.registeredButton);
        auth=FirebaseAuth.getInstance();
        registeredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });





    }

    public void Register(){

                 email_txt=emailEditText.getText().toString();
                 password_txt=passwordEditText.getText().toString();

                if(TextUtils.isEmpty(email_txt) || TextUtils.isEmpty(password_txt))
                    Toast.makeText(RegisterActivity.this,"Empty Credentials!!!",Toast.LENGTH_SHORT).show();

                else if(password_txt.length()<6)
                    Toast.makeText(RegisterActivity.this,"Password size atleast of 6 characters!!",Toast.LENGTH_SHORT).show();

                else{
                    registerUser(email_txt,password_txt);
                }


    }


    private void registerUser(String email, String password) {

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                    FirebaseUser user = auth.getCurrentUser();

                    emailEditText.getText().clear();
                    passwordEditText.getText().clear();
                    emailEditText.clearFocus();
                    passwordEditText.clearFocus();

                    startActivity(new Intent(RegisterActivity.this,ProfileActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this,"You have already registered!",Toast.LENGTH_SHORT).show();

                    passwordEditText.getText().clear();
                    emailEditText.clearFocus();
                    startActivity(new Intent(RegisterActivity.this,SignUpActivity.class));
                    finish();



                }
            }
        });

    }
}