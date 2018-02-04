package com.app.danny.neiuber.menu.items;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.app.danny.neiuber.R;
import com.app.danny.neiuber.classes_without_activity.Route;
import com.app.danny.neiuber.ride_history.DetailedRideHistory;
import com.app.danny.neiuber.ride_history.adapter.RideHistoryAdapter;
import com.app.danny.neiuber.ride_history.adapter.RideHistoryItems;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by danny on 11/10/17.
 */

public class RideHistoryMenu extends Fragment{

    public RideHistoryMenu(){

    }
    private List<HashMap<String, String>> listOfUserInfo = new ArrayList<>();
    private View rideHistoryView;
    HashMap<String, String> mp = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = new Bundle();
        bundle = this.getArguments();

        if (bundle != null) {
            mp = (HashMap<String, String>) bundle.getSerializable("driver_info");
            new RideHistoryFromDatabase().execute();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rideHistoryView =  inflater.inflate(R.layout.ride_history_skeleton, container, false);
        return  rideHistoryView;
    }


    private class RideHistoryFromDatabase extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL("http://"+ Route.curentRoute+"/neiuber/public/index.php/api/ride_history/"+mp.get("PhoneNumber")+"/driver");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept","application/json");
                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                return stringBuilder.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JsonArray res = new JsonParser().parse(result).getAsJsonArray();

                if(!res.isJsonNull()){

                    for(int jsonObjIndex = 0; jsonObjIndex < res.size(); jsonObjIndex ++){
                        JsonObject arr = res.get(jsonObjIndex).getAsJsonObject();
                        String jsonToString = arr.toString();

                        Gson gson = new Gson();
                        Type stringStringMap = new TypeToken<HashMap<String, String>>(){}.getType();
                        HashMap<String, String> hm = gson.fromJson(jsonToString, stringStringMap);

                        listOfUserInfo.add(hm);
                    }
                    setHistory();
                }

            }catch (Exception e){
                Log.d("error",e.getMessage());
            }
        }
    }

    public void setHistory(){
        ListAdapter customListAdapter = new RideHistoryAdapter(getActivity(),getData());
        ListView customListView = (ListView) rideHistoryView.findViewById(R.id.list_history);
        customListView.setAdapter(customListAdapter);
        ArrayList<RideHistoryItems> cd = getData();

        customListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        sendToDetailedRideHistory(position);
                    }
                }
        );

    }

    private ArrayList getData() {

        ArrayList<RideHistoryItems> rideHistoryArrayList=new ArrayList<>();
        for(int i = 0; i < listOfUserInfo.size(); i++){

            RideHistoryItems rh = new RideHistoryItems();
            rh.setImage(R.drawable.prisonmike2);
            rh.setRideFare("$"+ listOfUserInfo.get(i).get("TotalFare"));
            rh.setRideTime(listOfUserInfo.get(i).get("TripDate"));
            rideHistoryArrayList.add(rh);
        }

        return rideHistoryArrayList;
    }

    private void sendToDetailedRideHistory(int indexOfMap){
        HashMap<String,String> detaileMap = listOfUserInfo.get(indexOfMap);
        Fragment fragment = new DetailedRideHistory();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_layout_screen,fragment);
        fragmentTransaction.addToBackStack(null);

        Bundle bundle = new Bundle();
        bundle.putSerializable("history", detaileMap);
        bundle.putSerializable("driver_info", mp);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();

    }
}
