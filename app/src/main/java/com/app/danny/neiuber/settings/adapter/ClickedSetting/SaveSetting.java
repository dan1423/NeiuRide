package com.app.danny.neiuber.settings.adapter.ClickedSetting;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.app.danny.neiuber.classes_without_activity.Route;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danny on 1/20/18.
 */

public class SaveSetting {

    private String phoneNumber;
    Context c;
    private String stuff;
    public SaveSetting(String phoneNumber, Context c) {
        this.phoneNumber = phoneNumber;
        this.c = c;
    }

    public void saveEmail(String email) {
        new UpdateSetting().execute(email, "email");

    }
    public void  savePhoneNUmber() {
            new UpdatePhoneNumber().execute();

    }

    public void savePassword(String password) {

    }

    public void saveHomeAddress(String homeAddress) {

    }

    private class UpdateSetting extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String setting = params[0];
            String settingType = params[1];

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://" + Route.curentRoute + "/neiuber/public/index.php/api/update_setting");
            List<BasicNameValuePair> nvps = new ArrayList<>();
            BasicNameValuePair set = new BasicNameValuePair("setting", setting);
            BasicNameValuePair type = new BasicNameValuePair("setting_type", settingType);
            BasicNameValuePair phone = new BasicNameValuePair("phone", phoneNumber);
            nvps.add(set);
            nvps.add(type);
            nvps.add(phone);

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
                return result.toString().trim();

            } catch (Exception e) {
                Log.v("posting error", e.toString());
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(c,s,Toast.LENGTH_LONG).show();
        }
    }

    private class UpdatePhoneNumber extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://" + Route.curentRoute + "/neiuber/public/index.php/api/update_phone_number");
            List<BasicNameValuePair> nvps = new ArrayList<>();

            BasicNameValuePair phone = new BasicNameValuePair("phone", phoneNumber);
            nvps.add(phone);

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
                return result.toString().trim();

            } catch (Exception e) {
                Log.v("posting error", e.toString());
                return e.getMessage().toString();
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(c,s,Toast.LENGTH_LONG).show();


        }
    }





}
