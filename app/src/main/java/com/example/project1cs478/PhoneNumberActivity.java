package com.example.project1cs478;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class PhoneNumberActivity extends AppCompatActivity {
    EditText phoneEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

       phoneEditText = findViewById(R.id.editTextPhone2);

        phoneEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { //Defined OnEditorActionListener for EditText
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){ //Checks if the done or return key was clicked on the soft keyboard
                    String phoneNumber = phoneEditText.getText().toString(); //phoneNumber stores the phone number entered in the EditText
                    Intent phoneNumberActivityIntent = new Intent();
                    phoneNumberActivityIntent.putExtra("contactNumber",phoneNumber); //Adds phoneNumber(phone information) to the phoneNumberActivityIntent
                    if(phoneNumber.matches("^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$") || phoneNumber.matches("^\\d{10}$") && phoneNumber.length()<=14) //If condition, checks if the entered phone number matches the following formats : '(xxx) xxx-xxxx', 'xxxxxxxxxx'
                    {
                       setResult(RESULT_OK,phoneNumberActivityIntent); //If the entered phone number matches one of the two accepted formats then the result code is set to RESULT_OK
                    }else{
                        setResult(RESULT_CANCELED,phoneNumberActivityIntent);//If the entered phone number does not match any of the accepted formats the result code is set to RESULT_CANCELLED
                    }
                    finish(); //Closes the PhoneNumberActivity

                }
                return false;
            }
        });
    }
}