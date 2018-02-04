package com.app.danny.neiuber.authentication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.danny.neiuber.Navigation;
import com.app.danny.neiuber.R;
import com.app.danny.neiuber.classes_without_activity.Route;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/*In this class we are trying to accomplish these things:
* Login successfully(If the user is already registered)
* Send to Register Activity(if the user is not registered
* Send to Profile(if the user logs in with the right credentials
*
* */
public class Login extends AppCompatActivity {

    private Button btnLogin;
    private TextView phoneNumber;//user will input phone number
    private String phoneNumberToString;//convert entered phone number to string
    private   HashMap<String,String> hashMapOfUserInfo;//we will map user data to send to other activities(after successful login)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        phoneNumber = (TextView) findViewById(R.id.lPhoneNumber);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                phoneNumberToString = phoneNumber.getText().toString();
                new ConnectAndLogin().execute();
            }
        });
    }



    //Resposible for checking if user's phone number exists in the database
    private class ConnectAndLogin extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("http://"+ Route.curentRoute+"/neiuber/public/index.php/api/login/" + phoneNumberToString+"/driver");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept","application/json");

                //Response from the POST request
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                String s = stringBuilder.toString();
                return stringBuilder.toString();
            }
            catch (Exception  e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

           //if the REST call is successful, we will be able to parse user info into a hashmap
            try {
                JsonArray res = new JsonParser().parse(result).getAsJsonArray();
                if(res.size() > 0){
                    JsonObject arr = res.get(0).getAsJsonObject();

                    Gson gson = new Gson();
                    Type stringStringMap = new TypeToken<HashMap<String, String>>(){}.getType();
                    hashMapOfUserInfo =(HashMap<String, String>) gson.fromJson(arr.toString(), stringStringMap);
                    /**********HashMap keys****************
                    * DriverId
                    * PhoneNumber
                    * FirstName
                    * LastName":"Mike",
                    * Gender
                    * Email
                    * Address
                    * Apt
                    * CitY
                    * State
                    * Zipcode
                    * DriverLicense
                    *
                    * */
                    saveSharedPreference();
                     sendToProfile();

                }else{//the phone number is not recognized so we send the user to registration page
                    sendToRegister();
                }

            }catch (JsonParseException e ){
                Log.d("error",e.getMessage());
            }
        }
    }

    /*save the user info so that when user closes the app, the information is stored
    and will be retrieved when the app is reopened util user logs out completely
     */
    public void saveSharedPreference(){
        Gson gson = new Gson();
        String hashMapString = gson.toJson(hashMapOfUserInfo);
        SavedUserInfo.setUserInfo(getApplicationContext(), hashMapString);
    }

    private void sendToProfile() {

        Intent intent = new Intent(Login.this, Navigation.class);
        intent.putExtra("driver_info",hashMapOfUserInfo);

        startActivity(intent);
    }

    private void sendToRegister() {
        Intent intent = new Intent(Login.this, Register.class);
        intent.putExtra("driver_phone",phoneNumberToString);
        startActivity(intent);

    }
}
