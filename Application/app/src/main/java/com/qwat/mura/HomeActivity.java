package com.qwat.mura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonIncidents;
    private Button buttonTransact;
    private Button buttonAccountInformation;
    private Button buttonProofOfRes;
    private Button buttonEmergency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttonIncidents = (Button) findViewById(R.id.buttonIncidents);
        buttonTransact =(Button) findViewById(R.id.buttonTransact);
        buttonProofOfRes = (Button) findViewById(R.id.buttonProofOfRes);
        buttonAccountInformation = (Button) findViewById(R.id.buttonAccountInformation);
        buttonEmergency = (Button) findViewById(R.id.buttonEmergency);

        buttonIncidents .setOnClickListener(this);
        buttonTransact.setOnClickListener(this);
        buttonProofOfRes.setOnClickListener(this);
        buttonAccountInformation.setOnClickListener(this);
        buttonEmergency.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonIncidents){
            //Report an incident
            finish();
            startActivity(new Intent(this, IncidentActivity.class));
        }

        if (view == buttonProofOfRes){
            //Request proof of residence
            finish();
            startActivity(new Intent(this, ProofOrResActivity.class));
        }

        if (view == buttonAccountInformation){
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }

        if (view == buttonEmergency){
            finish();
            startActivity(new Intent(this, EmergencyActivity.class));
        }
    }
}
