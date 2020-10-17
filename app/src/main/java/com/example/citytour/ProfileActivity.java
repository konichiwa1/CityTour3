package com.example.citytour;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private Button saveButton;
    private EditText nameEditText,profEditText,phoneEditText;
    private TextView dateEditText;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    String name;
    DatePickerDialog datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        saveButton=findViewById(R.id.saveButton);

        nameEditText=findViewById(R.id.nameEditText);
        profEditText=findViewById(R.id.profEditText);
        dateEditText=findViewById(R.id.dateEditText);
        phoneEditText=findViewById(R.id.phoneEditText);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datepicker = new DatePickerDialog(ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String day = String.valueOf(dayOfMonth);
                                if(dayOfMonth/10==0) day = "0"+day;
                                String month = String.valueOf(monthOfYear+1);
                                if((dayOfMonth+1)/10==0) month = "0"+month;
                                dateEditText.setText(day + "/" + month + "/" + year);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        firebaseDatabase= FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

    }

    public void save(){

        name = nameEditText.getText().toString();
        String profession = profEditText.getText().toString();
        String date=dateEditText.getText().toString();
        String phone=phoneEditText.getText().toString();


        Map<String,String> userProfile= new HashMap<>();
        userProfile.put("name",name);
        userProfile.put("profession",profession);
        userProfile.put("date",date);
        userProfile.put("phone",phone);


        if(userProfile.isEmpty()){
            Toast.makeText(getApplicationContext(),"Fill all columns!!",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(ProfileActivity.this,ProfileActivity.class));
        }
        else {
            firebaseDatabase.getReference().child("users").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).setValue(userProfile);
            Toast.makeText(getApplicationContext(),"Profile created successfully!",Toast.LENGTH_SHORT).show();
            userProfile.clear();

            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();


        }
    }
}