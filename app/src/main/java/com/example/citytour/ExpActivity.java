package com.example.citytour;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class ExpActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private String photoFolder;
    private ArrayList<ExperienceUpload> mExperiences = new ArrayList<>();
    private ArrayList<String> mKey = new ArrayList<>();
    private String rmKey;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbexp);
        tb.setTitle("Experiences");
        setSupportActionBar(tb);

        //getting intent from parent
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                photoFolder= null;
            } else {
                photoFolder= extras.getString("PastTripExpParticular");
            }
        } else {
            photoFolder= (String) savedInstanceState.getSerializable("PastTripExpParticular");
        }

        RecyclerView recyclerView = findViewById(R.id.recvexp);
        final RecyclerViewAdapterExp adapter = new RecyclerViewAdapterExp(this, mExperiences, mKey, photoFolder);
        displayExp(adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fab = findViewById(R.id.fabexp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.citytour.ExpActivity.this, ExpWriteActivity.class);
                intent.putExtra("PastTripExpParticular", photoFolder);
                startActivity(intent);
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder vH, int direction) {
                int pos = vH.getAdapterPosition();

                //removing from database
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(photoFolder+"Exp");
                Query applesQuery = ref.child(mKey.get(pos));
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });

                rmKey=mKey.get(pos); //storing the key of removed item because the mExperience arraylist was getting again filled with the item that was deleted
                removeAL(pos);
                final RecyclerViewAdapterExp adap = new RecyclerViewAdapterExp(ExpActivity.this, mExperiences, mKey, photoFolder);
                RecyclerView recyclerV = findViewById(R.id.recvexp);
                recyclerV.setAdapter(adap);
                recyclerV.setLayoutManager(new LinearLayoutManager(ExpActivity.this));
                recyclerV.setItemAnimator(new DefaultItemAnimator());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void displayExp(final RecyclerViewAdapterExp adap) {
        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(photoFolder==null) return;
        DatabaseReference myRef = database.getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(photoFolder+"Exp");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot dss: dataSnapshot.getChildren()) {
                    ExperienceUpload value = dss.getValue(ExperienceUpload.class);
                    String key = dss.getKey();
                    if(!mKey.contains(key) && rmKey!=key) { //rmKey!=key prevents from filling of arraylist again after swipe deleting which is happening due to lag in realtime database
                        mExperiences.add(value);
                        mKey.add(key);
                        adap.notifyDataSetChanged();
                    }
                }
                Log.d(TAG, "onDataChange: ");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mExperiences.clear();
        mKey.clear();
        RecyclerView recyclerView = findViewById(R.id.recvexp);
        final RecyclerViewAdapterExp adapter = new RecyclerViewAdapterExp(this, mExperiences, mKey, photoFolder);
        displayExp(adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void removeAL(int pos)   {
        mExperiences.remove(pos);
        mKey.remove(pos);
    }

}
