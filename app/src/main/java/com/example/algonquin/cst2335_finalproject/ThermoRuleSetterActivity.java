package com.example.algonquin.cst2335_finalproject;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class ThermoRuleSetterActivity extends Activity {
    public static final String ACTIVITY_NAME = "ThermoRuleSetterActivity";
    public static final Integer TEMP_VALUES [] = {5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35};
    TextView welcomeText;
    Spinner spinner;
    EditText setTime;
    Spinner setTemperature;
    ScheduleEntry entry;
    Button submit;
    Button deleteButton;
    Button cancelButton;
    Button newRuleButton;
    int [] intArray; // 0-id, 1-day, 2-hours, 3-minutes, 4-temperature, 5-viewId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_setter);
        welcomeText = findViewById(R.id.welcomeText);
        spinner = findViewById(R.id.spinnerDays);
        setTime = findViewById(R.id.textTime);
        setTemperature = findViewById(R.id.spinnerTemp);
        submit = findViewById(R.id.buttonSubmit);
        deleteButton = findViewById(R.id.deleteButton);
        cancelButton = findViewById(R.id.cancelButton);
        newRuleButton = findViewById(R.id.newRuleButton);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        intArray = (int[]) extras.get("data");
        int mode = (int) extras.get("mode");

        if (mode == 5) { //Do entry new value
            //Changing welcome text
            welcomeText.setText("Please Enter New Thermostat Rule");
            intArray = new int [] {0,0,0,0,15};
            deleteButton.setEnabled(false);
            submit.setEnabled(false);
        } else if (mode == 6){ //Do edit or delete value
            welcomeText.setText("Please change or delete Thermostat Rule");
            deleteButton.setEnabled(true);
            submit.setEnabled(true);
        }
//Put Numbers into proper boxes
        //Spinner with Dates
        // Info about spinners https://developer.android.com/guide/topics/ui/controls/spinner.html
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, ThermostatActivity.DAYS);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(intArray[1]);

        //Setting time
        String tempTime = "";
        if (intArray[2]<10){tempTime = "0";}
        tempTime = tempTime + intArray[2] +":";
        if (intArray[3]<10) {tempTime+="0";}
        tempTime = tempTime + intArray[3];
        setTime.setText(tempTime);

        //Setting temperature
        //Spinner with temperatures
        ArrayAdapter<Integer> spinnerAdapter2 = new ArrayAdapter<> (this, android.R.layout.simple_spinner_item, ThermoRuleSetterActivity.TEMP_VALUES);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setTemperature.setAdapter(spinnerAdapter2);
        setTemperature.setSelection(intArray[4]);


        //Settings changers

        //Spinner Dates Handler
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(ACTIVITY_NAME, "==Day Position = "+position);
                intArray[1] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Time Handler
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //Stack overflow https://stackoverflow.com/questions/17901946/timepicker-dialog-from-clicking-edittext

                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker = new TimePickerDialog(ThermoRuleSetterActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    intArray[2] = selectedHour;
                    intArray[3] = selectedMinute;
                    String tempTime = "";
                    if (intArray[2]<10){tempTime = "0";}
                    tempTime = tempTime + intArray[2] +":";
                    if (intArray[3]<10) {tempTime+="0";}
                    tempTime = tempTime + intArray[3];
                    setTime.setText(tempTime);
                }
                        }, hour, minute, true);//True -  24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
            }
        });

        //Temperature handler
        setTemperature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(ACTIVITY_NAME, "==Temperature Position = "+position);
                intArray[4] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Submit Button Handler
        newRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data", intArray);
                setResult(11, intent); //11-create new entry
                finish();
            }
        });

        //Cancel Button Handler
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(0, intent);
                finish();
            }
        });

        //Delete button handler
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data", intArray);
                setResult(12, intent); //12-delete entry
                finish();
            }
        });

        //Save Button handler
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data", intArray);
                setResult(13, intent); //12-change entry
                finish();
            }
        });
    }

}
