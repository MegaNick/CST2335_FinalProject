package com.example.algonquin.cst2335_finalproject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import javax.xml.datatype.Duration;

public class ViewActivityTracking extends Fragment {
    ListView listView;
    ArrayList<String> infor;
    Cursor cursor;
    Cursor cursor_gb;
    SQLiteDatabase db;
    LayoutInflater inflater;
    View result, view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_tracking, container, false);

        //Instance the forecastQuery and call it.
        databaseQuery query = new databaseQuery();
        query.execute();


        listView = view.findViewById(R.id.listView_trackingItem);
        infor = new ArrayList<>();
        Button bt_orderByDate = (Button)view.findViewById(R.id.button_orderByDate);
        Button bt_summary = (Button)view.findViewById(R.id.button_summary);

        final ActivityTrackingDatabaseHelper aTrackingHelperObject = new ActivityTrackingDatabaseHelper(getActivity());
        final ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.activitytrackingrecord,infor);
        //Create a object to contain message
        final ActivityTrackingAdapter messageAdapter =new ActivityTrackingAdapter(getActivity());
        db = aTrackingHelperObject.getWritableDatabase();

        if (getArguments().getString("button") == "click")
            cursor = db.query(false,aTrackingHelperObject.name,new String[]{"_id","DATE","ACTIVITYTYPE","DURATION","COMMENT"},null,null,null,null,null,null);
        else if (getArguments().getString("button") == "orderByDate")
            cursor = db.query(false,aTrackingHelperObject.name,new String[]{"_id","DATE","ACTIVITYTYPE","DURATION","COMMENT"},null,null,null,null,"DATE",null);
        else if (getArguments().getString("button") == "summary")
            cursor = db.query(false,aTrackingHelperObject.name,new String[]{"DATE", "SUM(DURATION) as DURATION"},null,null,"DATE",null,null,null);


        //Order by date
        bt_orderByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Refresh this fragment
                FragmentManager fm = getFragmentManager();;
                FragmentTransaction transaction = fm.beginTransaction();
                ViewActivityTracking fragmentOrder = new ViewActivityTracking();
                transaction.replace(R.id.fl_toolbar,fragmentOrder,"orderByDate");
                transaction.commit();

                Bundle bundle = new Bundle();
                bundle.putString("button","orderByDate");
                fragmentOrder.setArguments(bundle);

            }
        });

        //Order by date
        bt_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Refresh this fragment
                FragmentManager fm = getFragmentManager();;
                FragmentTransaction transaction = fm.beginTransaction();
                ViewActivityTracking fragmentSummary = new ViewActivityTracking();
                transaction.replace(R.id.fl_toolbar,fragmentSummary,"summary");
                transaction.commit();

                Bundle bundle = new Bundle();
                bundle.putString("button","summary");
                fragmentSummary.setArguments(bundle);

            }
        });

        //Get the index(what column in order starting from 0) of message in the table
        int idIndex =  cursor.getColumnIndex( aTrackingHelperObject.KEY_ID);
        int dateIndex =  cursor.getColumnIndex( aTrackingHelperObject.KEY_date);
        int activityTypeIndex = cursor.getColumnIndex( aTrackingHelperObject.KEY_ACTIVITYTYPE);
        int durationIndex =  cursor.getColumnIndex( aTrackingHelperObject.KEY_DURATION);
        int commentIndex = cursor.getColumnIndex( aTrackingHelperObject.KEY_COMMENT);

//        String n = cursor.getColumnName(1);
        //Get message and add them to the arrayList
        String returnedInfor;
        int countRow = cursor.getCount();
        cursor.moveToFirst();//resets the iteration of results
        if (getArguments().getString("button") == "summary" && countRow > 0){  //summary the item by date
            while(!cursor.isAfterLast())
            {
                String activitySummary ="";
                cursor_gb = db.query(false,aTrackingHelperObject.name,new String[]{"DATE", "ACTIVITYTYPE"},"DATE=\""+cursor.getString(dateIndex)+"\"",null,null,null,null,null);
                cursor_gb.moveToFirst();//resets the iteration of results
                while (!cursor_gb.isAfterLast()){
                    if (activitySummary.contains(cursor_gb.getString(1)) == false){
                        activitySummary = activitySummary + cursor_gb.getString(1) + "&";
                    }
                    cursor_gb.moveToNext();
                }

                returnedInfor = "null" + "," + cursor.getString(dateIndex) + ","
                        + TextUtils.substring(activitySummary,0,activitySummary.length()-1) + ","
                        + cursor.getString(durationIndex) + "," + "null";
                infor.add(returnedInfor);
                cursor.moveToNext();
            }

        }
        else if (countRow > 0){   //List the item
            while(!cursor.isAfterLast())
            {
                returnedInfor = cursor.getString(idIndex) + "," + cursor.getString(dateIndex) + "," + cursor.getString(activityTypeIndex) + ","
                        + cursor.getString(durationIndex) + "," + cursor.getString(commentIndex);
                infor.add(returnedInfor);
                cursor.moveToNext();
            }
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
                ViewActivityTracking viewActivityTracking = (ViewActivityTracking)getFragmentManager().findFragmentByTag("summary");
                if (viewActivityTracking == null) {  //If in the summary fragment (the summary button is clicked), no action if an item is clicked
                    //Pass the clicked record to addOneActivityTracking activity
                    if (i>0){  //The activity tracking item is clicked
                        FragmentManager fm = getFragmentManager();;
                        FragmentTransaction transaction = fm.beginTransaction();
                        addActivityTracking fragmentAdd = new addActivityTracking();
                        FrameLayout frameLayoutTablet = getActivity().findViewById(R.id.fl_tablet);
                        if (frameLayoutTablet == null){  //In phone mode
                            transaction.replace(R.id.fl_toolbar,fragmentAdd,"update");
                        }else   //In tablet/landscape mode
                            transaction.replace(R.id.fl_tablet,fragmentAdd,"update");
                        transaction.commit();

                        String value = infor.get(i-1);
                        Bundle bundle = new Bundle();
                        bundle.putString("fragment","update");
                        bundle.putString("item",value);
                        fragmentAdd.setArguments(bundle);
                    }
                }

            }
        });
        return view;

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

    private class databaseQuery extends AsyncTask<String, Integer, String> {


        //onProgressUpdate method to update GUI.
        @Override
        protected void onProgressUpdate (Integer ... values) {
            super.onProgressUpdate(values);
           // progressBar.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

    }

        @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }

}
