package com.qwat.mura;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.nfc.Tag;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;

public class IncidentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //declare spinner
    public Spinner spinner;
    //dropdown items
    private static final String[] paths = {"Pipe burst", "Pothole", "Electric cable",};
    private String StrIncident;

    //view declarations
    private Button buttonCaptureImage;
    private Button buttonReportIssue;
    private ImageView imageViewIssue;
    private EditText editTextIssueDetails;
    private Button buttonLogout;
    private Button buttonHome;

    private static final int CAMERA_REUEST_CODE = 9999;

    private StorageReference mStorage;

    private ProgressDialog mprogressDialog;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);

        mStorage = FirebaseStorage.getInstance().getReference();

        mprogressDialog = new ProgressDialog(this);

        buttonHome = (Button) findViewById(R.id.buttonHome);

        //spinner adapter
        spinner = (Spinner)findViewById(R.id.spinnerIncidentType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncidentActivity.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        buttonCaptureImage = (Button) findViewById(R.id.buttonCaptureImage);
        buttonReportIssue = (Button) findViewById(R.id.buttonReportIssue);
        editTextIssueDetails = (EditText) findViewById(R.id.editTextIssueDetails);
        imageViewIssue = (ImageView) findViewById(R.id.imageViewIssue);



        buttonCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, CAMERA_REUEST_CODE);


            }
        });

        buttonReportIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //report the issue
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IncidentActivity.this, HomeActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REUEST_CODE && requestCode == RESULT_OK &&
                data != null && data.getData() != null){
            mprogressDialog.setTitle("Uploading Image...");
            mprogressDialog.show();

            uri = data.getData();

            StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
            imageViewIssue.setImageURI(uri);

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mprogressDialog.dismiss();
                    Toast.makeText(IncidentActivity.this, "Finished uploading...", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mprogressDialog.dismiss();
                    Toast.makeText(IncidentActivity.this, "Couldn't upload...", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mprogressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        StrIncident = parent.getItemAtPosition(position).toString();
        Log.d("Incident Type", StrIncident);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.i("Nothing Selected", "Select Incident type");
    }

}
