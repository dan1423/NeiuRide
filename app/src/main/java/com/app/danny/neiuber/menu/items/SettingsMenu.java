package com.app.danny.neiuber.menu.items;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.app.danny.neiuber.R;
import com.app.danny.neiuber.settings.adapter.ClickedSetting.PersonalSetting;
import com.app.danny.neiuber.settings.adapter.ClickedSetting.PrivacySetting;
import com.app.danny.neiuber.settings.adapter.SettingsAdapter;
import com.app.danny.neiuber.settings.adapter.SettingsItems;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by danny on 1/17/18.
 */

public class SettingsMenu extends Fragment {

    public SettingsMenu(){

    }

    private View settingsView;
    HashMap<String, String> mp = new HashMap<>();
    //we will use this list ot keep titles of settings, so when user clicks a setting, we can send the name of that setting to the next page
    ArrayList<Fragment> listOfSettingTitles = new ArrayList<>();

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
        // Inflate the layout for this fragment
        settingsView =  inflater.inflate(R.layout.activity_settings, container, false);
        setSettingsList();
        return settingsView;
    }

   public void setSettingsList(){
        final ListAdapter customListAdapter = new SettingsAdapter(getActivity(),getData());
       ListView customListView = (ListView) settingsView.findViewById(R.id.list_settings);
        customListView.setAdapter(customListAdapter);

        customListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //String titleOfSetting =
                                sendToClickedSetting(listOfSettingTitles.get(position));

                    }
                }
        );

    }

    private ArrayList getData() {

        ArrayList<SettingsItems> settingsArrayList=new ArrayList<>();

        SettingsItems settingIetems1 = new SettingsItems();
        settingIetems1.setImage(R.drawable.ic_person_black_24dp);
        settingIetems1.setSettingTitle("Personal Settings");
        settingIetems1.setSettingDesc("Name, Phone#, Email");
        listOfSettingTitles.add(new PersonalSetting());


        SettingsItems settingIetems2 = new SettingsItems();
        settingIetems2.setImage(R.drawable.ic_lock_black_24dp);
        settingIetems2.setSettingTitle("Privacy");
        settingIetems2.setSettingDesc("Account setting");
        listOfSettingTitles.add(new PrivacySetting());

        settingsArrayList.add(settingIetems1);
        settingsArrayList.add(settingIetems2);

        return settingsArrayList;
    }

    private void sendToClickedSetting( Fragment fg){
        HashMap<String,String> detaileMap = mp;
        Fragment fragment = fg;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_layout_screen,fragment);
        fragmentTransaction.addToBackStack(null);

        Bundle bundle = new Bundle();
        bundle.putSerializable("driver_info", detaileMap);
        fragment.setArguments(bundle);

        fragmentTransaction.commit();

    }

}
