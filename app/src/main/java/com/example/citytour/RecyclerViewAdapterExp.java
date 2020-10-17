package com.example.citytour;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterExp extends RecyclerView.Adapter<com.example.citytour.RecyclerViewAdapterExp.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapterExp";
    private Context mContext;
    private ArrayList<ExperienceUpload> mExperiences = new ArrayList<>();
    private ArrayList<String> mKey = new ArrayList<>();
    private String expFolder;

    public RecyclerViewAdapterExp(Context context, ArrayList<ExperienceUpload> experiences, ArrayList<String> key, String expfolder) {
        mContext = context;
        mExperiences = experiences;
        expFolder = expfolder;
        mKey = key;
    }

    @Override
    public com.example.citytour.RecyclerViewAdapterExp.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exp_card, parent, false);
        com.example.citytour.RecyclerViewAdapterExp.ViewHolder holder = new com.example.citytour.RecyclerViewAdapterExp.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final com.example.citytour.RecyclerViewAdapterExp.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.tileText.setText(mExperiences.get(position).getTitle());
        holder.contentText.setText(mExperiences.get(position).getNote());

        Log.d(TAG, "onBindViewHolder: (################################)"+mExperiences.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mExperiences.get(position));

                Intent intent = new Intent(mContext, ExpWriteActivity.class);
                intent.putExtra("PastTripExpParticular",expFolder);
                intent.putExtra("Key", mExperiences.get(position).getKey());
                mContext.startActivity(intent);
                //displayHolder(holder, position);
                Toast.makeText(mContext, mExperiences.get(position).getTitle(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mExperiences.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tileText;
        TextView contentText;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tileText = itemView.findViewById(R.id.tvtitle);
            contentText = itemView.findViewById(R.id.tvnote);
            parentLayout = itemView.findViewById(R.id.expcard);
        }
    }

}
