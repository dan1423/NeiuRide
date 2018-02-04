package com.app.danny.neiuber.settings.adapter.ClickedSetting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.danny.neiuber.R;

import java.util.HashMap;

/**
 * Created by danny on 1/19/18.
 */
/*
* User will be able to edit phone number, email, password
* */
public class PersonalSetting extends Fragment {

    private ImageView iv;
    private TextView name,phoneNumber, email;
    private EditText userInput;

    private View viewForDetailed;
    private HashMap<String, String> mp = new HashMap<>();


    public PersonalSetting() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = new Bundle();
        bundle = this.getArguments();
        if (bundle != null) {
            mp = (HashMap<String, String>) bundle.getSerializable("driver_info");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewForDetailed = inflater.inflate(R.layout.clicked_setting_edit_personal, container, false);

        iv = (ImageView) viewForDetailed.findViewById(R.id.arrowBackToSettings);
        name = (TextView) viewForDetailed.findViewById(R.id.txtPersonalSettingName);
        phoneNumber = (TextView) viewForDetailed.findViewById(R.id.txtPersonalSettingPhoneNumber);
        email = (TextView) viewForDetailed.findViewById(R.id.txtPersonalSettingEmail);



       // password = (TextView)viewForDetailed.findViewById(R.id.settingPassword);

        setClickFunctions();
        setTExtFields();


        return  viewForDetailed;
    }

    public void setClickFunctions() {
        iv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                backToSettings();
            }
        });

        //user clicks email to change it
        email.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendToEditSetting("email",email.getText().toString(),"Edit Email");
            }
        });

        //update phone number
        phoneNumber.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sendToEditSetting("phone",email.getText().toString(),"Edit Phone");
            }
        });
    }

    public void setTExtFields() {
        name.setText(mp.get("FirstName") +" "+ mp.get("LastName"));
        phoneNumber.setText(mp.get("PhoneNumber"));
        email.setText(mp.get("Email"));

    }

    private void backToSettings() {
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
    }

    private  void sendToEditSetting(String settingType, String settingData, String heading){
        Fragment fragment = new EditSetting();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_layout_screen,fragment);
        fragmentTransaction.addToBackStack(null);

        Bundle bundle = new Bundle();
        bundle.putString("setting_type", settingType);
        bundle.putString("setting_data", settingData);
        bundle.putString("heading", heading);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();
    }
}
