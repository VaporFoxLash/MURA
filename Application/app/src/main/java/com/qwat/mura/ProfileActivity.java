package com.qwat.mura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button buttonHome;

    private DatabaseReference databaseReference;

    private EditText editTextName, editTextAddress, editTextGender, editTextAge, editTextMobileNumber;
    private Button buttonAdduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = firebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            //user not logged in
            finish();
            startActivity(new Intent(this, loginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextGender = (EditText) findViewById(R.id.editTextGender);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextMobileNumber = (EditText) findViewById(R.id.editTextNumber);
        buttonAdduser = (Button) findViewById(R.id.buttonAddUser);
        buttonHome = (Button) findViewById(R.id.buttonHome);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        //textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);

        //textViewUserEmail.setText("Welcome " +user.getEmail());
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        //adding listener to button
        buttonLogout.setOnClickListener(this);
        buttonAdduser.setOnClickListener(this);
        buttonHome.setOnClickListener(this);
    }

    private void saveUserInformation(){
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String Gender = editTextGender.getText().toString().trim();
        String Age = editTextAge.getText().toString().trim();
        String mobileNumber = editTextMobileNumber.getText().toString().trim();


        UserInformation userInformation = new UserInformation(name, address, Gender, Age, mobileNumber);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(userInformation);

        Toast.makeText(this, "Information saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view == buttonAdduser){
            saveUserInformation();
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
}
