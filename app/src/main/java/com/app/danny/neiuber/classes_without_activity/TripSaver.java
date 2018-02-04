package com.app.danny.neiuber.classes_without_activity;

import android.os.AsyncTask;
import android.util.Log;

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
import java.util.HashMap;
import java.util.List;

/**
 * Created by danny on 1/13/18.
 * After a ride is completed we create and object with a map as a parameter to save to ride history
 */

public class TripSaver {

    HashMap<String, String> hashMapOfTrip;

    public TripSaver(HashMap<String, String> hashMapOfTrip) {
        this.hashMapOfTrip = hashMapOfTrip;
    }

    public void setHashMapOfTrip(HashMap<String, String> hashMapOfTrip) {
        this.hashMapOfTrip = hashMapOfTrip;
    }

    public String saveTripToDatabase(){
        try{
            String s2 =  new SaveTripToDatabase().execute().get().toString();
            return  s2;
        }catch(Exception e){
            return e.getMessage();
        }

    }



    private class SaveTripToDatabase extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://" + Route.curentRoute + "/neiuber/public/index.php/api/save_ride_history");
            List<BasicNameValuePair> nvps = new ArrayList<>();
            BasicNameValuePair start_trip = new BasicNameValuePair("starting_location", hashMapOfTrip.get("startingLocation"));
            BasicNameValuePair  end_trip = new BasicNameValuePair("ending_location",hashMapOfTrip.get("endingLocation"));
            BasicNameValuePair driver_phone = new BasicNameValuePair("driver_phone", hashMapOfTrip.get("driverPhone"));
            BasicNameValuePair passenger_phone = new BasicNameValuePair("passenger_phone", hashMapOfTrip.get("passengerPhone"));
            BasicNameValuePair total_miles = new BasicNameValuePair("total_miles", hashMapOfTrip.get("totalMiles"));
            BasicNameValuePair total_fare = new BasicNameValuePair("total_fare", hashMapOfTrip.get("totalFare"));
            BasicNameValuePair trip_date = new BasicNameValuePair("trip_date", hashMapOfTrip.get("tripDate"));
            BasicNameValuePair start_time = new BasicNameValuePair("start_time", hashMapOfTrip.get("startTime"));
            BasicNameValuePair end_time = new BasicNameValuePair("end_time", hashMapOfTrip.get("endTime"));



            nvps.add(start_trip);
            nvps.add(end_trip);
            nvps.add(driver_phone);
            nvps.add(passenger_phone);
            nvps.add(total_miles);
            nvps.add(total_fare);
            nvps.add(trip_date);
            nvps.add(start_time);
            nvps.add(end_time);


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
        public void onPostExecute(String result) {
            super.onPostExecute(result);

        }

    }
}
