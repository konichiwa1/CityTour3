package com.example.citytour;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapterPhoto extends RecyclerView.Adapter<com.example.citytour.RecyclerViewAdapterPhoto.ViewHolder> {

    private static final String TAG = "RecylerViewAdapterPhoto";
    private Context mContext;
    private ArrayList<String> mImages = new ArrayList<>();

    public RecyclerViewAdapterPhoto(Context context, ArrayList<String> images) {
        mImages = images;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_cards, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(com.example.citytour.RecyclerViewAdapterPhoto.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext).load(mImages.get(position)).into(holder.image);

        Log.d(TAG, "onBindViewHolder: (################################)"+mImages.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mImages.get(position));

                Toast.makeText(mContext, mImages.get(position), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.pcphoto);
            parentLayout = itemView.findViewById(R.id.photocard);
        }
    }
}
