package com.example.citytour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText,passwordEditText;
    private Button loggedButton;
    FirebaseAuth auth;
    InputMethodManager imm;
    private TextView forgotPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        loggedButton=findViewById(R.id.loggedButton);
        forgotPasswordTextView=findViewById(R.id.forgotPasswordTextView);
        auth=FirebaseAuth.getInstance();
        loggedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });


        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotActivity.class));
                finish();
            }
        });
    }

    public void Login(){

                String email_txt=emailEditText.getText().toString();
                String password_txt=passwordEditText.getText().toString();

                loginUser(email_txt,password_txt);

    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Toast.makeText(LoginActivity.this,"You have successfully Logged in!",Toast.LENGTH_SHORT).show();
                emailEditText.getText().clear();
                passwordEditText.getText().clear();
                emailEditText.clearFocus();
                passwordEditText.clearFocus();

                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }

        });
        auth.signInWithEmailAndPassword(email,password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(LoginActivity.this,"Your E-mail id or password is incorrect!!",Toast.LENGTH_SHORT).show();

                passwordEditText.getText().clear();
                emailEditText.clearFocus();


                startActivity(new Intent(LoginActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
}