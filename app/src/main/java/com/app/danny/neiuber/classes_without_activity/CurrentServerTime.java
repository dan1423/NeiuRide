package com.app.danny.neiuber.classes_without_activity;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by danny on 1/14/18.
 * This class is responsible for getting the server time of the user's location
 * This is to avoid complications of using the user's phone time as it can result in the wrong information
 */

public class CurrentServerTime {
    private String timeString = "";

    public CurrentServerTime() {

    }

    public String getCurrentTime() {
        try {
            timeString = new GetServerTime().execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return timeString;
    }

    //Sends request to server to get time
    private class GetServerTime extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(new HttpGet("https://google.com/"));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    String dateStr = response.getFirstHeader("Date").getValue();

                    return dateStr;

                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            }catch (ClientProtocolException e) {
                Log.d("Response", e.getMessage());
            }catch (IOException e) {
                Log.d("Response", e.getMessage());
            }
            return "";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }

}
