package com.app.danny.neiuber.menu.items;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.danny.neiuber.R;

import java.util.HashMap;
/*Fragment  of user's profile*/

public class DriverProfileMenu extends Fragment {

    private View driverProfileView;
    private TextView name,location,phone,registrationDate,numberOfRides;

   public DriverProfileMenu(){

   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        driverProfileView = inflater.inflate(R.layout.activity_driver_profile, container, false);

        name = (TextView) driverProfileView.findViewById(R.id.txtProfileName);
        location = (TextView) driverProfileView.findViewById(R.id.txtProfileLocation);
        phone = (TextView) driverProfileView.findViewById(R.id.txtProfilePhone);
        registrationDate = (TextView) driverProfileView.findViewById(R.id.registrationDate);
        numberOfRides = (TextView) driverProfileView.findViewById(R.id.txtProfileNumberOfRides);

        HashMap<String, String> mp = new HashMap<>();
        Bundle bundle = new Bundle();
        bundle = this.getArguments();


        if (bundle != null) {
            mp =  (HashMap<String, String>) bundle.getSerializable("driver_info");

            name.setText(mp.get("FirstName") + " "+mp.get("LastName") );
            location.setText(mp.get("City")+","+mp.get("State"));
            phone.setText(mp.get("PhoneNumber"));
            registrationDate.setText("Member since "+ mp.get("RegistrationDate"));
            numberOfRides.setText("You have given 0 rides");
        }else{
            name.setText("PROFILE NOT AVAILABLE");
        }




        return driverProfileView;
    }


}
