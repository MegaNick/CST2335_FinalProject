package com.example.algonquin.cst2335_finalproject;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddOneActivityTracking extends Activity implements AdapterView.OnItemSelectedListener{

    String[] activityType={"Running","Walking","Biking","Swimming","Skating"};
    Button bt_add;
    Button bt_delete;
    Button bt_update;
    EditText et_duration, et_comment;
    TextView tv_date;
    SQLiteDatabase db;
    String activity; //, date, comment, duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_one_tracking);

        bt_add = findViewById(R.id.button_add);
        bt_delete = findViewById(R.id.button_delete);
        et_duration = findViewById(R.id.editText_amountOfTime);
        et_comment = findViewById(R.id.editText_comment);
        bt_update = findViewById(R.id.button_update);
        tv_date = findViewById(R.id.textView_valueOfDate);

        //Get current date
        final Calendar c = Calendar.getInstance();
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                date = df.format(c.getTime());

        //Set up the spinner of activity type.
        Spinner spin = (Spinner) findViewById(R.id.spinner_selectOneActivity);
        //Load the spinner of activity type.
        spin.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,activityType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        //Get the bundle value.
        Bundle info = getIntent().getExtras();

        if (info != null){
            //Set update and delete button visible, and add invisible.
            bt_update.setVisibility(View.VISIBLE);
            bt_delete.setVisibility(View.VISIBLE);
            bt_add.setVisibility(View.INVISIBLE);

            String[] separated = info.getString("Click").split(","); // get the string at position
            //Set the spinner of activity type to the clicked item in viewActivityTracking.
            //Find the item position and set the value of spinner
            switch (separated[2]){
                case "Running": spin.setSelection(0);
                    break;
                case "Walking": spin.setSelection(1);
                    break;
                case "Biking": spin.setSelection(2);
                    break;
                case "Swimming": spin.setSelection(3);
                    break;
                case "Skating": spin.setSelection(4);
                    break;
            }

            //Set the value of duration, date and comment.
            tv_date.setText(separated[1]);
            et_duration.setText(separated[3]);
            et_comment.setText(separated[4]);
            //Log.i("AddOneActivityTracking",info.getString("Click"));
        }else {
            //Set update and delete button invisible, and add visible.
            bt_update.setVisibility(View.INVISIBLE);
            bt_delete.setVisibility(View.INVISIBLE);
            bt_add.setVisibility(View.VISIBLE);
            tv_date.setText(df.format(c.getTime()));
        }

        final ActivityTrackingDatabaseHelper aTrackingHelperObject = new ActivityTrackingDatabaseHelper(this);
        db = aTrackingHelperObject.getWritableDatabase();

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                System.out.println("Current time => " + df.format(c.getTime()));
                System.out.println("Current time => " + activity);
                System.out.println("Current time => " + et_duration.getText().toString());
                System.out.println("Current time => " + et_comment.getText().toString());
*/
//                duration = et_duration.getText().toString();
//                comment = et_comment.getText().toString();

                //create a object for inserting data to a SQLite database
                ContentValues newData = new ContentValues();
                newData.put("DATE", df.format(c.getTime()));
                newData.put("ACTIVITYTYPE", activity);
                newData.put("DURATION", et_duration.getText().toString());
                newData.put("COMMENT", et_comment.getText().toString());
                long rowInserted = db.insert(aTrackingHelperObject.name, "", newData);
                if(rowInserted != -1){
                    Toast toast = Toast.makeText(getApplicationContext(), "Inserted successfully", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Failed to insert", Toast.LENGTH_SHORT);
                    toast.show();
                }

                //Clear the input
                et_duration.setText("");
                et_comment.setText("");
            }
        });


    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //Toast.makeText(getApplicationContext(), activityType[position], Toast.LENGTH_LONG).show();
        activity = activityType[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

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
        db.close();
    }

}
