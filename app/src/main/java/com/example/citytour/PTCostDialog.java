package com.example.citytour;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class PTCostDialog extends AppCompatDialogFragment {

    private EditText description;
    private EditText costs;
    private String costFolder;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.cost_ptdialog, null);

        Bundle bundle = getArguments();
        costFolder = bundle.getString("PastTripCostParticular", "");

        builder.setView(view)
                .setTitle("Enter the expenses")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String des = description.getText().toString();
                        final Double cost = Double.parseDouble(costs.getText().toString());

                        //firebase pastTrip name upload to database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(costFolder+"CostPT");

                        String uploadid = myRef.push().getKey();
                        CostUpload costUpload = new CostUpload(des, cost, uploadid);
                        myRef.child(uploadid).setValue(costUpload);
                    }
                });

        description = view.findViewById(R.id.tvddescription);
        costs = view.findViewById(R.id.tvdcost);

        return builder.create();
    }
}
