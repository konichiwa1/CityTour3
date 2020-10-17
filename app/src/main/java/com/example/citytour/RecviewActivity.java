package com.example.citytour;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static android.content.ContentValues.TAG;

public class RecviewActivity extends AppCompatActivity {

    private Button photos;
    private Button experience;
    private Button costs;
    private String photoFolder;
    //private Button experiences = (Button) findViewById(R.id.experiences);
    //private Button costs = (Button) findViewById(R.id.costs);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recview);
        photos = (Button) findViewById(R.id.photos);
        experience = (Button) findViewById(R.id.experiences);
        costs = (Button) findViewById(R.id.costs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.ablrecv);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                photoFolder= null;
            } else {
                photoFolder= extras.getString("PastTripParticular");
                //getActionBar().setTitle(photoFolder);
                toolbar.setTitle(photoFolder);
                toolbar.inflateMenu(R.menu.menurecv);
                setSupportActionBar(toolbar);
            }
        } else {
            photoFolder= (String) savedInstanceState.getSerializable("PastTripParticular");

            toolbar.setTitle(photoFolder);
            toolbar.inflateMenu(R.menu.menurecv);
            setSupportActionBar(toolbar);
        }

        Log.d(TAG, "onCreate: (intent************************)"+photoFolder);

        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhoto();
            }
        });

        experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExperiences();
            }
        });

        costs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCosts();
            }
        });
    }

    public void openPhoto() {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("PastTripPhotoParticular",photoFolder);
        startActivity(intent);
    }

    public void openExperiences()   {
        Intent intent = new Intent(this, ExpActivity.class);
        intent.putExtra("PastTripExpParticular", photoFolder);
        startActivity(intent);
    }

    public void openCosts() {
        Intent intent = new Intent(this, CostPastTripActivity.class);
        intent.putExtra("PastTripCostParticular", photoFolder);
        startActivity(intent);
    }
}
