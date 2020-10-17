package com.example.citytour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText newPasswordEditText, confirmNewPasswordEditText, oldPasswordEditText, emailCnfEditText;
    private Button changeButton;
    Toolbar toolbar2;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmNewPasswordEditText = findViewById(R.id.confirmNewPasswordEditText);
        oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        emailCnfEditText = findViewById(R.id.email_cnfEditText);
        changeButton = findViewById(R.id.changeButton);

        auth = FirebaseAuth.getInstance();

        toolbar2 = findViewById(R.id.toolbar2);
        toolbar2.setTitle("Change Password");
        setSupportActionBar(toolbar2);

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }


    private void changePassword() {
        String newly = newPasswordEditText.getText().toString();
        final String confirm = confirmNewPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(newly) && TextUtils.isEmpty(confirm)) {
            Toast.makeText((getApplicationContext()), "Please fill all the Fields.", Toast.LENGTH_SHORT).show();
        } else {
            if (newly.equals(confirm)) {

                final FirebaseUser user = auth.getCurrentUser();

                AuthCredential credential = EmailAuthProvider
                        .getCredential(emailCnfEditText.getText().toString(), oldPasswordEditText.getText().toString());

                if(user!=null)  {
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(confirm).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
                                                } else {
                                                    Toast.makeText(ChangePasswordActivity.this, "Error password not updated", Toast.LENGTH_SHORT).show();
                                                    confirmNewPasswordEditText.getText().clear();
                                                    newPasswordEditText.getText().clear();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(ChangePasswordActivity.this, "You entered wrong email/old password", Toast.LENGTH_SHORT).show();
                                        confirmNewPasswordEditText.getText().clear();
                                        newPasswordEditText.getText().clear();
                                        oldPasswordEditText.getText().clear();
                                        emailCnfEditText.getText().clear();
                                    }
                                }
                            });
                }
            }

            else {
                Toast.makeText(getApplicationContext(), " Passwords are not matching", Toast.LENGTH_SHORT).show();
                confirmNewPasswordEditText.getText().clear();
                newPasswordEditText.getText().clear();
            }

        }
    }
}

