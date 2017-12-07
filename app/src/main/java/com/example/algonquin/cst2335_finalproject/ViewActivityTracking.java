package com.example.algonquin.cst2335_finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewActivityTracking extends Activity {
    ListView listView;
    ArrayList<String> infor;
/*
    ArrayList<String> date;
    ArrayList<String> activityType;
    ArrayList<String> duration;
    ArrayList<String> comment;
*/
    Cursor cursor;
    SQLiteDatabase db;
    LayoutInflater inflater;
    View result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tracking);

        listView = findViewById(R.id.listView_trackingItem);
        infor = new ArrayList<>();
/*
        date = new ArrayList<>();
        activityType = new ArrayList<>();
        duration = new ArrayList<>();
        comment = new ArrayList<>();
*/
        final ActivityTrackingDatabaseHelper aTrackingHelperObject = new ActivityTrackingDatabaseHelper(this);
        final ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.activitytrackingrecord,infor);
        //Create a object to contain message
        final ActivityTrackingAdapter messageAdapter =new ActivityTrackingAdapter( this );
        db = aTrackingHelperObject.getWritableDatabase();
        cursor = db.query(false,aTrackingHelperObject.name,new String[]{"_id","DATE","ACTIVITYTYPE","DURATION","COMMENT"},null,null,null,null,null,null);
/*
        //Get the column number of the table of the database
        Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount() );
        //Get the column name of the table of the database
        for (int i=0; i<cursor.getColumnCount(); i++){
            Log.i(ACTIVITY_NAME,"Cursor's column name of index " + cursor.getColumnName(i));
        }
*/
        //Get the index(what column in order starting from 0) of message in the table
        int idIndex =  cursor.getColumnIndex( aTrackingHelperObject.KEY_ID);
        int dateIndex =  cursor.getColumnIndex( aTrackingHelperObject.KEY_date);
        int activityTypeIndex = cursor.getColumnIndex( aTrackingHelperObject.KEY_ACTIVITYTYPE);
        int durationIndex =  cursor.getColumnIndex( aTrackingHelperObject.KEY_DURATION);
        int commentIndex = cursor.getColumnIndex( aTrackingHelperObject.KEY_COMMENT);

        //Get message and add them to the arrayList
        String returnedInfor;
        int countRow = cursor.getCount();
        cursor.moveToFirst();//resets the iteration of results
        while(!cursor.isAfterLast())
        {
//            returnedMessage = cursor.getString(MESSAGEIndex);
//            Log.i(ACTIVITY_NAME,"SQL MESSAGE: " + returnedMessage);
            returnedInfor = cursor.getString(idIndex) + "," + cursor.getString(dateIndex) + "," + cursor.getString(activityTypeIndex) + ","
                    + cursor.getString(durationIndex) + "," + cursor.getString(commentIndex);
            infor.add(returnedInfor);
/*
            date.add(cursor.getString(dateIndex));
            activityType.add(cursor.getString(activityTypeIndex));
            duration.add(cursor.getString(durationIndex));
            comment.add(cursor.getString(commentIndex));
*/
            cursor.moveToNext();
        }

        //Set the header of ListView
        inflater = ViewActivityTracking.this.getLayoutInflater();
        result = inflater.inflate(R.layout.activitytrackingrecord, null);
        listView.addHeaderView(result);
        //Put information adapter to listView to display the activities.
        listView.setAdapter (messageAdapter);

        //Click listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
/*
                //Get value from the clicked item
                TextView item1 = (TextView)view.findViewById(R.id.textView_dateAndTime);
                TextView item2 = (TextView)view.findViewById(R.id.textView_activityType);
                TextView item3 = (TextView)view.findViewById(R.id.textView_durationInMinutes);
        //        TextView item4 = (TextView)view.findViewById(R.id.textView_comment);
                String value = item1.getText().toString() + "," + item2.getText().toString() +"," + item3.getText().toString()
                        + ",";// +item4.getText().toString()+ "," +"view";
*/
                //Pass the clicked record to addOneActivityTracking activity
                if (i==0 || i<0){
                    //The header is clicked, nothing is needed to do.
                }else {   //The activity tracking item is clicked
                    String value = infor.get(i-1);
                    final Bundle info = new Bundle();
                    info.putString("Click",value);
                    Log.i("ViewActivityTracking","bundle " + value);

                    Intent intent = new Intent(ViewActivityTracking.this, AddOneActivityTracking.class);
                    intent.putExtras(info);
                    startActivity(intent);
                }
            }
        });


    }

    private class ActivityTrackingAdapter extends ArrayAdapter<String> {
        public ActivityTrackingAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return infor.size();
        }

        public String getItem(int position){
            return infor.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            inflater = ViewActivityTracking.this.getLayoutInflater();

            String[] separated = getItem(position).split(","); // get the string at position
            result = inflater.inflate(R.layout.activitytrackingrecord, null);
            TextView tv_date = (TextView)result.findViewById(R.id.textView_dateAndTime);
            TextView tv_activityType = (TextView)result.findViewById(R.id.textView_activityType);
            TextView tv_duration = (TextView)result.findViewById(R.id.textView_durationInMinutes);

            tv_date.setText(separated[1]); //set the activity type
            tv_activityType.setText(separated[2]);   //set the activity type
            tv_duration.setText(separated[3]);  //set the duration
            return result;
        }

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
        cursor.close();
        db.close();
    }

}
