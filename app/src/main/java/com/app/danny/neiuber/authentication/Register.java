package com.app.danny.neiuber.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.danny.neiuber.R;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*This class is rsposible for registering a new user

* */
public class Register extends Activity {

    private TextView rPhoneNumber,rFirstName,
            rLastName,rEmail,rAddress1,rAddress2,rCity,rState,rZipcode,rLicense;

    private RadioButton rGenderM, rGenderF;

    private HashMap<String, String> register_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        Intent in = new Intent();
        String phone_extra = in.getStringExtra("driver_phone");



        rPhoneNumber = (TextView)findViewById(R.id.rPhoneNumber);
        rFirstName = (TextView)findViewById(R.id.rFirstName);
        rLastName = (TextView)findViewById(R.id.rLastName);
        rEmail = (TextView)findViewById(R.id.rEmail);
        rAddress1 = (TextView)findViewById(R.id.rAddress1);
        rAddress2 = (TextView)findViewById(R.id.rAddress2);
        rCity = (TextView)findViewById(R.id.rCity);
        rState = (TextView)findViewById(R.id.rState);
        rZipcode = (TextView)findViewById(R.id.rZipcode);
        rLicense = (TextView)findViewById(R.id.rLicense);
        rGenderM = (RadioButton)findViewById(R.id.rGenderM);
        rGenderF = (RadioButton)findViewById(R.id.rGenderF);


        rPhoneNumber.setText(phone_extra);


        Button btnRegister = (Button)findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register_info = new HashMap<String, String>();
                register_info.put("phone",rPhoneNumber.getText().toString());
                register_info.put("first_name",rFirstName.getText().toString());
                register_info.put("last_name",rLastName.getText().toString());
                register_info.put("email",rEmail.getText().toString());
                register_info.put("address1",rAddress1.getText().toString());
                register_info.put("address2",rAddress2.getText().toString());
                register_info.put("city",rCity.getText().toString());
                register_info.put("state",rState.getText().toString());
                register_info.put("zipcode",rZipcode.getText().toString());
                register_info.put("license",rLicense.getText().toString());

                String gender = "M";

                if(rGenderM.isChecked()){
                    gender = "F";
                }

                register_info.put("sex",gender);

                new SaveNewUserInfoIntoDatabase().execute();

            }
        });
    }


    private class SaveNewUserInfoIntoDatabase extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://172.31.98.57/neiuber/public/index.php/api/driver/register");
            List <BasicNameValuePair> nvps = new ArrayList <>();
            BasicNameValuePair phone = new BasicNameValuePair("phone",register_info.get("phone"));
            BasicNameValuePair first_name = new BasicNameValuePair("first_name",register_info.get("first_name"));
            BasicNameValuePair last_name = new BasicNameValuePair("last_name", register_info.get("last_name"));
            BasicNameValuePair email = new BasicNameValuePair("email", register_info.get("email"));
            BasicNameValuePair address1 = new BasicNameValuePair("address", register_info.get("address1"));
            BasicNameValuePair address2 = new BasicNameValuePair("apt", register_info.get("address"));
            BasicNameValuePair city = new BasicNameValuePair("city", register_info.get("city"));
            BasicNameValuePair state = new BasicNameValuePair("state", register_info.get("state"));
            BasicNameValuePair zipcode = new BasicNameValuePair("zipcode", register_info.get("zipcode"));
            BasicNameValuePair license= new BasicNameValuePair("license", register_info.get("license"));
            BasicNameValuePair gender = new BasicNameValuePair("gender", register_info.get("gender"));


            nvps.add(phone);
            nvps.add(first_name);
            nvps.add(last_name);
            nvps.add(email);
            nvps.add(address1);
            nvps.add(address2);
            nvps.add(city);
            nvps.add(state);
            nvps.add(zipcode);
            nvps.add(license);
            nvps.add(gender);



            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                CloseableHttpResponse response2 = httpclient.execute(httpPost);
                HttpEntity entity2 = response2.getEntity();

                BufferedReader rd = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

                return  result.toString().trim();

            } catch (Exception e){
                Log.v("posting error", e.toString());
                return  e.toString();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            try {

                JSONArray res = new JSONArray(result);


            }catch (Exception e){
                Log.d("error",e.getMessage());
            }
        }
    }


}
