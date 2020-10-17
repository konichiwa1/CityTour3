package com.example.citytour;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<com.example.citytour.RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecylerViewAdapter";

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private Context mContext;
    private FirebaseAuth mAuth;


    public RecyclerViewAdapter(Context context, ArrayList<String> imageNames, ArrayList<String> images) {
        mNames = imageNames;
        mImages = images;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_trip_cards, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if(mNames.get(position)==null) return;
        DatabaseReference myRef = database.getReference().child("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(mNames.get(position));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Glide.with(mContext).load("").placeholder(R.drawable.placeholderimage).centerCrop().into(holder.image);
                for(DataSnapshot dss: dataSnapshot.getChildren()) {
                    String value = dss.getValue(String.class);
                    Glide.with(mContext).load(value).placeholder(R.drawable.placeholderimage).centerCrop().into(holder.image);
                    break;
                }
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        holder.imageName.setText(mNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mNames.get(position));

                Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, RecviewActivity.class);

                intent.putExtra("PastTripParticular",mNames.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView imageName;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imv);
            imageName = itemView.findViewById(R.id.txv);
            parentLayout = itemView.findViewById(R.id.ptcard);
        }
    }
}
