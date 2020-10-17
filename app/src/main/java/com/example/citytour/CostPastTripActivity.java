package com.example.citytour;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class CostPastTripActivity extends AppCompatActivity {

    private ArrayList<CostUpload> mCosts = new ArrayList<>();
    private String costFolder = null;
    private TextView totalCost;
    private ArrayList<String> mKey = new ArrayList<>();
    private Double total;
    private String rmKey;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costs_pasttrip);
        totalCost = (TextView) findViewById(R.id.total_cost_pasttrip);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar_ptcost);
        tb.setTitle("Costs");
        setSupportActionBar(tb);

        if(costFolder==null)   {
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {
                    costFolder= null;
                } else {
                    costFolder= extras.getString("PastTripCostParticular");
                }
            } else {
                costFolder= (String) savedInstanceState.getSerializable("PastTripCostParticular");
            }
        }

        final RecyclerViewAdapterPTCost adapter = new RecyclerViewAdapterPTCost(this, mCosts, mKey, costFolder);
        RecyclerView recyclerView = findViewById(R.id.recvptcost);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        displayCosts(adapter);

        FloatingActionButton fab = findViewById(R.id.fab_addcost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTCostDialog ptcDialog = new PTCostDialog();
                Bundle bundle = new Bundle();
                bundle.putString("PastTripCostParticular",costFolder);
                ptcDialog.setArguments(bundle);
                ptcDialog.show((CostPastTripActivity.this).getSupportFragmentManager(), "cost dialog");
                displayCosts(adapter);
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();

                //removing from database
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(costFolder+"CostPT");
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
                mCosts.remove(pos);
                mKey.remove(pos);
                final RecyclerViewAdapterPTCost adap = new RecyclerViewAdapterPTCost(CostPastTripActivity.this, mCosts, mKey, costFolder);
                RecyclerView recyclerV = findViewById(R.id.recvptcost);
                recyclerV.setAdapter(adap);
                recyclerV.setLayoutManager(new LinearLayoutManager(CostPastTripActivity.this));
                recyclerV.setItemAnimator(new DefaultItemAnimator());
                displayTotal();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void displayCosts(final RecyclerViewAdapterPTCost adap)  {
        // Read from the database
        Log.d(TAG, "displayCosts: (^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^): called");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(costFolder==null) return;
        DatabaseReference myRef = database.getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(costFolder+"CostPT");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "onDataChange(<-------------------------->): called");
                for(DataSnapshot dss: dataSnapshot.getChildren()) {
                    CostUpload value = dss.getValue(CostUpload.class);
                    String key = dss.getKey();
                    if(!mKey.contains(key) && rmKey!=key) {
                        mCosts.add(value);
                        mKey.add(key);
                        Log.d(TAG, "onDataChange: called");
                        adap.notifyDataSetChanged();
                    }
                }
                displayTotal();
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void displayTotal()  {
        total = 0.00;
        for(CostUpload cu : mCosts)  {
            total += cu.getCost();
        }
        String d = new DecimalFormat("#.##").format(total);
        Log.d(TAG, "displayTotal: (<<<<<<<<<<>>>>>>>>>>>)"+ mCosts.size());
        totalCost.setText(getString(R.string.INR)+d);
    }
}
