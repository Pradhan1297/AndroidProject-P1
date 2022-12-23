package com.example.project1cs478;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button enterPhoneNumberButton;
    Button openDialerButton;
    boolean stateChangeFlag = true;//stateChangeFlag ensures the phone number is not lost when the orientation of
    // the device is changed and the focus is on main activity
    boolean dialerButtonState; // stores the state of the openDialerButton in order to enable/disable this button
    int ReqCode; //keeps track of the Request Code throughout the changes that happen in the configuration of the application
    int ResCode; //keeps track of the Response Code throughout the changes that happen in the configuration of the application
    Intent dat; // keeps track of the Intent data throughout the changes that happen in the configuration of the application


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialerButtonState = false;
        enterPhoneNumberButton = findViewById(R.id.button);
        openDialerButton = findViewById(R.id.button2);

        openDialerButton.setEnabled(dialerButtonState); //openDialerButton is initially disabled
        if (savedInstanceState != null) {
            openDialerButton.setEnabled(savedInstanceState.getBoolean("dialerButtonState"));
            if (!savedInstanceState.getBoolean("stateChangeFlag")) {
                onActivityResult(savedInstanceState.getInt("ReqCode"),savedInstanceState.getInt("ResCode"),savedInstanceState.getParcelable("IntentData"));//onActivityResult is called
                // whenever the configuration of the app changes
            }
        }
        enterPhoneNumberButton.setOnClickListener(new View.OnClickListener() { //Defined OnClickListener for enterPhoneNumberButton
            @Override
            public void onClick(View view) {
                openPhoneNumberActivity();
            } //openPhoneNumberActivity() is called on clicking the enterPhoneNumberButton
        });
    }

    public void openPhoneNumberActivity() {
        Intent mainActivityIntent = new Intent(this, PhoneNumberActivity.class);
        startActivityForResult(mainActivityIntent, 1); //starts the PhoneNumberActivity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ReqCode = requestCode;
        ResCode = resultCode;
        dat = data;
        stateChangeFlag = false;
        dialerButtonState = true;
        openDialerButton.setEnabled(dialerButtonState);
        if (requestCode == 1 ) {
            String phoneNumber = data.getStringExtra("contactNumber");// Gets the phone number from the PhoneNumberActivity
            if (resultCode == RESULT_OK) {
                openDialerButton.setOnClickListener(new View.OnClickListener() { //Defined OnClickListener for openDialerButton
                    @Override
                    public void onClick(View view) {
                        Intent dialerIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + phoneNumber));// An instance of Implicit Intent with Action - ACTION.DIAL and scheme - tel is created
                        startActivity(dialerIntent); //Starts the dialer activity
                    }
                });
            } else {
                openDialerButton.setOnClickListener(new View.OnClickListener() { //Defined OnClickListener for openDialerButton
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this,
                                "Number entered is incorrect" + " " + phoneNumber,
                                Toast.LENGTH_LONG).show(); //Toast message is displayed to inform the device user that an incorrect number was entered
                    }
                });
            }
        }
    }
        @Override
        protected void onSaveInstanceState (@NonNull Bundle outState){ //onSaveInstanceState method saves the state of the variables during the change in configuration of the application
            super.onSaveInstanceState(outState);
            outState.putBoolean("stateChangeFlag", stateChangeFlag);
            outState.putBoolean("dialerButtonState", dialerButtonState);
            outState.putInt("ResCode",ResCode);
            outState.putInt("ReqCode",ReqCode);
            outState.putParcelable("IntentData",dat);

        }
    }
