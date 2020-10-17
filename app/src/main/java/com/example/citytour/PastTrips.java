package com.example.citytour;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.citytour.PastTrips#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PastTrips extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    public static ArrayList<Bitmap> mBM = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PastTrips() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static com.example.citytour.PastTrips newInstance(String param1, String param2) {
        com.example.citytour.PastTrips fragment = new com.example.citytour.PastTrips();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.past_trip, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recvpt);

        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(this.getContext(), mNames, mImages);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("PastTripName");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String pName;
                for(DataSnapshot dss: dataSnapshot.getChildren())   {
                    pName = dss.getValue(String.class);
                    if(!mNames.contains(pName)) mNames.add(pName);
                    Log.d(TAG, "onDataChange(***************************************): "+pName);
                }



                adapter.notifyItemInserted(mNames.size());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                Toast.makeText(getContext(), "Failed to read value", Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fabpt);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses fab button
                Log.d(TAG, "onClick: "+" "+mNames.size());
                openDialog();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("PastTripName");
                // Read from the database
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String pName;
                        for(DataSnapshot dss: dataSnapshot.getChildren())   {
                            pName = dss.getValue(String.class);
                            if(!mNames.contains(pName)) mNames.add(pName);
                            Log.d(TAG, "onDataChange(***************************************): "+pName);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                        Toast.makeText(getContext(), "Failed to read value", Toast.LENGTH_SHORT).show();
                    }
                });

                adapter.notifyItemInserted(mNames.size());
            }
        });

        Log.d(TAG, "onCreateView: ");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    //public void insertPastTripCard(View v, final RecyclerViewAdapter adap)    {
      //
    //}

    public void openDialog() {
        PastTripDialog ptd = new PastTripDialog();
        Log.d(TAG, "openDialog: "+"*******************************************************************************"+mNames.size());
        ptd.show(getChildFragmentManager(), "uwh");

    }


}