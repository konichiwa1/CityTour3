package com.example.citytour;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class PhotoActivity extends AppCompatActivity {

    private String photoFolder=null;
    private ArrayList<String> mImages = new ArrayList<>();
    private StorageReference mStorageRef;
    private ProgressBar progress;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        //getSupportActionBar().setTitle("Photos");
        progress = findViewById(R.id.progBar);
        mAuth = FirebaseAuth.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recvphoto);
        final RecyclerViewAdapterPhoto adapter = new RecyclerViewAdapterPhoto(this, mImages);
        displayPhotos(adapter);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //getting intent extra from parent
        if(photoFolder==null)   {
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {
                    photoFolder= null;
                } else {
                    photoFolder= extras.getString("PastTripPhotoParticular");
                }
            } else {
                photoFolder= (String) savedInstanceState.getSerializable("PastTripPhotoParticular");
            }
        }

        displayPhotos(adapter);


        FloatingActionButton fab = findViewById(R.id.fabphoto);

        if(photoFolder!=null) mStorageRef = FirebaseStorage.getInstance().getReference().child("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(photoFolder);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
                displayPhotos(adapter);
                Log.d(TAG, "onClick: "+" "+mImages.size());
            }
        });

        Log.d(TAG, "onCreateView: ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)    {
            Uri targetUri = data.getData();
            Log.d(TAG, "onActivityResult: (targetUri********************************)"+targetUri.toString());

            StorageReference riversRef = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(targetUri));

            riversRef.putFile(targetUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            if(taskSnapshot.getMetadata()!=null && taskSnapshot.getMetadata().getReference()!=null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String dUrl = uri.toString();

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference().child("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(photoFolder);
                                        String id = myRef.push().getKey();
                                        myRef.child(id).setValue(dUrl);
                                        progress.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(com.example.citytour.PhotoActivity.this, "Failed to add Photo", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            progress.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

    private String getFileExtension (Uri uri)   {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void displayPhotos(final RecyclerViewAdapterPhoto adap)  {
        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(photoFolder==null) return;
        DatabaseReference myRef = database.getReference().child("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(photoFolder);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot dss: dataSnapshot.getChildren()) {
                    String value = dss.getValue(String.class);
                    if(!mImages.contains(value)) {
                        mImages.add(value);
                        Log.d(TAG, "onDataChange: (@*@*@@*@*@*@*@*@*@*@*@*@*@)"+value+" "+mImages.size());
                        adap.notifyItemInserted(mImages.size());
                    }
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
}
