package com.qwat.mura;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProofOrResActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int STORAGE_CODE = 1000;
    private FirebaseAuth firebaseAuth;

    private Button buttonLogout;
    private Button buttonHome;

    private DatabaseReference databaseReference;

    private EditText editTextName, editTextAddress, editTextReason, editTextIDNumber;
    private Button buttonReuestProofOfRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proof_or_res);

        firebaseAuth = firebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            //user not logged in
            finish();
            startActivity(new Intent(this, loginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextIDNumber = (EditText) findViewById(R.id.editTextIDNumber);
        editTextReason = (EditText) findViewById(R.id.editTextReason);
        buttonReuestProofOfRes = (Button) findViewById(R.id.buttonReuestProofOfRes);
        buttonHome = (Button) findViewById(R.id.buttonHome);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        //textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);

        //textViewUserEmail.setText("Welcome " +user.getEmail());
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        //adding listener to button
        buttonReuestProofOfRes.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);
        buttonHome.setOnClickListener(this);
    }

    private void ProofOfResInformation(){
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String RequestReason = editTextIDNumber.getText().toString().trim();
        String IDNumber = editTextReason.getText().toString().trim();

        ProofOfResInformation proofOfResInformation = new ProofOfResInformation(name, address, RequestReason, IDNumber);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(proofOfResInformation);

        Toast.makeText(this, "Information saved", Toast.LENGTH_SHORT).show();
    }

    private void savePdf() {
        //create object of Document class
        Document mDoc = new Document();
        //pdf filename
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //pdf file path
        String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";

        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            //open the document for writting
            mDoc.open();
            //get text from editText
            String mTextName = editTextName.getText().toString().trim();
            String mTextAddress = editTextAddress.getText().toString().trim();
            String mTextIDNumber = editTextIDNumber.getText().toString().trim();
            String mTextReason = editTextReason.getText().toString().trim();

            //add author of the document
            mDoc.addAuthor("Tshwane Municipality");
            mDoc.addCreationDate();
            mDoc.addTitle("Proof Of Residence");

            //add body text
            mDoc.add(new Paragraph("                      Poof Of Residence\n\n\n\n"));
            mDoc.add(new Paragraph("Full names: "+mTextName+"\n"));
            mDoc.add(new Paragraph("Identity Number: "+mTextIDNumber+"\n\n\n"));
            mDoc.add(new Paragraph("Physical Address: "+mTextAddress+"\n\n"));
            mDoc.add(new Paragraph("Reason: "+mTextReason+"\n"));
            mDoc.add(new Paragraph("Date: "+mFileName));

            //close the document
            mDoc.close();
            //show message that the file is saved, show filename and the path
            Toast.makeText(this, mFileName+".pdf\nis saved to\n" + mFilePath, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            //if anything goes wrong causing exception, get and show exception message
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonReuestProofOfRes){
            ProofOfResInformation();
            //handle runtime permission for devices with marshmellow and above
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                //system OS >= Marshmellow(6.0), check if permission is enabled or not
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED){
                    //permission not granted, request write permision
                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, STORAGE_CODE);
                }
                else {
                    //permission already granted, save pdf method
                    savePdf();

                }
            }
            else {
                //OS < Marshmellow(6.0), not required to check runtime permision. Call save pdf method
                savePdf();
            }


        }

        if (view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, loginActivity.class));
        }

        if (view == buttonHome){
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }
    }


    //handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission was granted from popup, call sevepdf method
                }
                else {
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
