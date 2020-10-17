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

public class PastTripDialog extends AppCompatDialogFragment {

    private EditText editPlaceName;
    private FirebaseAuth mAuth;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_past_trip_dialog, null);

        builder.setView(view)
                .setTitle("Enter the place you went")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String placeName = editPlaceName.getText().toString();

                        //firebase pastTrip name upload to database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        mAuth = FirebaseAuth.getInstance();
                        DatabaseReference myRef = database.getReference().child("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("PastTripName");
                        String uploadid = myRef.push().getKey();
                        myRef.child(uploadid).setValue(placeName);
                    }
                });

        editPlaceName = view.findViewById(R.id.place_name_past);

        return builder.create();
    }

}
