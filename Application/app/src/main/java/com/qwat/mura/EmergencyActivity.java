package com.qwat.mura;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class EmergencyActivity extends AppCompatActivity {

    private static final int REUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        Button buttonHome = (Button) findViewById(R.id.buttonHome);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmergencyActivity.this, HomeActivity.class));
            }
        });

    }

    private void makeFonePhoneCallPolice(){
        String munNumber = "0123456789";

        if (ContextCompat.checkSelfPermission(EmergencyActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EmergencyActivity.this,
                    new String[] {Manifest.permission.CALL_PHONE}, REUEST_CALL);
        }
        else {
            String dial = "tel:" + munNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    private void makeFonePhoneCallMun(){
        String munNumber = "10111";

        if (ContextCompat.checkSelfPermission(EmergencyActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EmergencyActivity.this,
                    new String[] {Manifest.permission.CALL_PHONE}, REUEST_CALL);
        }
        else {
            String dial = "tel:" + munNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makeFonePhoneCallPolice();
                makeFonePhoneCallMun();
            }
            else{
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
