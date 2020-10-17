package com.example.citytour.ui.home;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.citytour.Informations;
import com.example.citytour.MainActivity;
import com.example.citytour.R;
import com.example.citytour.photuuu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private Uri imageuri,croppedImageUri;
    ImageView imageView3;
    ImageButton imageButton2;
    FirebaseDatabase firebaseDatabase;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("profilephoto").child(uid);
    DatabaseReference databaseReference1 =FirebaseDatabase.getInstance().getReference().child("users").child(uid);


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_page, container, false);
        imageView3 = root.findViewById(R.id.usp_image);
        imageButton2 = root.findViewById(R.id.usp_uploadimage);
        final TextView tvName = (TextView) root.findViewById(R.id.usp_name);
        final TextView tvPhone = (TextView) root.findViewById(R.id.usp_phone);
        TextView tvEmail = (TextView) root.findViewById(R.id.usp_email);
        String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        tvEmail.setText(email);
        final TextView tvProfession = (TextView) root.findViewById(R.id.usp_profession);
        final TextView tvDOB = (TextView) root.findViewById(R.id.usp_dob);

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Informations info = snapshot.getValue(Informations.class);
                tvName.setText(info.getName());
                tvPhone.setText(info.getPhone());
                tvProfession.setText(info.getProfession());
                tvDOB.setText(info.getDate());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
        downloadImage();

        return root;
    }

    private void openImage() {
        CropImage.startPickImageActivity(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            imageuri = CropImage.getPickImageResultUri(getContext(), data);
            startCrop(imageuri);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                croppedImageUri=result.getUri();
                uploadImage();


            }
        }
    }

    private void startCrop(Uri croppedImageUri) {
        CropImage.activity(croppedImageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setMultiTouchEnabled(true)
                .start(getContext(), this);
    }


    private void uploadImage() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Uploading...");
        pd.show();
        if (croppedImageUri != null) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("users").child(uid);

            storageReference.putFile(croppedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            firebaseDatabase.getInstance().getReference().child("profilephoto").child(uid).child("URLIMAGE").setValue(url);
                            Log.d("DownloadUrl", url);
                            pd.dismiss();

                            Toast.makeText(getActivity(), "Image Upload Successfully!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
   public void downloadImage(){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                photuuu pho=snapshot.getValue(photuuu.class);
                if(pho!=null) {
                    Picasso.with(getContext()).load(String.valueOf(pho.getURLIMAGE())).into(imageView3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), (CharSequence) error,Toast.LENGTH_SHORT).show();

            }
        });
    }


}