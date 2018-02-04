package com.app.danny.neiuber.authentication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by danny on 1/23/18.
 */
/*This class is responsible for validating user input including(but not limited to) phone number, email, address
* we validate by using both java's regex library and android's Patterns library
* */
public class InputValidator {

    public InputValidator() {

    }


    public boolean validatePhoneNumber(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

   /* public boolean validateEmail(String email) {
        return emailValidator(email);
    }*/

    public boolean validatePassword() {
        return true;
    }

    //responsible for sending SMS to user's phone to validate phone number's existense
    public void sendSMS(String phone, String message, Context context) {

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, message, null, null);
            Toast.makeText(context, "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

    }
}



