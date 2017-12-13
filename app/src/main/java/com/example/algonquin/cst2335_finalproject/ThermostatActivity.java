package com.example.algonquin.cst2335_finalproject;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ThermostatActivity extends FragmentActivity {
    private Button ruleAddButton;
    private FloatingActionButton floatingActionButton;
    private String [] days;
    private ArrayList<ScheduleEntry> arrayList = new ArrayList<>();
    private ListView chatListView;
    private FrameLayout frameLayout;
    private ChatAdapter messageAdapter;
    private ProgressBar progressBar;
    private SQLiteDatabase db;
    private Handler handler; // Timer Initiator
    private int timerCounter;
    private Runnable runnable; //for timer
    private int y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermostat);
        ruleAddButton = findViewById(R.id.addRuleButt);
        chatListView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.thermoProgressBar);
        messageAdapter =new ChatAdapter( this );
        chatListView.setAdapter (messageAdapter);
        //Checking Phone orientation
        frameLayout = findViewById(R.id.frameThermo);
        days = new String[] {getResources().getString(R.string.monday), getResources().getString(R.string.tuesday), getResources().getString(R.string.wednesday),
                getResources().getString(R.string.thursday), getResources().getString(R.string.friday), getResources().getString(R.string.saturday), getResources().getString(R.string.sunday)};
        floatingActionButton = findViewById(R.id.floatingActionButton);

        //Initializer
//        System.out.println("Position-"+frameLayout);

        //Timer Initializer
        //Timer task for delays
        timerCounter=0;
        //Timer counter idea is taken from http://www.mopri.de/2010/timertask-bad-do-it-the-android-way-use-a-handler/comment-page-1/
        handler = new Handler(); // Timer Initiator
        runnable = new Runnable() {
            @Override
            public void run() {
      /* do what you need to do */
            timerCounter++;
            if (timerCounter>100000) timerCounter=0;
//            System.out.println("=="+timerCounter);
      /* and here comes the "trick" */
                handler.postDelayed(this, 100);
            }
        };
        handler.postDelayed(runnable, 1000);

        //Access to Database via AsyncTask
        new Async(this).execute("");

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

                //Checking for phone or tablet
                //
                Bundle tempBundle = new  Bundle();
                tempBundle.putInt("mode", 6);
                tempBundle.putIntArray("data", tempArray);
                //This is what we do for phone
                if (frameLayout==null) {
                    Intent intent = new Intent(getApplicationContext(), ThermoMessageDetails.class);
                    intent.putExtra("package",tempBundle);
                    startActivityForResult(intent, 10);
                } else{
                    ThermoRuleSetterActivity newFragment = new ThermoRuleSetterActivity(1);
                    newFragment.setArguments(tempBundle);
                    //Backstack cleaning taken from Stackoverflow https://stackoverflow.com/questions/17107005/how-to-clear-fragment-backstack-in-android
                    while (getSupportFragmentManager().getBackStackEntryCount() > 0){
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.frameThermo, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        //New Rule Button Handler
        ruleAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle tempBundle = new Bundle();
                tempBundle.putInt("mode", 5); // 5 - new entry
                //Checking Different Layouts

                //If this is phone
                if (frameLayout==null) {
                    Intent intent = new Intent(getApplicationContext(), ThermoMessageDetails.class);
                    intent.putExtra("package", tempBundle);
                    startActivityForResult(intent, 10);
                } else{
                    //If this is Tablet
                    ThermoRuleSetterActivity newFragment = new ThermoRuleSetterActivity(1);
                    newFragment.setArguments(tempBundle);
                    //Backstack cleaning taken from Stackoverflow https://stackoverflow.com/questions/17107005/how-to-clear-fragment-backstack-in-android
                    while (getSupportFragmentManager().getBackStackEntryCount() > 0){
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                     transaction.replace(R.id.frameThermo, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
          });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(v.getContext());
                builder.setTitle("Thermostat Activity by Nikolay Melnik");
                builder.setMessage( "You can program the thermostat by \n" +
                                    "adding the new rules or deleting/updating\n" +
                                    "the old ones.\n" +
                                    "Please press \"Add new Rule\" button\n" +
                                    "to add new rule, or click on the List\n" +
                                    "to update/delete an old one.\n" +
                                    "In the next activity you can change\n" +
                                    "individual parameters by clicking on them\n" +
                                    "or by choosing different options.\n" +
                                    "After you are satisfied with your choice\n" +
                                    "please click proper button for operation\n" +
                                    "and you will return back to this screen\n" +
                                    "where you will see the changes.\n" +
                                    "If you enter a duplicate entry, you will\n" +
                                    "be asked to confirm your choice by an Alert\n" +
                                    "window.");
// Add the button
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                // Create the AlertDialog
                android.support.v7.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }//END of onCreate



    ////////////////////////////////////////////////////////////////
    //Different Methods
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
        if (requestCode == 10) { //From ThermoRuleSetterActivity
      if (data==null) return;
            Bundle extras = data.getExtras();
            int[] tempArray = (int[]) extras.get("data");
            ScheduleEntry tempEntry = new ScheduleEntry(tempArray[0], tempArray[1], tempArray[2], tempArray[3], tempArray[4]);

            if (resultCode == 11) { //Create new entry
                addEntry(tempEntry);

            } else if (resultCode ==12){ //Delete Entry
                eraseEntry(tempEntry);

            } else if (resultCode ==13){ //Change Entry
                updateEntry(tempEntry);
            }
        }
    }

    //Duplication checker
    public void duplicationChecker(ScheduleEntry x){

        String query = "SELECT * FROM "+ThermoDatabaseHelper.TABLE_NAME+
                " WHERE "+ThermoDatabaseHelper.WEEK_DAY+" = '"+x.day +"' AND "+
                ThermoDatabaseHelper.TIME_HRS+" = '"+x.hours +"' AND "+
                ThermoDatabaseHelper.TIME_MIN+" = '"+x.minutes +"' AND "+
                ThermoDatabaseHelper.TEMPERATURE+" = '"+x.temperature+"'";
        Cursor returnCursor = db.rawQuery(query,null);

        //Checking if it is

            if (returnCursor.getCount()<1) return;
                //Duplicate is found
                returnCursor.moveToFirst();
                y = returnCursor.getInt(returnCursor.getColumnIndex(ThermoDatabaseHelper.KEY_ID));
                returnCursor.close();

                AlertDialog.Builder builder = new AlertDialog.Builder(ThermostatActivity.this);
                builder.setMessage(R.string.duplicateQuestion)

                        .setTitle(getResources().getString(R.string.thermoDuplicate))
                        .setPositiveButton(getResources().getString(R.string.thermoYes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                //Deleting entry from database
                                eraseEntry(new ScheduleEntry(y,0,0,0,0));
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.thermoNo), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        })
                        .show();
    }

 //Entry Update
    public void updateEntry(ScheduleEntry tempEntry){
        duplicationChecker(tempEntry);
        //Update query
        ContentValues values = new ContentValues();
        values.put(ThermoDatabaseHelper.WEEK_DAY, tempEntry.day);
        values.put(ThermoDatabaseHelper.TIME_HRS, tempEntry.hours);
        values.put(ThermoDatabaseHelper.TIME_MIN, tempEntry.minutes);
        values.put(ThermoDatabaseHelper.TEMPERATURE, tempEntry.temperature);
        db.update(ThermoDatabaseHelper.TABLE_NAME, values, ThermoDatabaseHelper.KEY_ID+" = ?", new String[]{String.valueOf(tempEntry.id)});
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).id==tempEntry.id){
                arrayList.set(i, tempEntry);
            }
        }
        addAndSort();
        messageAdapter.notifyDataSetChanged();

        Toast toast = Toast.makeText(getApplicationContext(), R.string.SuccessfulEntry, Toast.LENGTH_SHORT);
        toast.show(); //Delete confirmation
    }

    //Entry Adding
    public void addEntry(ScheduleEntry tempEntry){
        duplicationChecker(tempEntry);
        //Insert procedure
        ContentValues values = new ContentValues();
        values.put(ThermoDatabaseHelper.WEEK_DAY, tempEntry.day);
        values.put(ThermoDatabaseHelper.TIME_HRS, tempEntry.hours);
        values.put(ThermoDatabaseHelper.TIME_MIN, tempEntry.minutes);
        values.put(ThermoDatabaseHelper.TEMPERATURE, tempEntry.temperature);
        db.insert(ThermoDatabaseHelper.TABLE_NAME, null, values);
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        cursor.moveToFirst();
        tempEntry.id = cursor.getInt(0);
        cursor.close();
        arrayList.add(tempEntry);
        addAndSort();
        messageAdapter.notifyDataSetChanged();
        View view = findViewById(android.R.id.content);

        Snackbar snackbar = Snackbar.make (view, getString(R.string.EntrySuccessful), Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    //Entry eraser
    public void eraseEntry(ScheduleEntry tempEntry){
        int x = tempEntry.id;
        String query = "DELETE FROM "+ThermoDatabaseHelper.TABLE_NAME + " WHERE "+
                ThermoDatabaseHelper.KEY_ID +" = '"+x + "';";
        db.execSQL(query);
        Log.i("==", "QueryDelete="+query);
        //deleting line from view
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).id==x){
                arrayList.remove(i);
            }
        }
        messageAdapter.notifyDataSetChanged();

        Toast toast = Toast.makeText(getApplicationContext(), R.string.EntryRemoved, Toast.LENGTH_SHORT);
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
            return arrayList.get(position);
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
            String text = days[tempEntry.day] + ", Time:";
            if (tempEntry.hours<10){text += "0";}
            text += tempEntry.hours +":";
            if (tempEntry.minutes<10) {text+="0";}
            text += tempEntry.minutes;
            text = text + ", \nTemperature in C:" + (tempEntry.temperature);
            message.setText(text); // get the string at position
            return result;
        }

    }//End Inner Class ChatAdapter

    //Inner Class
    private class Async extends AsyncTask<String, Integer, String>{
        Activity mActivity;

        public Async(Activity activity) {
            mActivity = activity;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
     //       returnCursor = null;
            //Initializing video countdown
            timerCounter = 0;
            progressBar.setActivated(true);
         }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ProgressBar pbar;
            pbar = mActivity.findViewById(R.id.thermoProgressBar);
            pbar.setVisibility(View.INVISIBLE);
            addAndSort();
            messageAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int x = values[0];
            ProgressBar pbar;
            pbar = mActivity.findViewById(R.id.thermoProgressBar);
            pbar.setVisibility(View.VISIBLE);
            pbar.setProgress(x);
        }

        @Override
        protected String doInBackground(String... strings) {
            onProgressUpdate(25);

//Database opener and processor
            if (db == null) {
                db= new ThermoDatabaseHelper(getApplicationContext()).getReadableDatabase();
            }

            while (timerCounter<10);
            onProgressUpdate(50);

            //Update Screen
            Cursor cursor = db.rawQuery("SELECT * FROM "+ThermoDatabaseHelper.TABLE_NAME, null);
            while (timerCounter<20);
            onProgressUpdate(75);
            arrayList.clear();
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                ScheduleEntry temp = new ScheduleEntry();

                temp.id = cursor.getInt(cursor.getColumnIndex(ThermoDatabaseHelper.KEY_ID));
                temp.day = cursor.getInt(cursor.getColumnIndex(ThermoDatabaseHelper.WEEK_DAY));
                temp.hours = cursor.getInt(cursor.getColumnIndex(ThermoDatabaseHelper.TIME_HRS));
                temp.minutes = cursor.getInt(cursor.getColumnIndex(ThermoDatabaseHelper.TIME_MIN));
                temp.temperature = cursor.getInt(cursor.getColumnIndex(ThermoDatabaseHelper.TEMPERATURE));
                arrayList.add(temp);
                cursor.moveToNext();
            }
            while (timerCounter<30);

            onProgressUpdate(100);
            while (timerCounter<40);
            cursor.close();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}