//To the Glory of God!
// Thermostat activity by Nikolay Melnik. Algonquin College. Ottawa, 2018.
//
package com.example.algonquin.cst2335_finalproject;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Supply class which operates user entries in the second activity of Thermostat
 * By Nikolay Melnik
 */
public class ThermoRuleSetterActivity extends Fragment {
    public static final String ACTIVITY_NAME = "ThermoRuleSetterActivity";
    private TextView welcomeText;
    private Spinner spinner;
    private EditText setTime;
    private EditText setTemperature;
    private Button submit;
    private Button deleteButton;
    private Button cancelButton;
    private Button newRuleButton;
    int phoneMode;
    int [] intArray; // 0-id, 1-day, 2-hours, 3-minutes, 4-temperature, 5-viewId

    //Constructors to check Phone or Tablet

    /**
     * Default constructor
     */
    public ThermoRuleSetterActivity(){} // Default Constructor

    /**
     * Parameterized constructor which sets current phone mode:
     * @param x Mode  0- phone, 1 tablet
     */
    @SuppressLint("ValidFragment")
    public ThermoRuleSetterActivity(int x){
        phoneMode = x;
    }

    /**
     * Major processing method which operates the second activity.
     * Bundle holds int values "mode" 5 - entry new value, 6 - update current value
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return current view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_thermo_rule_setter, container, false);
        welcomeText = view.findViewById(R.id.welcomeText);
        spinner = view.findViewById(R.id.spinnerDays);
        setTime = view.findViewById(R.id.textTime);
        setTime.setFocusable(false);
        setTemperature = view.findViewById(R.id.editTemperature);
        submit = view.findViewById(R.id.buttonSubmit);
        deleteButton = view.findViewById(R.id.deleteButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        newRuleButton = view.findViewById(R.id.newRuleButton);
        final String [] days = {getString(R.string.monday), getString(R.string.tuesday), getString(R.string.wednesday), getString(R.string.thursday), getString(R.string.friday), getString(R.string.saturday), getString(R.string.sunday)};


        Bundle args = this.getArguments();

        int mode = args.getInt("mode");

        if (mode == 5) { //Do entry new value
            //Changing welcome text
            welcomeText.setText(R.string.thermoRuleMessage);
            intArray = new int [] {0,0,0,0,15};
            deleteButton.setEnabled(false);
            submit.setEnabled(false);
        } else if (mode == 6){ //Do edit or delete value
            intArray = args.getIntArray("data");
            welcomeText.setText(R.string.thermoRuleChange);
            deleteButton.setEnabled(true);
            submit.setEnabled(true);
        }
    //Puts Numbers into proper boxes
        //Spinner with Dates
        // Info about spinners https://developer.android.com/guide/topics/ui/controls/spinner.html
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String> (getContext(), android.R.layout.simple_spinner_item, days);
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
        setTemperature.setText(String.valueOf(intArray[4]));

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

                TimePickerDialog mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
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

        //Submit Button Handler
        newRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If it is phone
                if (phoneMode==0) {
                    if (checkTemp()) {
                        Intent intent = new Intent();
                        intent.putExtra("data", intArray);
                        getActivity().setResult(11, intent); //11-create new entry
                        getActivity().finish();
                    }
                } else { //If it is tablet
                    if (checkTemp()) {((ThermostatActivity)getActivity()).addEntry(arrayToEntry(intArray));}
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });

        //Cancel Button Handler
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If it is phone
                if (phoneMode==0) {
                    Intent intent = new Intent();
                    intent.putExtra("data", intArray);
                    getActivity().setResult(0, intent);
                    getActivity().finish();
                }else {//If it is tablet - just leave
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });

        //Delete button handler
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If it is phone
                if (phoneMode==0) {
                    Intent intent = new Intent();
                    intent.putExtra("data", intArray);
                    getActivity().setResult(12, intent); //12-delete entry
                    getActivity().finish();
                } else { //If it is tablet
                    ((ThermostatActivity)getActivity()).eraseEntry(arrayToEntry(intArray));
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });

        //Save Button handler
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If it is phone
                if (phoneMode==0) {
                    if (checkTemp()) {
                        Intent intent = new Intent();
                        intent.putExtra("data", intArray);
                        getActivity().setResult(13, intent); //12-change entry
                        getActivity().finish();
                    }
                } else { //If it is tablet
                    if (checkTemp()) { ((ThermostatActivity)getActivity()).updateEntry(arrayToEntry(intArray));}
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });
    return view;
    }

    //Checking Temperature for valid entry

    /**
     * Method checks valid temperature for the thermostat. If it's wrong, Shows a Toast with warning
     * @return true - temp is ok, false - temp is wrong
     */
    public boolean checkTemp(){
        int temp;
        String temperature = setTemperature.getText().toString();
        try {
            temp = Integer.parseInt(temperature);
        } catch (NumberFormatException e){
            Toast toast = Toast.makeText(getContext(), "Please enter CORRECT temperature value", Toast.LENGTH_SHORT);
            toast.show(); //show warning
            return false;
        }
        intArray[4]=temp;
        return true;
    }

    //Transfer array to ThermoScheduleEntry

    /**
     * Method takes an array of 5 ints and creates new ScheduleEntry object
     * @param x int[5] of data
     * @return ScheduleEntry object
     */
    public ScheduleEntry arrayToEntry(int [] x){
        return new ScheduleEntry(x[0], x[1], x[2], x[3], x[4]);
    }

    /**
     * Standard onDestroy Method
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
