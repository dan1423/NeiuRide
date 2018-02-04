package com.app.danny.neiuber.menu.items;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.danny.neiuber.R;

import java.util.HashMap;

/**
 * Created by danny on 1/20/18.
 *
 */

public class HelpMenu extends Fragment {

    public HelpMenu() {

    }

    private View helpView;
    private TextView textviewFaqs, textviewEmail;

    HashMap<String, String> mp = new HashMap<>();

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
        helpView =  inflater.inflate(R.layout.nav_help_menu, container, false);
        textviewFaqs = helpView.findViewById(R.id.textviewHelpFaqs);
        textviewEmail = helpView.findViewById(R.id.textviewHelpEmail);
        textviewFaqs = setTextView(textviewFaqs, "Frequently Asked Questions");
        textviewEmail = setTextView(textviewEmail, "Email Us");

        return helpView;
    }

    private TextView setTextView(TextView textView, String message) {
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='http://www.google.com'>"+message +"</a>";
        textView.setText(Html.fromHtml(text));

        return  textView;
    }


}