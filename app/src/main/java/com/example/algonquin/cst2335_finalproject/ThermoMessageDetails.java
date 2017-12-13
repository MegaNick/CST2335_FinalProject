package com.example.algonquin.cst2335_finalproject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class ThermoMessageDetails extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermo_message_details);
        Bundle extras = getIntent().getExtras();
        Bundle args = extras.getBundle("package");


        // Otherwise, we're in the one-pane layout and must swap frags...
        // Create fragment and give it an argument for the selected article
        ThermoRuleSetterActivity newFragment = new ThermoRuleSetterActivity(0);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
//        transaction.replace(R.id.emptyFrame, newFragment);
        transaction.replace(R.id.emptyFrame, newFragment);
//        transaction.addToBackStack(null);
//        // Commit the transaction
        transaction.commit();

    }

}
