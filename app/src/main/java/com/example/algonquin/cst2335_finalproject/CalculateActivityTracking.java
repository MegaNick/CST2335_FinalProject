package com.example.algonquin.cst2335_finalproject;


import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalculateActivityTracking extends Fragment {

    SQLiteDatabase db;
    Cursor cursor;

    public CalculateActivityTracking() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculate_activity_tracking, container, false);
        //view.setBackgroundColor(Color.BLUE);

        TextView tv_perMonth = (TextView)view.findViewById(R.id.textView_minutes_month);
        TextView tv_lastMonth = (TextView)view.findViewById(R.id.textView_minutes_lastmonth);
        TextView tv_nameLastMonth = (TextView)view.findViewById(R.id.textView_lastmonth);

        //Get the data groud by month
        ActivityTrackingDatabaseHelper aTrackingHelperObject = new ActivityTrackingDatabaseHelper(getActivity());
        db = aTrackingHelperObject.getWritableDatabase();
        cursor = db.query(false,aTrackingHelperObject.name,new String[]{"substr(DATE,0,8) as mDATE","SUM(DURATION) as DURATION"},null,null,"mDATE",null,null,null);

        //Get the previous month
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        String lastMonth = format.format(cal.getTime());
        tv_nameLastMonth.setText(getActivity().getString(R.string.textView_lastmonth) + lastMonth + "): ");

        //Get message and add them to the arrayList
        int total = 0;
        int countRow = cursor.getCount();
        cursor.moveToFirst();//resets the iteration of results
        if (countRow > 0){  //data are available
            while(!cursor.isAfterLast())
            {
                if (lastMonth.equals(cursor.getString(0)) ){
                    tv_lastMonth.setText(cursor.getString(1)); //Set minutes of previous month
                }

                total = total + Integer.valueOf(cursor.getString(1));
                cursor.moveToNext();
            }
            int avg = total/countRow;
            tv_perMonth.setText(" " + String.valueOf(avg)); //Set minutes per month.
        }

        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

}
