package com.example.algonquin.cst2335_finalproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class addActivityTracking extends Fragment implements AdapterView.OnItemSelectedListener {

    private String[] activityType;
    private long idInDB;
    private Button bt_add;
    private Button bt_delete;
    private Button bt_update;
    private EditText et_duration, et_comment;
    private static TextView tv_date;
    private TextView tv_datePicker;
    private SQLiteDatabase db;
    private View view;
    private int activity; //The position in the activity array for an activity
//    private String activity; //,date,comment, duration;
//    private static String date;

    public addActivityTracking() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_activity_tracking, container, false);
        //view.setBackgroundColor(Color.YELLOW);
        //The spinner's values
        activityType= new String[]{
                getActivity().getString(R.string.Running),
                getActivity().getString(R.string.Walking),
                getActivity().getString(R.string.Biking),
                getActivity().getString(R.string.Swimming),
                getActivity().getString(R.string.Skating)
        };

        //Declare the variable
        bt_add = (Button)view.findViewById(R.id.button_add);
        bt_delete = view.findViewById(R.id.button_delete);
        et_duration = view.findViewById(R.id.editText_amountOfTime);
        et_comment = view.findViewById(R.id.editText_comment);
        bt_update = view.findViewById(R.id.button_update);
        tv_date = view.findViewById(R.id.textView_valueOfDate);
        tv_datePicker = view.findViewById(R.id.textView_datePicker);

        //Get current date
        final Calendar c = Calendar.getInstance();
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        date = df.format(c.getTime());

        //Set up the spinner of activity type.
        Spinner spin = (Spinner) view.findViewById(R.id.spinner_selectOneActivity);
        //Load the spinner of activity type.
        spin.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,activityType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        //Get the bundle value.
        if (getArguments().getString("fragment") == "update"){ //Update and delete layout
            //Set update and delete button visible, and add invisible.
            bt_update.setVisibility(View.VISIBLE);
            bt_delete.setVisibility(View.VISIBLE);
            bt_add.setVisibility(View.INVISIBLE);

            String[] separated = getArguments().getString("item").split(","); // get the string at position
            //Set the spinner of activity type to the clicked item in viewActivityTracking.
            //Find the item position and set the value of spinner
            switch (separated[2]){
                case ("0"): spin.setSelection(0);
                    break;
                case "1": spin.setSelection(1);
                    break;
                case "2": spin.setSelection(2);
                    break;
                case "3": spin.setSelection(3);
                    break;
                case "4": spin.setSelection(4);
                    break;
            }

            //Set the value of duration, date and comment.
            idInDB = Long.parseLong(separated [0]);
            tv_date.setText(separated[1]);
            if (separated.length>=4){
                et_duration.setText(separated[3]);
            }else {
                et_duration.setText("");  //duration is null
            }
            if (separated.length>=5)
                et_comment.setText(separated[4]);
            else
                et_comment.setText("");   //comment is null

        }
        else if (getArguments().getString("fragment") == "add"){ //add item to show the layout
            //Set update and delete button invisible, and add visible.
            bt_update.setVisibility(View.INVISIBLE);
            bt_delete.setVisibility(View.INVISIBLE);
            bt_add.setVisibility(View.VISIBLE);
            tv_date.setText(df.format(c.getTime()));
        }

        final ActivityTrackingDatabaseHelper aTrackingHelperObject = new ActivityTrackingDatabaseHelper(getActivity());
        db = aTrackingHelperObject.getWritableDatabase();

        //Add item
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a object for inserting data to a SQLite database
/*
                ContentValues newData = new ContentValues();
                newData.put("DATE", df.format(c.getTime()));
                newData.put("ACTIVITYTYPE", activity);
                newData.put("DURATION", et_duration.getText().toString());
                newData.put("COMMENT", et_comment.getText().toString());
*/
                long rowInserted = db.insert(aTrackingHelperObject.name, "", contentValues());
                if(rowInserted >= 1){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), getActivity().getString(R.string.addTrackingSucc), Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), getActivity().getString(R.string.addTrackingFail), Toast.LENGTH_SHORT);
                    toast.show();
                }

                //Clear the input
                et_duration.setText("");
                et_comment.setText("");

                //Close current fragment and then get to activity list view
                getActivity().getFragmentManager().beginTransaction().remove(addActivityTracking.this).commit();
                getActivity().findViewById(R.id.action_two).performClick();

            }
        });

        //Delete item
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the dialog when an item is clicked.
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getActivity().getString(R.string.deleteTrackingConfirm));
                // Add the buttons
                builder.setPositiveButton(getActivity().getString(R.string.deleteTrackingConfirmYes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        //Execute sql query to remove from database
//                        db.execSQL("DELETE FROM " + aTrackingHelperObject.name + " WHERE " + aTrackingHelperObject.KEY_ID + "=" + idInDB + ";");
                        long delete = db.delete(aTrackingHelperObject.name,aTrackingHelperObject.KEY_ID + "=" + idInDB,null);
                        if (delete >= 1 ){
                            Snackbar.make(getActivity().findViewById(android.R.id.content), getActivity().getString(R.string.deleteTrackingConfirmYesAlert), Snackbar.LENGTH_LONG)
                                    .show();
                        }else {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), getActivity().getString(R.string.deleteTrackingConfirmNoAlert), Snackbar.LENGTH_LONG)
                                    .show();
                        }
                        //Close current fragment and then get to activity list view
                        getActivity().getFragmentManager().beginTransaction().remove(addActivityTracking.this).commit();
                        getActivity().findViewById(R.id.action_two).performClick();
                    }
                });
                builder.setNegativeButton(getActivity().getString(R.string.deleteTrackingConfirmNo), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //Update item
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a object for inserting data to a SQLite database
                long update = db.update(aTrackingHelperObject.name,contentValues(),aTrackingHelperObject.KEY_ID + "=" + idInDB,null);
                if(update >= 1){
                    Toast.makeText(getActivity().getApplicationContext(), getActivity().getString(R.string.updateTrackingSucc), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), getActivity().getString(R.string.updateTrackingFail), Toast.LENGTH_SHORT).show();
                }

                //Clear the input
                et_duration.setText("");
                et_comment.setText("");

                //Close current fragment and then get to activity list view
                getActivity().getFragmentManager().beginTransaction().remove(addActivityTracking.this).commit();
                getActivity().findViewById(R.id.action_two).performClick();

            }
        });

        //Date picker to select a new date
        tv_datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        return view;
    }

    //Get current new/updated record
    private ContentValues contentValues (){
        ContentValues data = new ContentValues();
        data.put("DATE", tv_date.getText().toString());
        data.put("ACTIVITYTYPE", String.valueOf(activity));
        data.put("DURATION", et_duration.getText().toString());
        data.put("COMMENT", et_comment.getText().toString());

        return data;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //activity = activityType[i];
        activity = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    /* https://developer.android.com/guide/topics/ui/controls/pickers.html */
    //Class of date picker
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user and change to format "yyyy-MM-dd"
            final Calendar c = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = c.getTime();
            String dtStar = year + "-" + (month+1) + "-" + day; // beware of month indexing from zero
            try {
                date = format.parse(dtStar);
            }catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            tv_date.setText(format.format(date));
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }
}
