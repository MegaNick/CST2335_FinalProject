package com.example.algonquin.cst2335_finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.icu.util.Calendar;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ThermostatActivity extends Activity {
    Button ruleAddButton;
    int year, monthOfYear, dayOfMonth;
    public static final String [] DAYS = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
    ArrayList<ScheduleEntry> arrayList = new ArrayList<>();
    private ListView chatListView;
    private ChatAdapter messageAdapter;
    int y;
    ScheduleEntry tempScheduleEntry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermostat);
        ruleAddButton = findViewById(R.id.addRuleButt);
        chatListView = findViewById(R.id.listView);
        messageAdapter =new ChatAdapter( this );
        chatListView.setAdapter (messageAdapter);

        //ListView listeners
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ScheduleEntry tempEntry = arrayList.get(position);
            int [] tempArray = new int [6];
                tempArray[0] = tempEntry.id;
                tempArray[1] = tempEntry.day;
                tempArray[2] = tempEntry.hours;
                tempArray[3] = tempEntry.minutes;
                tempArray[4] = tempEntry.temperature;
                tempArray[5] = position;
                Intent intent = new Intent(getApplicationContext(), RuleSetterActivity.class);
                intent.putExtra("mode", 6 ); // 6 - change or delete entry
                intent.putExtra("data", tempArray);
                startActivityForResult(intent, 10);
            }
        });

        //New Rule Button Handler
        ruleAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RuleSetterActivity.class);
                intent.putExtra("mode", 5 ); // 5 - new entry
                startActivityForResult(intent, 10);
            }
          });
    }

    //Add and sort Method
    public void addAndSort(){
        //Sorting the Array
        Collections.sort(arrayList, new CustomComparator());
    }
    //Custom Comparator
    private class CustomComparator implements Comparator<ScheduleEntry> {

        @Override
        public int compare(ScheduleEntry o1, ScheduleEntry o2) {
            int firstMinutes = 1440* o1.day + 60*o1.hours + o1.minutes;
            int secondMinutes = 1440* o2.day + 60*o2.hours + o2.minutes;
            return (firstMinutes-secondMinutes);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //If entering new value
        if (requestCode == 10) { //From RuleSetterActivity
            if (resultCode == 11) { //Create new entry
                Bundle extras = data.getExtras();
                int[] tempArray = (int[]) extras.get("data");
                ScheduleEntry tempEntry = new ScheduleEntry(tempArray[0], tempArray[1], tempArray[2], tempArray[3], tempArray[4]);
                duplicationChecker(tempEntry);
                arrayList.add(tempEntry);
            } else if (resultCode ==12){ //Delete Entry
                Bundle extras = data.getExtras();
                int[] tempArray = (int[]) extras.get("data");
                eraseEntry(tempArray);
            } else if (resultCode ==13){ //Change Entry
                Bundle extras = data.getExtras();
                int[] tempArray = (int[]) extras.get("data");
                arrayList.get(tempArray[5]).id = tempArray[0];
                arrayList.get(tempArray[5]).day = tempArray[1];
                arrayList.get(tempArray[5]).hours = tempArray[2];
                arrayList.get(tempArray[5]).minutes = tempArray[3];
                arrayList.get(tempArray[5]).temperature = tempArray[4];
                ScheduleEntry tempEntry = arrayList.get(tempArray[5]);
                duplicationChecker(tempEntry);
            }
        }
    }

    //Duplication checker
    public void duplicationChecker(ScheduleEntry x){

        boolean result = false;
        addAndSort();
        messageAdapter.notifyDataSetChanged();

        for (int i = 0; i < arrayList.size(); i++) {
            ScheduleEntry z = arrayList.get(i);
            if (z.day == x.day && z.hours == x.hours && z.minutes == x.minutes && z.temperature == x.temperature) {
                result = true;
                y = i;
                break;
            }
        }

        //Checking if it is

            if (result) {
                //Duplicate is found
                AlertDialog.Builder builder = new AlertDialog.Builder(ThermostatActivity.this);
// 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.duplicateQuestion)

                        .setTitle("Duplcate entry's found!")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                arrayList.remove(y);
                                addAndSort();
                                messageAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        })
                        .show();

            }
    }

    //Entry eraser
    public void eraseEntry(int[] x){
        arrayList.remove(x[5]);
        messageAdapter.notifyDataSetChanged();
        Toast toast = Toast.makeText(getApplicationContext(), "Entry has been removed successfully", Toast.LENGTH_SHORT);
        toast.show(); //Delete confirmation
    }


    //Inner Class
    private class ChatAdapter extends ArrayAdapter<ScheduleEntry>{

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        //Methods
//        a.	int getCount()  -  This returns the number of rows that will be in your listView. In your case, it should be the number of strings in the array list object ( return list.size() ).
//        b.	String getItem(int position) – This returns the item to show in the list at the specified position: ( return list.get(position) )
//        c.	View getView(int position, View convertView, ViewGroup parent) – this returns the layout that will be positioned at the specified row in the list. Do this in step 9.
        @Override
        public int getCount(){
            return arrayList.size();
        }

        @Override
        public ScheduleEntry getItem(int position){
            ScheduleEntry tempEntry = arrayList.get(position);
            return tempEntry;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ThermostatActivity.this.getLayoutInflater();
            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.thermostat_row_even, null);
            else
                result = inflater.inflate(R.layout.thermostat_row_odd, null);
            TextView message = (TextView)result.findViewById(R.id.message_text);

            ScheduleEntry tempEntry = getItem(position);
            String text = DAYS[tempEntry.day] + ", Time:";
            if (tempEntry.hours<10){text += "0";}
            text += tempEntry.hours +":";
            if (tempEntry.minutes<10) {text+="0";}
            text += tempEntry.minutes;
            text = text + ", Temperature in C:" + tempEntry.temperature;
            message.setText(text); // get the string at position
            return result;
        }

    }//End Inner Class

}