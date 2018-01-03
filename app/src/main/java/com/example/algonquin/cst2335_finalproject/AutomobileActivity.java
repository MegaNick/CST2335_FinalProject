package com.example.algonquin.cst2335_finalproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class AutomobileActivity extends AppCompatActivity {
    public ListView automobileList;
    private AutoMobileAdapter aAdapter;
    public ArrayList<AutomobileInformation> listItems;
    public Button purchaseBT;
    public Button priceHistoryBT;
    public AutomobileDatabaseHelper adHelper;
    public SQLiteDatabase db;
    public int timeCounter;
    public ProgressBar progressBar;
    public Cursor c;
    public boolean isPhone;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile);

        if (findViewById(R.id.fragmentFrameLayout)==null){
            isPhone= true ;
        }else{
            isPhone = false;
        }
        Log.i("is Phone?", String.valueOf(isPhone));

        //Initialises stuff here
        listItems = new ArrayList<AutomobileInformation>();
        purchaseBT = (Button)findViewById(R.id.fuelCostB);
        priceHistoryBT = (Button)findViewById(R.id.fuelAnalysisButton);
        automobileList = (ListView) findViewById(R.id.automobileList);
        progressBar = findViewById(R.id.automobileProgBar);
        toolbar = findViewById(R.id.automobileToolbar);
        setSupportActionBar(toolbar);

        adHelper = new AutomobileDatabaseHelper(this);
        aAdapter = new AutoMobileAdapter(this);
        automobileList.setAdapter(aAdapter); //adapt the list

        Async async;

        async = new Async(this);
        async.execute("");


        Log.i("DONE DB", "yes");

        //Click on button
        automobileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AutomobileInformation tempInfo = listItems.get(i);
                String [] tempArray = new String [] {
                        tempInfo.time,
                        tempInfo.gasPrice,
                        tempInfo.gasVolume,
                        tempInfo.kiloOfGas
                };

                Bundle data = new Bundle();
                data.putStringArray("info",tempArray);
                data.putLong("ID",l);
                data.putInt("position",i);



               if (isPhone) {
                    Intent intent = new Intent (getApplicationContext(), AutomobileDetails.class);
                    intent.putExtra("package", data);
                    startActivityForResult(intent, 10);
                }else{
                   PurchaseGasActivity fragment = new PurchaseGasActivity(false);
                   fragment.setArguments(data);
                   while (getSupportFragmentManager().getBackStackEntryCount() > 0){
                       getSupportFragmentManager().popBackStackImmediate();
                   }
                   FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                   transaction.replace(R.id.fragmentFrameLayout, fragment);
                   transaction.addToBackStack(null);
                   transaction.commit();
               }
            }
        });

        purchaseBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putInt("addButtonClicked",1);

                Snackbar snackbar = Snackbar.make(view,getResources().getString(R.string.addSnackbar),Snackbar.LENGTH_LONG);
                snackbar.show();

                if (isPhone){
                    Intent intent = new Intent(getApplicationContext(),AutomobileDetails.class);
                    intent.putExtra("package",data);
                    startActivityForResult(intent, 10);

                }else{
                    PurchaseGasActivity fragment = new PurchaseGasActivity(false);
                    fragment.setArguments(data);
                    while (getSupportFragmentManager().getBackStackEntryCount() > 0){
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentFrameLayout, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }


            }
        });

        priceHistoryBT.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                double [] tempArray = averageGasPrice(listItems);
                Bundle data = new Bundle();

                    Intent intent = new Intent(getApplicationContext(), GasPriceHistoryActivity.class);
                    data.putDoubleArray("displayArray",tempArray);
                    intent.putExtra("package",data);
                    startActivity(intent);


            }
        });
    }
    public void addEntry(AutomobileInformation tempInfo){
        ContentValues values = new ContentValues();
        values.put(AutomobileDatabaseHelper.KEY_DATE, tempInfo.time);
        values.put(AutomobileDatabaseHelper.KEY_GASPRICE, tempInfo.gasPrice);
        values.put(AutomobileDatabaseHelper.KEY_VOLUME, tempInfo.gasVolume);
        values.put(AutomobileDatabaseHelper.KEY_KMGAS, tempInfo.kiloOfGas);

        db.insert(AutomobileDatabaseHelper.name,null,values);
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        cursor.moveToFirst();
        cursor.close();

        listItems.add(tempInfo);
        sortInfo();
        aAdapter.notifyDataSetChanged();
    }

    public void deleteEntry (int position, long id){
       // Intent data = new Intent();
      //  Bundle deleteBundle = data.getBundleExtra("secondClass");
      //  int position = deleteBundle.getInt("position");
      //  long id = deleteBundle.getLong("ID");

        listItems.remove(position);
        db.delete(AutomobileDatabaseHelper.name, AutomobileDatabaseHelper.KEY_ID + "=" + id, null);
        aAdapter.notifyDataSetChanged();

    }

    public void  editEntry(int position, long id,AutomobileInformation tempInfo){

        ContentValues values = new ContentValues();
        values.put(AutomobileDatabaseHelper.KEY_DATE, tempInfo.time);
        values.put(AutomobileDatabaseHelper.KEY_GASPRICE, tempInfo.gasPrice);
        values.put(AutomobileDatabaseHelper.KEY_VOLUME, tempInfo.gasVolume);
        values.put(AutomobileDatabaseHelper.KEY_KMGAS, tempInfo.kiloOfGas);

        db.update(AutomobileDatabaseHelper.name, values,AutomobileDatabaseHelper.KEY_ID + "=" + id, null);
        listItems.set(position, tempInfo);
        sortInfo();
        aAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==10){
            if (data==null) return;
            Bundle bundle = data.getExtras();


            if (resultCode == 11){

                String [] tempArray = (String []) bundle.get("data");
                AutomobileInformation tempInfo = new AutomobileInformation(tempArray[0], tempArray[1], tempArray[2], tempArray[3]);

                ContentValues values = new ContentValues();
                values.put(AutomobileDatabaseHelper.KEY_DATE, tempInfo.time);
                values.put(AutomobileDatabaseHelper.KEY_GASPRICE, tempInfo.gasPrice);
                values.put(AutomobileDatabaseHelper.KEY_VOLUME, tempInfo.gasVolume);
                values.put(AutomobileDatabaseHelper.KEY_KMGAS, tempInfo.kiloOfGas);

                db.insert(AutomobileDatabaseHelper.name,null,values);
                Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
                cursor.moveToFirst();
                cursor.close();

                listItems.add(tempInfo);
                sortInfo();
                aAdapter.notifyDataSetChanged();
            }
            //delete
            if (resultCode == 12){
                Bundle deleteBundle = data.getBundleExtra("secondClass");
                int position = deleteBundle.getInt("position");
                long id = deleteBundle.getLong("ID");

                listItems.remove(position);
                db.delete(AutomobileDatabaseHelper.name, AutomobileDatabaseHelper.KEY_ID + "=" + id, null);
                aAdapter.notifyDataSetChanged();

            }
            //edit
            if(resultCode == 13){
                long id = bundle.getLong("ID");
                int position = bundle.getInt("position");
                String [] tempArray = (String []) bundle.get("data");
                AutomobileInformation tempInfo = new AutomobileInformation(tempArray[0], tempArray[1], tempArray[2], tempArray[3]);

                ContentValues values = new ContentValues();
                values.put(AutomobileDatabaseHelper.KEY_DATE, tempInfo.time);
                values.put(AutomobileDatabaseHelper.KEY_GASPRICE, tempInfo.gasPrice);
                values.put(AutomobileDatabaseHelper.KEY_VOLUME, tempInfo.gasVolume);
                values.put(AutomobileDatabaseHelper.KEY_KMGAS, tempInfo.kiloOfGas);

                db.update(AutomobileDatabaseHelper.name, values,AutomobileDatabaseHelper.KEY_ID + "=" + id, null);
                listItems.set(position, tempInfo);
                sortInfo();
                aAdapter.notifyDataSetChanged();
            }

            if (resultCode == RESULT_CANCELED){

            }
        }

    }

    public void sortInfo() {
        Collections.sort(listItems, new AutomobileComparator());}

    public class  AutomobileComparator implements Comparator<AutomobileInformation> {

        @Override
        public int compare(AutomobileInformation obj1, AutomobileInformation obj2){
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
            long sortDay = 0;
            try {
                Date day1 = myFormat.parse(obj1.time);
                Date day2 = myFormat.parse((obj2.time));
                sortDay = day2.getTime() - day1.getTime();

            }catch (ParseException e){

            }
            return (int)sortDay;
        }
    }

    /**
     * Perform calculation for gas prices (30days) and monthly
     */
    public double [] averageGasPrice(ArrayList<AutomobileInformation> listItems){
        double counter = 0;
        double gasPrice = 0;
        double gasVolume = 0;
        double [] gasPriceMonth = new double[12];
        double [] returnArray = new double[14];

        AutomobileInformation automobileInformation = new AutomobileInformation();
        String time;

        if (listItems.isEmpty()) {
            return new double[14];
        }

        LocalDate now = LocalDate.now();

        for (int i = 0; i<listItems.size(); i++){

            for (int j = 0; j<=30; j++){

                time = automobileInformation.parseYearMonth(listItems.get(i).time);
                LocalDate prevDay = now.minusDays(j);

                Log.i("Parsed from object", time);
                Log.i("prevDay", prevDay.toString());

                if (time.equals(prevDay.toString())){
                    gasPrice = gasPrice + Double.valueOf(listItems.get(i).gasPrice);
                    gasVolume = gasVolume + Double.valueOf(listItems.get(i).gasVolume);

                    counter++;
                }
            }
        }

        //for $$ every month

        for (int i = 0; i<listItems.size();i++){

            int yearInInt = LocalDate.now().getYear(); //current year
            String year = automobileInformation.parseYear(listItems.get(i).time); //year of the entry
            String month = automobileInformation.parseMonth(listItems.get(i).time); //month of the entry


            if (yearInInt == Integer.valueOf(year) ){
                for (int j = 0; j <= 11; j++){
                    if (Integer.valueOf(month) == j+1){
                        gasPriceMonth[j] = gasPriceMonth[j] + Double.valueOf(listItems.get(i).gasVolume);
                    }
                }
            }

        }

        Log.i("value of gas price", Double.toString(gasPrice));
        Log.i("value of counter", Double.toString(counter));
        returnArray[0] = gasPrice/counter;
        returnArray[1] = gasVolume;

        for (int i = 0; i<gasPriceMonth.length;i++){
            returnArray[i+2]=gasPriceMonth[i];
        }

        return returnArray;

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
    }


    public class Async extends AsyncTask<String, Integer, String>{
        Activity activity;

        public Async(Activity activity){
            this.activity=activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            timeCounter = 0;
            progressBar.setActivated(true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ProgressBar pbar;
            pbar = activity.findViewById(R.id.automobileProgBar);
            pbar.setVisibility(View.INVISIBLE);
            sortInfo();
            aAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int x = values[0];
            ProgressBar pbar;
            pbar = activity.findViewById(R.id.automobileProgBar);
            pbar.setVisibility(View.VISIBLE);
            pbar.setProgress(x);
        }

        @Override
        protected String doInBackground(String... strings) {

            onProgressUpdate(25);

           if (db == null){
               db = new AutomobileDatabaseHelper(getApplicationContext()).getReadableDatabase();
               Log.i("creating database", "yes");
           }

            Cursor cursor = db.rawQuery("SELECT * FROM "+AutomobileDatabaseHelper.name, null);
            cursor.moveToFirst();
            listItems.clear();
            timeCounter = 0;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //while (timeCounter<1);
            onProgressUpdate(50);

            while(!cursor.isAfterLast()){
                AutomobileInformation tempInfo = new AutomobileInformation();

                tempInfo.time = cursor.getString(cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_DATE));
                tempInfo.gasPrice = cursor.getString(cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_GASPRICE));
                tempInfo.gasVolume = cursor.getString(cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_VOLUME));
                tempInfo.kiloOfGas = cursor.getString(cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_KMGAS));

                listItems.add(tempInfo);
                cursor.moveToNext();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //timeCounter = 0;
           // while (timeCounter<2);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onProgressUpdate(1000);

           // timeCounter = 0;
           // while (timeCounter<3);
            cursor.close();


            return null;
        }
    }



    private class AutoMobileAdapter extends ArrayAdapter<AutomobileInformation>{

        public AutoMobileAdapter(Context context){
            super(context,0);
        }

        public int getCount(){
            return listItems.size();
        }

        public long getItemId(int position){
            c = db.rawQuery("select * from " + AutomobileDatabaseHelper.name,null);
            c.moveToPosition(position);
            String x;
            x = c.getString(c.getColumnIndex(AutomobileDatabaseHelper.KEY_ID));
            return Long.parseLong(x);
        }

        public AutomobileInformation getItem(int position){
            return listItems.get(position);
       }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = AutomobileActivity.this.getLayoutInflater();
            View result = null;
            result = inflater.inflate(R.layout.automobile_display,null);

            AutomobileInformation automobileInformation = getItem(position);

            String [] date = automobileInformation.parseDate(automobileInformation.time);
            /*String displayText = "Time of Purchase: "+
                    "\nYear: "+date[0] +
                    "\n"+ date[1] +
                    " " + date[2]+
                    "\nHour: "+date[3]+
                    "\nMinutes: " +date[4]+
                    "\nSeconds: " +date[5]+
                    "\n"
                    +  "Gas Purchased At: " + automobileInformation.gasPrice + "$\n"
                    + "Liters of gas Purchased: "+ automobileInformation.gasVolume + "L";*/

            String displayText = getResources().getString(R.string.timeOfPurchase) + "\n"+automobileInformation.time
                    + "\n" + getResources().getString(R.string.gasPurchasedAt) + "\n"+ automobileInformation.gasPrice + " $"
                    + "\n" + getResources().getString(R.string.LitersOfGas) + "\n"+ automobileInformation.gasVolume + " L"
                    + "\n" + getResources().getString(R.string.kmOfGasDisplay) + "\n" + automobileInformation.kiloOfGas + "km";

            TextView displayTextView = (TextView) result.findViewById(R.id.automobileDisplay);
            displayTextView.setText(displayText);

            return result;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        super.onCreateOptionsMenu(m);
        getMenuInflater().inflate(R.menu.auto_toolbar_menu, m );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        super.onOptionsItemSelected(mi);
        int id = mi.getItemId();
        switch (id) {
            case R.id.action_one:
                //Activity Tracking
                Intent intent1 = new Intent(AutomobileActivity.this,DashBoardOfActivityTracking.class);
                startActivity(intent1);
                break;
            case R.id.action_two:
                //Nutrition Tracker
                Intent intent2 = new Intent(AutomobileActivity.this, FoodList.class);
                startActivity(intent2);
                break;
            case R.id.action_three:
                //Start Thermostat
                Intent intent4 = new Intent(AutomobileActivity.this, ThermostatActivity.class);
                startActivity(intent4);
                break;
            case R.id.action_four:
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle(R.string.autoHeader);
                builder.setMessage(getResources().getString(R.string.autoHelper));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                // Create the AlertDialog
                android.support.v7.app.AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return true;
    }

}

