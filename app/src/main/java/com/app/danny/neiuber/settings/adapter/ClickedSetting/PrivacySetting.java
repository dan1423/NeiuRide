package com.app.danny.neiuber.settings.adapter.ClickedSetting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.app.danny.neiuber.R;
import com.app.danny.neiuber.authentication.Login;
import com.app.danny.neiuber.authentication.SavedUserInfo;

import java.util.HashMap;

/**
 * Created by danny on 1/19/18.
 */

public class PrivacySetting extends Fragment {
    private View viewForDetailed;
    private HashMap<String, String> mp = new HashMap<>();
    Button btnLogout, btnDeleteAccount;
    private ImageView btnBackToSettings;

    public PrivacySetting() {

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

        viewForDetailed = inflater.inflate(R.layout.clicked_setting_edit_privacy, container, false);
        btnLogout = (Button) viewForDetailed.findViewById(R.id.settingButtonLogout);
        btnDeleteAccount = (Button) viewForDetailed.findViewById(R.id.settingButtonDeleteAccount);
        btnBackToSettings =  (ImageView) viewForDetailed.findViewById(R.id.privacyBackToSettings);

        btnLogout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                logOutAndSendToLogin();
            }
        });
        btnDeleteAccount.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
               deleteAccount();
            }
        });


        btnBackToSettings.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                backToSettings();
            }
        });


        return  viewForDetailed;
    }

    public void logOutAndSendToLogin() {
        SavedUserInfo.clearAllSharedPreferences(getContext());
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }

    public void deleteAccount() {

    }


    private void backToSettings() {
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
    }
}
