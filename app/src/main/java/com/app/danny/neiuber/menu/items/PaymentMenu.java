package com.app.danny.neiuber.menu.items;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.danny.neiuber.R;

import java.util.HashMap;

/**
 * Created by danny on 1/20/18.
 */

public class PaymentMenu extends Fragment {

    public PaymentMenu() {

    }

    View creditCardMenuView;
    HashMap<String, String> mp = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
       creditCardMenuView =  inflater.inflate(R.layout.nav_payment_menu, container, false);

        return creditCardMenuView;
    }
}
