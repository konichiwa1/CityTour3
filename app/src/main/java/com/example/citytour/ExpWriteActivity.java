package com.example.citytour;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class ExpWriteActivity extends AppCompatActivity {

    private String expFolder=null;
    private String key=null;
    private EditText title;
    private EditText note;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expwrite);
        title = (EditText) findViewById(R.id.editTextTitle);
        note = (EditText) findViewById(R.id.editTextNote);
        Toolbar tb = findViewById(R.id.toolbwrite);
        tb.setTitle("");
        setSupportActionBar(tb);

        //displays the back arrow on toolbar
        if(getSupportActionBar() != null)   {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //finishes activity on clicking back arrow
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //getting intent extra from parent
        if(expFolder==null)   {
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {
                    expFolder= null;
                } else {
                    expFolder= extras.getString("PastTripExpParticular");
                    key = extras.getString("Key");
                }
            } else {
                expFolder= (String) savedInstanceState.getSerializable("PastTripExpParticular");
                key = (String) savedInstanceState.getSerializable("Key");
            }
        }

        displayExp();
    }

    public void displayExp() {
        Log.d(TAG, "displayExp: (&&&&&&&&&&&&&&&&&&&&&&&&&)"+key);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(expFolder+"Exp/"+key);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int i=0;
                for(DataSnapshot dss: dataSnapshot.getChildren()) {
                    String value = dss.getValue(String.class);
                    if(i==1) note.setText(value);
                    else if(i==2) title.setText(value);
                    i++;
                }
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void closeExpWrite() {
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_writer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final String ttl = title.getText().toString();
        final String nt = note.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(expFolder + "Exp");
        if (key != null) {
            final DatabaseReference cRef = database.getReference(expFolder + "Exp").child(key);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ExperienceUpload experience = new ExperienceUpload(ttl, nt, key);
                        myRef.child(key).setValue(experience);
                        closeExpWrite();
                    } else {
                        String id = myRef.push().getKey();
                        ExperienceUpload experience = new ExperienceUpload(ttl, nt, id);
                        myRef.child(id).setValue(experience);
                        closeExpWrite();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            cRef.addListenerForSingleValueEvent(valueEventListener);
        } else {
            String id = myRef.push().getKey();
            ExperienceUpload experience = new ExperienceUpload(ttl, nt, id);
            myRef.child(id).setValue(experience);
            closeExpWrite();
        }
        return super.onOptionsItemSelected(item);
    }
}
