package com.example.algonquin.cst2335_finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityTracking extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        //
        Button bt_addActivity = findViewById(R.id.button_addActivity);
        bt_addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTracking.this,AddOneActivityTracking.class);
                startActivity(intent);
            }
        });

        Button bt_viewActivity = findViewById(R.id.button_ViewActivity);
        bt_viewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTracking.this,ViewActivityTracking.class);
                startActivity(intent);
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
