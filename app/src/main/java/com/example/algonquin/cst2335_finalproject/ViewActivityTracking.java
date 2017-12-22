package com.example.algonquin.cst2335_finalproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.ArrayList;


public class ViewActivityTracking extends Fragment {
    ListView listView;
    ArrayList<String> infor;
    Cursor cursor;
    Cursor cursor_gb;
    SQLiteDatabase db;
    LayoutInflater inflater;
    View result, view;
    ProgressBar progressBar;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Progress bar
        progressBar=view.findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.VISIBLE);

        //Instance the Query with async task and call it.
        databaseQuery query = new databaseQuery();
        query.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_tracking, container, false);
        //view.setBackgroundColor(Color.CYAN);

        listView = view.findViewById(R.id.listView_trackingItem);
        Button bt_orderByDate = (Button)view.findViewById(R.id.button_orderByDate);
        Button bt_summary = (Button)view.findViewById(R.id.button_summary);

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

        //Put activityTrackingRecord into inflater, and set the header of ListView
        inflater = ViewActivityTracking.this.getLayoutInflater();
        result = inflater.inflate(R.layout.activitytrackingrecord, null);
        listView.addHeaderView(result);

        //Click listView to select an item
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

                        String value = infor.get(i-1);  //Get the string of this item. Because the first item is the header, use get(i-1)
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

            String[] separated = getItem(position).split(","); // get the string at the position
            result = inflater.inflate(R.layout.activitytrackingrecord, null);
            TextView tv_date = (TextView)result.findViewById(R.id.textView_dateAndTime);
            TextView tv_activityType = (TextView)result.findViewById(R.id.textView_activityType);
            TextView tv_duration = (TextView)result.findViewById(R.id.textView_durationInMinutes);

            tv_date.setText(separated[1]); //set the date
            if (getArguments().getString("button") == "summary"){  //Perhaps more than one activity type
                String [] separated_activityType = separated[2].split("&");
                String activitySummary ="";
                for (int i = 0; i<separated_activityType.length; i++){
                    //Check the duplicated activity type and ignore it. or add it to the activitySummary
                    if (activitySummary.contains(positionToActivityType(separated_activityType[i])) == false){
                        activitySummary = activitySummary + positionToActivityType(separated_activityType[i]) + "&";
                    }
                }
                tv_activityType.setText(TextUtils.substring(activitySummary,0,activitySummary.length()-1)); //Take "," out
            }else
                tv_activityType.setText(positionToActivityType(separated[2]));   //set the activity type
//            tv_activityType.setText(separated[2]);   //set the activity type
            if (separated.length>=4)
               tv_duration.setText(separated[3]);  //set the duration
            else
                tv_duration.setText("");   //duration is null
            return result;
        }

    }

    //Check the position in the activity type array and return the activity type name
    private String positionToActivityType(String position){
        String type = null;
        switch (position){  //set the activity type
            case "0": type =  getActivity().getString(R.string.Running);
                break;
            case "1": type = getActivity().getString(R.string.Walking);
                break;
            case "2": type = getActivity().getString(R.string.Biking);
                break;
            case "3": type = getActivity().getString(R.string.Swimming);
                break;
            case "4": type = getActivity().getString(R.string.Skating);
                break;
        }
        return type;
    }


    private class databaseQuery extends AsyncTask<String, Integer, String> {
        ActivityTrackingAdapter messageAdapter;

        //do in background
        @Override
        protected String doInBackground(String... strings) {

            infor = new ArrayList<>();
            final ActivityTrackingDatabaseHelper aTrackingHelperObject = new ActivityTrackingDatabaseHelper(getActivity());
            final ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.activitytrackingrecord,infor);
            //Create a object to contain message
            messageAdapter =new ActivityTrackingAdapter(getActivity());
            db = aTrackingHelperObject.getWritableDatabase();

            //Data query from the database. If click to open the view, query the data order by ID;
            //if orderByTime button is clicked, query the data order by date;
            //if summary button is clicked, query the data group by date, and sum the duration.
            if (getArguments().getString("button") == "click")
                cursor = db.query(false,aTrackingHelperObject.name,new String[]{"_id","DATE","ACTIVITYTYPE","DURATION","COMMENT"},null,null,null,null,null,null);
            else if (getArguments().getString("button") == "orderByDate")
                cursor = db.query(false,aTrackingHelperObject.name,new String[]{"_id","DATE","ACTIVITYTYPE","DURATION","COMMENT"},null,null,null,null,"DATE",null);
            else if (getArguments().getString("button") == "summary")
                cursor = db.query(false,aTrackingHelperObject.name,new String[]{"DATE", "SUM(DURATION) as DURATION"},null,null,"DATE",null,null,null);


            //Get the index(what column in order starting from 0) of message in the table
            int idIndex =  cursor.getColumnIndex( aTrackingHelperObject.KEY_ID);
            int dateIndex =  cursor.getColumnIndex( aTrackingHelperObject.KEY_date);
            int activityTypeIndex = cursor.getColumnIndex( aTrackingHelperObject.KEY_ACTIVITYTYPE);
            int durationIndex =  cursor.getColumnIndex( aTrackingHelperObject.KEY_DURATION);
            int commentIndex = cursor.getColumnIndex( aTrackingHelperObject.KEY_COMMENT);

//        String n = cursor.getColumnName(1);
            //Get message and add them to the arrayList
            String returnedInfor;
            int countRow = cursor.getCount(), i=0;
            cursor.moveToFirst();//resets the iteration of results
            if (getArguments().getString("button") == "summary" && countRow > 0){  //summary the item by date
                while(!cursor.isAfterLast())
                {
                    String activitySummary ="";
                    //Query the activities group by the month that is taken in the above db query (summary button is clicked)
                    cursor_gb = db.query(false,aTrackingHelperObject.name,new String[]{"DATE", "ACTIVITYTYPE"},"DATE=\""+cursor.getString(dateIndex)+"\"",null,null,null,null,null);
                    cursor_gb.moveToFirst();//resets the iteration of results
                    while (!cursor_gb.isAfterLast()){
                        //Check the duplicated activity type and ignore it. or add it to the activitySummary
                        if (activitySummary.contains(cursor_gb.getString(1)) == false){
                            activitySummary = activitySummary + cursor_gb.getString(1) + "&";
                        }
                        cursor_gb.moveToNext();
                    }

                    returnedInfor = "null" + "," + cursor.getString(dateIndex) + ","
                            + TextUtils.substring(activitySummary,0,activitySummary.length()-1) + ","
                            + cursor.getString(durationIndex) + "," + "null";
                    infor.add(returnedInfor);
                    publishProgress(i/countRow);
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
            return null;
        }

        //onProgressUpdate method to update GUI.
        @Override
        protected void onProgressUpdate (Integer ... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        //Execute
        @Override
        protected  void onPostExecute (String result){
            super.onPostExecute(result);
            //Put information adapter to listView to display the activities.
            listView.setAdapter (messageAdapter);

            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }

}