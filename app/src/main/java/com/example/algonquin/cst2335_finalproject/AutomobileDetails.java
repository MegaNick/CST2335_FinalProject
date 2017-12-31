package com.example.algonquin.cst2335_finalproject;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AutomobileDetails extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile_details);
        //ADD BUNDLE STUFF
        Bundle extras = getIntent().getExtras();
        Bundle args = extras.getBundle("package");

        PurchaseGasActivity automobileFragment = new PurchaseGasActivity();
        automobileFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.emptyAutomobileFrame,automobileFragment);
        transaction.commit();


    }
}
