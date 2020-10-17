package com.example.citytour;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterPTCost extends RecyclerView.Adapter<RecyclerViewAdapterPTCost.ViewHolder> {


    private static final String TAG = "RecyclerViewAdapterPTCo";
    private ArrayList<CostUpload> mCosts= new ArrayList<>();
    private ArrayList<String> mKey = new ArrayList<>();
    private String costFolder;
    private Context mContext;

    public RecyclerViewAdapterPTCost(Context context, ArrayList<CostUpload> costs, ArrayList<String> Key, String costfolder) {
        mCosts = costs;
        mContext = context;
        mKey = Key;
        costFolder = costfolder;
    }

    @Override
    public RecyclerViewAdapterPTCost.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.costs_ptcard, parent, false);
        RecyclerViewAdapterPTCost.ViewHolder holder = new RecyclerViewAdapterPTCost.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterPTCost.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        holder.description.setText(mCosts.get(position).getDescription());
        holder.costs.setText(mContext.getString(R.string.INR)+mCosts.get(position).getCost().toString());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mCosts.get(position));

                Toast.makeText(mContext, mCosts.get(position).getDescription(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mCosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView description;
        TextView costs;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.tvitem);
            costs = itemView.findViewById(R.id.tvexpense);
            parentLayout = itemView.findViewById(R.id.costsptcard);
        }
    }
}
