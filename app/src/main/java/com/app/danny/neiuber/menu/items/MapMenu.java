package com.app.danny.neiuber.menu.items;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.danny.neiuber.DriverLocation;
import com.app.danny.neiuber.classes_without_activity.CurrentServerTime;
import com.app.danny.neiuber.R;
import com.app.danny.neiuber.classes_without_activity.Route;
import com.app.danny.neiuber.classes_without_activity.TripSaver;
import com.app.danny.neiuber.classes_without_activity.TimeFormatter;
import com.app.danny.neiuber.classes_without_activity.TripCalculation;
import com.app.danny.neiuber.dialog_fragments.DialogObject;
import com.app.danny.neiuber.dialog_fragments.DialogObjectSetter;
import com.app.danny.neiuber.dialog_fragments.RideRequestDialog;
import com.app.danny.neiuber.dialog_fragments.TripDialogFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
//import com.google.android.gms.location.LocationListener;


/**
 * Created by danny on 11/9/17.
 */

/*
/in this class driver will be able to go online and offline as pleased
on button click, driver will trigger query to delete or add to available_drivers(table in database)
if driver goes offline, we have to stop updating location and checking for request
we only check for requests if we are already online
need to delete user from available drivers when the app closes
dealing with ride history object, wondering if constructor has too many arguments
*fix dialog fragments during trip
 */

public class MapMenu extends Fragment
        implements  OnMapReadyCallback,LocationListener, RideRequestDialog.AcceptRequestListener, TripDialogFragment.ChangeDialog{
    GoogleMap gMap;
    MapView mMapView;
    View mView, requestView,tripView;

    LocationManager locationManager;
    LocationListener locationListener;
    Button btndriverStatus, btnEnd;
    public double currentLat = 0.0;
    public double currentLng = 0.0;
    protected Marker posMarker;
    private boolean isOnline = false;
    private String rideStartTime = "";
    private String rideEndTime = "";
    private TextView textPassengerName, textRequestDistance, textRequestHeading, textRequestTimer;
    private ImageView  showTripDialog;
    int countMarkerUpdate = 0;
    HashMap<String, String> hashMapOfUserInfo;
    HashMap  <String, String> hashMapOfRideRequest;


    int lc = 1;
    int la = 1;

    private Timer locationUpdateTimer;//updates location

    boolean driverAccept = false;
    DialogObject[] arayOfDialogObjects = new DialogObject[4];
    int counterForArrayOfDialogObjects = 0;
    private TripDialogFragment tdf;




    public MapMenu() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hashMapOfUserInfo = (HashMap<String,String>) getActivity().getIntent().getSerializableExtra("driver_info");
        runOnApplicationLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.map_layout, container, false);
        btndriverStatus = (Button) mView.findViewById(R.id.btnDriverStatus);
        btnEnd = (Button) mView.findViewById(R.id.btnEndRide);
        showTripDialog =(ImageView) mView.findViewById(R.id.showTripDialog);
        showTripDialog.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
             tdf.getDialog().show();
            }
        });

        setButtonFunctions();

        return mView;
    }

    public void setButtonFunctions() {
       btnEnd.setVisibility(View.GONE);
        showTripDialog.setVisibility(View.GONE);

        btndriverStatus.setText("Offline");
        //driver is automatically offline at startup, but will toggle based on button click
        btndriverStatus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isOnline){//driver will be going offline
                    handleGoingOffline();
                }else{//driver will be going online
                    handleGoingOnline();
                }

            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                CurrentServerTime tm = new CurrentServerTime();
                rideEndTime = tm.getCurrentTime();
                afterRideHasFinished();
            }
        });

    }

    public void  handleGoingOffline(){
        btndriverStatus.setText("Offline");
        isOnline = false;
        //Stop updating location, since user is going offline
        locationUpdateTimer.cancel();
        locationUpdateTimer.purge();
        new GoOffline().execute();
    }
    public void handleGoingOnline(){
        btndriverStatus.setText("Online");
        isOnline = true;
        new GoOnline().execute();
        //after user goes online, start updating location, and checking passenger request
        locationUpdateTimer = new Timer();
        TimerTask updateTask =  new TimerTask() {
            @Override
            public void run() {
                new UpdateLocationInDatabase().execute();//update latitude and longitude of driver in interval
                new CheckForPassengerRequest().execute();//check for passenger request in interval

            }};
        locationUpdateTimer.schedule( updateTask, 0L ,5000L);//schedule updating location and checking passenger every 5 seconds
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        gMap = googleMap;
        LatLng latlng = new LatLng(41.992541, -87.689902);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(latlng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);

        posMarker = gMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title("Chicago"));

        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);

    }


    private void runOnApplicationLoad() {

        final Criteria criteria = new Criteria();

        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setSpeedRequired(true);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //Acquire a reference to the system Location Manager

        locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {

            public void onLocationChanged(Location newLocation) {
                //gets position
                currentLat = newLocation.getLatitude();
                currentLng = newLocation.getLongitude();
            }

            //not entirely sure what these do yet
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }

        };
        updateLocationOnMap();
    }

    private void updateLocationOnMap() {

        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    10);
        } else {
            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Double dLon = 0.0;
            Double dLat = 0.0;

            while(dLon == null){
                 dLon = (Double) loc.getLongitude();
                 dLat = (Double) loc.getLatitude();
            }
            currentLat = loc.getLatitude();
            currentLng = loc.getLongitude();

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        updateLocationOnMap();
    }

    @Override
    public void onPause() {
        locationManager.removeUpdates(this);
        super.onPause();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    //as user moves, we want to reset the marker to his location
    public void updateMapMarker() {
        posMarker.remove();
        LatLng ll = new LatLng(41.994888, -87.690106);
        posMarker = gMap.addMarker(new MarkerOptions()
                .position(ll)
                .title("UpdateCount: " + countMarkerUpdate));
        countMarkerUpdate++;
    }
    public void testMapStuff() {
        getActivity().startService(new Intent(getActivity(), DriverLocation.class));
    }

    private class GoOnline extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://" + Route.curentRoute + "/neiuber/public/index.php/api/driver/handle_driver_status");
            List<BasicNameValuePair> nvps = new ArrayList<>();
            BasicNameValuePair phone = new BasicNameValuePair("phone", hashMapOfUserInfo.get("PhoneNumber"));
            BasicNameValuePair option = new BasicNameValuePair("option", "online");

            nvps.add(phone);
            nvps.add(option);

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


    private class GoOffline extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {


            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://" + Route.curentRoute + "/neiuber/public/index.php/api/driver/handle_driver_status");
            List<BasicNameValuePair> nvps = new ArrayList<>();
            BasicNameValuePair phone = new BasicNameValuePair("phone",hashMapOfUserInfo.get("PhoneNumber"));
            BasicNameValuePair option = new BasicNameValuePair("option", "offline");

            nvps.add(phone);
            nvps.add(option);

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

        //updating driver location in a set interval until he is offline
        private class UpdateLocationInDatabase extends AsyncTask<String, Integer, String> {

            @Override
            protected String doInBackground(String... params) {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost("http://" + Route.curentRoute + "/neiuber/public/index.php/api/driver/update_driver_location");
                List<BasicNameValuePair> nvps = new ArrayList<>();
                BasicNameValuePair phone = new BasicNameValuePair("phone", hashMapOfUserInfo.get("PhoneNumber"));
                BasicNameValuePair latitude = new BasicNameValuePair("latitude", lc + "");
                BasicNameValuePair longitude = new BasicNameValuePair("longitude", la + "");
                lc++;
                la++;
                nvps.add(phone);
                nvps.add(latitude);
                nvps.add(longitude);

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
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.i("Result of request:", result);
                //updateMapMarker();
            }
        }

        //Once the driver goes on, we will periodically check for passenger request
        private class CheckForPassengerRequest extends AsyncTask<String, Integer, String> {

            @Override
            protected String doInBackground(String... params) {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost("http://" + Route.curentRoute + "/neiuber/public/index.php/api/driver/check_for_ride_request");
                List<BasicNameValuePair> nvps = new ArrayList<>();
                BasicNameValuePair phone = new BasicNameValuePair("phone", hashMapOfUserInfo.get("PhoneNumber"));//send driver phone number and check if there is a request
                nvps.add(phone);
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                    CloseableHttpResponse response2 = httpClient.execute(httpPost);
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
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (!result.trim().isEmpty()) {
                    try {
                        JsonArray res = new JsonParser().parse(result).getAsJsonArray();
                        if (res.size() > 0) {
                            //Stop searching for passengers and updating location in database
                            locationUpdateTimer.cancel();
                            locationUpdateTimer.purge();

                            JsonObject arr = res.get(0).getAsJsonObject();

                            Gson gson = new Gson();
                            Type stringStringMap = new TypeToken<HashMap<String, String>>() {
                            }.getType();
                            hashMapOfRideRequest = (HashMap<String, String>) gson.fromJson(arr.toString(), stringStringMap);
                            showPassengerRequest();
                        }
                    } catch (JsonParseException e) {
                        Log.d("error", e.getMessage());
                    }

                }
            }
        }


    /******************************RIDE REQUEST SECTION**********************************************************/

     /*This method gets driver's response to passenger request.
    if driver accepts request, then accepted will be true
     */

     /******************OVERRIDE DIALOG INTERFACES**********************************/
    @Override
    public void onFinishRideRequestDialog(boolean accepted) {
        if(accepted){
            showTripDialog.setVisibility(View.VISIBLE);
            setArrayOfDialogObjects();
        }
        //set array of dialog objects
    }

    @Override
    public void onChangeDialog() {
        showDialog();
    }

    private void setArrayOfDialogObjects(){
       arayOfDialogObjects = new DialogObjectSetter( Double.valueOf(hashMapOfRideRequest.get("StartingLat")),
                                                Double.valueOf(hashMapOfRideRequest.get("StartingLng")),
                                                Double.valueOf(hashMapOfRideRequest.get("EndingLat")),
                                                 Double.valueOf(hashMapOfRideRequest.get("EndingLng")),
                                                hashMapOfRideRequest.get("PassengerName"),getContext()).getDialogs();


        showDialog();
    }

    private void showDialog(){

        if(counterForArrayOfDialogObjects < 4){
            FragmentManager fm = getFragmentManager();
            tdf = new TripDialogFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("current_dialog",arayOfDialogObjects[counterForArrayOfDialogObjects]);
            counterForArrayOfDialogObjects+=1;

            tdf.setArguments(bundle);
            tdf.setTargetFragment(MapMenu.this, 112);
            tdf.show(fm, "Trip");
        }

    }



    private void showPassengerRequest() {
            FragmentManager fm = getFragmentManager();
            RideRequestDialog rrd =  RideRequestDialog.newInstance("Ride Request");

            Bundle bundle = new Bundle();
            bundle.putString("name",hashMapOfRideRequest.get("PassengerName"));
            bundle.putString("distance","2.3mi");
            bundle.putString("heading","You have a request");

            rrd.setArguments(bundle);
            rrd.setTargetFragment(MapMenu.this, 112);
            rrd.show(fm, "Ride Request");
        }






    /*Method utilizes TimeFormatter class to convert start time, endtime to  needed formats(and calculates total ride time to minutes)
    * It then calls for ride history to be saved
    * */
       public void afterRideHasFinished() {
            Date dateStartTime = new Date(rideStartTime);
            Date dateEndTime =  new Date(rideEndTime);
            long longTime = TimeUnit.MILLISECONDS.toMinutes(dateEndTime.getTime() - dateStartTime.getTime());
            String totalTime = String.valueOf(longTime);
            TimeFormatter tf = new TimeFormatter();

            String startTimeFormatted = tf.timeToHoursMinutesSeconds(dateStartTime);
            String endTimeFormatted = tf.timeToHoursMinutesSeconds(dateEndTime);
            String currentDateFormatted = tf.timeToYearMonthDay(dateEndTime);

            HashMap<String, String> tripMap = new HashMap<>();
            tripMap.put("tripDate", new String(currentDateFormatted));
            tripMap.put("startTime", new String(startTimeFormatted));
            tripMap.put("endTime", new String(endTimeFormatted));

            calculateTrip(tripMap, totalTime);
        }

        public void calculateTrip(  HashMap<String, String> rideMap, String totalTime){
          //  Toast.makeText(getContext(),hashMapOfRideRequest.toString(),Toast.LENGTH_LONG).show();
            double sLat = Double.valueOf(hashMapOfRideRequest.get("StartingLat"));
            double sLng = Double.valueOf(hashMapOfRideRequest.get("StartingLng"));
            double eLat =    Double.valueOf(hashMapOfRideRequest.get("EndingLat"));
            double eLng = Double.valueOf(hashMapOfRideRequest.get("EndingLng"));

           TripCalculation trip = new TripCalculation(sLat, sLng, eLat, eLng,totalTime, getContext());
            while (trip.getStartingLocation().trim().length() < 1){
                trip.convertStartingLocation();
                trip.convertEndingLocation();
            }

            //combine tripCalculation,rideMap,UserInfoMap,hashMapOfRideRequest
            HashMap<String, String> historyMap = new HashMap<>();
            historyMap.put("driverPhone",hashMapOfUserInfo.get("PhoneNumber"));
            historyMap.put("passengerPhone", hashMapOfRideRequest.get("PassengerPhone"));
            historyMap.put("startingLocation",trip.getStartingLocation());
            historyMap.put("endingLocation",trip.getEndingLocation());
            historyMap.put("totalMiles",String.valueOf(trip.getTotalMiles()));
            historyMap.put("totalFare",String.valueOf(trip.getFare()));
            historyMap.put("tripDate",rideMap.get("tripDate"));
            historyMap.put("startTime",rideMap.get("startTime"));
            historyMap.put("endTime",rideMap.get("endTime"));

           saveRideHistory(historyMap);
        }


        public void saveRideHistory(HashMap<String,String> historyMap){
            TripSaver tripSaver = new TripSaver(historyMap);
           String sg=  tripSaver.saveTripToDatabase();
            Toast.makeText(getContext(), sg,Toast.LENGTH_LONG).show();
        }

    /*---------------------------DEFAULT OVERRIDDEN METHODS------------------------------------------*/
   @Override
    public void onLocationChanged(Location location) {
    }


    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    }

