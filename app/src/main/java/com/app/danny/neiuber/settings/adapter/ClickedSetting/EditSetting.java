package com.app.danny.neiuber.settings.adapter.ClickedSetting;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.danny.neiuber.R;
import com.app.danny.neiuber.authentication.InputValidator;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by danny on 1/25/18.
 */
/*Need to validate phone and email, send sms to phone, email confirmation to email*/
public class EditSetting extends Fragment {

    public EditSetting() {

    }

    View editSettingView;
    String typeOfSetting, heading, settingData;
    TextView header;
    EditText textContent;
    Button saveEdit, clearEdit;
    ImageView arrowBackToPersonalSetting;
    InputValidator validator = new InputValidator();
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = new Bundle();
        bundle = this.getArguments();
        if(bundle != null){
            typeOfSetting = bundle.getString("setting_type");
            heading = bundle.getString("heading");
            settingData = bundle.getString("setting_data");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        editSettingView = inflater.inflate(R.layout.edit_setting_fragment, container, false);


        header = (TextView)editSettingView.findViewById(R.id.textEditSettingHeading);
        textContent = (EditText)editSettingView.findViewById(R.id.textEditSettingContent);
        saveEdit = (Button)editSettingView.findViewById(R.id.btnEditSettingSave);
        clearEdit = (Button)editSettingView.findViewById(R.id.btnEditSettingClear);
        arrowBackToPersonalSetting = (ImageView) editSettingView.findViewById(R.id.arrowBackToPersonalSetting);

        header.setText(heading);

        arrowBackToPersonalSetting.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                backToPersonalSetting();
            }
        });

        saveEdit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(typeOfSetting.trim().equals("phone")){
                    editPhone();
                }else if(typeOfSetting.trim().equals("email")){
                   editEmail();
                }
            }
        });

        clearEdit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                textContent.setText("");
            }
        });

        return  editSettingView;
    }


    public void editEmail() {
        Toast.makeText(getContext(),settingData,Toast.LENGTH_LONG).show();
       // SaveSetting cas = new SaveSetting(mp.get("PhoneNumber"),getContext());
       // cas.saveEmail(email.getText().toString());

    }



    public void editPhone() {
       if(validator.validatePhoneNumber(textContent.getText().toString())){
           confirmChange(textContent.getText().toString());
       }else{
           Toast.makeText(getContext(),"Please Enter a valid phone",Toast.LENGTH_LONG).show();
       }

       // SaveSetting cas = new SaveSetting(mp.get("PhoneNumber"),getContext());
       // cas.savePhoneNUmber();

    }



    private void confirmChange(String userInput){
        Random rand = new Random();
        String randGen = String.format("%04d", rand.nextInt(10000));
        if(checkPerm()){
            validator.sendSMS(userInput, randGen, getContext());
            backToPersonalSetting();
        }

    }

    private boolean checkPerm() {
        String permission = Manifest.permission.SEND_SMS;
        int grant = ContextCompat.checkSelfPermission(getContext(), permission);
        if ( grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(getActivity(), permission_list, 1);
        }else{
            return true;
        }
        return  false;
    }

    private void backToPersonalSetting(){
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
    }

}
