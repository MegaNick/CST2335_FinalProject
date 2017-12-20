package com.example.algonquin.cst2335_finalproject;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

public class FoodList extends AppCompatActivity {

    private ArrayList<FoodInformation> foodArray = new ArrayList<>();
    private ListView foodListView;
    private Button addFoodButton;
    private FoodDatabaseHelper fdHelper;
    private SQLiteDatabase db;
    private FoodAdapter foodAdapter;
    private Cursor c;
    private FrameLayout tabletLayOut;
    private Boolean onTablet;
    private Toolbar toolbar;
    private ProgressBar pBar;
    private Long singleId;
    private int position;
    private Boolean clickAdd;
    private Handler handler;
    private int timerCounter;
    private Runnable runnable;
    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        pBar = findViewById(R.id.progressBar);
        foodListView = findViewById(R.id.foodListView);
        addFoodButton = findViewById(R.id.addFood);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabletLayOut=findViewById(R.id.tableFrameLayout);
        if(tabletLayOut == null) onTablet=false;
        else onTablet=true;

        foodAdapter =new FoodAdapter(this);
        foodListView.setAdapter(foodAdapter);

        timerCounter=0;
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                if(timerCounter<100) {
                    addFoodButton.setEnabled(false);
                    //This idea is taken from: https://stackoverflow.com/questions/36918219/how-to-disable-user-interaction-while-progressbar-is-visible-in-android
                    //How to disable user interaction while ProgressBar is visible in android?
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                         WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (timerCounter == 0)
                        Toast.makeText(FoodList.this, "Loading Database...Please Wait", Toast.LENGTH_LONG).show();
                    if (timerCounter == 25)
                        Toast.makeText(FoodList.this, "Loading Database...Please Wait", Toast.LENGTH_SHORT).show();
                    if (timerCounter == 50)
                        Toast.makeText(FoodList.this, "Loading Database...Please Wait", Toast.LENGTH_SHORT).show();
                    if (timerCounter == 75)
                        Toast.makeText(FoodList.this, "Loading Database...Please Wait", Toast.LENGTH_SHORT).show();
                }
                if(timerCounter==100) {
                    addFoodButton.setEnabled(true);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(FoodList.this, "Loading Complete! Activity fully functional", Toast.LENGTH_LONG).show();
                }
                timerCounter++;
                handler.postDelayed(this,90);
            }
        };
        handler.postDelayed(runnable, 1000);

        BackgroundRun br = new BackgroundRun();
        br.execute();

        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAdd = true;
                Bundle bd = new Bundle();
                bd.putInt("TODO",1);

                if(onTablet==true){
                    NutritionDetails nd = new NutritionDetails(FoodList.this);
                    nd.setArguments(bd);
                    FragmentTransaction ft =  getFragmentManager().beginTransaction();
                    ft.replace(R.id.tableFrameLayout,nd);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else{
                    Intent phoneIntent = new Intent(FoodList.this, NutritionOnPhone.class);
                    phoneIntent.putExtra("bundle",bd);
                    startActivityForResult(phoneIntent,21);
                }
            }
        });

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickAdd = false;
                position = i;
                singleId = l;
                FoodInformation fi = foodArray.get(i);
                String [] tempArray = new String[7];
                tempArray[0] = fi.food;
                tempArray[1] = fi.day;
                tempArray[2] = fi.hour;
                tempArray[3] = fi.minute;
                tempArray[4] = fi.calories;
                tempArray[5] = fi.totalFat;
                tempArray[6] = fi.totalCarbohydrate;

                Bundle foodBundle = new  Bundle();
                foodBundle.putInt("TODO", 2);
                foodBundle.putStringArray("foodInfo", tempArray);
                foodBundle.putInt("viewPosition",position);
                foodBundle.putLong("id",singleId);

                if(onTablet==true){
                    NutritionDetails nd = new NutritionDetails(FoodList.this);
                    nd.setArguments(foodBundle);
                    FragmentTransaction ft =  getFragmentManager().beginTransaction();
                    ft.replace(R.id.tableFrameLayout,nd);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else{
                    Intent phoneIntent = new Intent(FoodList.this, NutritionOnPhone.class);
                    phoneIntent.putExtra("bundle",foodBundle);
                    startActivityForResult(phoneIntent,21);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.action_one:
                total = 0;
                for(int i = 0; i<foodArray.size(); i++){
                    c.moveToPosition(i);
                    total += Integer.parseInt(c.getString(c.getColumnIndex(FoodDatabaseHelper.Key_Calories)));
                }

                final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                View view =inflater.inflate(R.layout.custom_dialog_food, null);

                TextView item1Title = view.findViewById(R.id.item_title);
                item1Title.setText(getString(R.string.item1_title));

                TextView item1BodyMessage = view.findViewById(R.id.item_body_message);
                item1BodyMessage.setText(getString(R.string.item1_body_message) + " " + total);
                item1BodyMessage.setTypeface(null, Typeface.BOLD);
                item1BodyMessage.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

                builder1.setView(view)
                        .setPositiveButton(R.string.dialog_ok_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {}
                        });
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
                break;

            case R.id.action_two:
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                LayoutInflater inflater2 = this.getLayoutInflater();
                View view2 =inflater2.inflate(R.layout.custom_dialog_food, null);

                TextView item2Title = view2.findViewById(R.id.item_title);
                item2Title.setText((getString(R.string.item2_title)));

                TextView item2BodyMessage = view2.findViewById(R.id.item_body_message);
                item2BodyMessage.setText(getString(R.string.item2_body_message));
                item2BodyMessage.setTypeface(null, Typeface.BOLD);
                item2BodyMessage.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

                builder2.setView(view2)
                        .setPositiveButton(R.string.dialog_ok_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {}
                        });
                AlertDialog dialog2 = builder2.create();
                dialog2.show();
                break;

            case R.id.action_three:
                final AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                LayoutInflater inflater3 = this.getLayoutInflater();
                View view3 =inflater3.inflate(R.layout.custom_dialog_food, null);

                TextView item3Title = view3.findViewById(R.id.item_title);
                item3Title.setText((getString(R.string.item3_title)));

                TextView item3BodyMessage = view3.findViewById(R.id.item_body_message);
                item3BodyMessage.setText(getString(R.string.item3_body_message));
                item3BodyMessage.setTextSize(18);

                builder3.setView(view3)
                        .setPositiveButton(R.string.dialog_ok_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {}
                        });
                AlertDialog dialog3 = builder3.create();
                dialog3.show();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            String[] extraInfo = data.getStringArrayExtra("saveInfo");
            FoodInformation recordInfo = new FoodInformation(extraInfo[0], extraInfo[1], extraInfo[2], extraInfo[3], extraInfo[4],
                    extraInfo[5],extraInfo[6]);
            saveInfo(recordInfo);
        }
        else if (resultCode ==2) {
            Bundle args = data.getBundleExtra("forDelete");
            Long keyID = args.getLong("id");
            int viewPosition = args.getInt("viewPosition");
            deleteInfo(keyID, viewPosition);
        }
    }

    public void saveInfo(FoodInformation foodTemp){
        ContentValues values = new ContentValues();
        values.put(FoodDatabaseHelper.Key_FOOD, foodTemp.food);
        values.put(FoodDatabaseHelper.Key_Day, foodTemp.day);
        values.put(FoodDatabaseHelper.Key_Hour, foodTemp.hour);
        values.put(FoodDatabaseHelper.Key_Minute, foodTemp.minute);
        values.put(FoodDatabaseHelper.Key_Calories, foodTemp.calories);
        values.put(FoodDatabaseHelper.Key_Fat, foodTemp.totalFat);
        values.put(FoodDatabaseHelper.Key_Carbohydrate, foodTemp.totalCarbohydrate);

        if(clickAdd==true){
            db.insert(FoodDatabaseHelper.tableName, null, values);
            c = db.rawQuery("select * from " + fdHelper.tableName,null);
            foodArray.add(foodTemp);
            foodAdapter.notifyDataSetChanged();
            if(onTablet==true)
                Snackbar.make(findViewById(R.id.tabletXML), "A new food info is added", Snackbar.LENGTH_LONG).show();
            else
                Snackbar.make(findViewById(R.id.foodListLayout), "A new food info is added", Snackbar.LENGTH_LONG).show();
        }
        else{
            db.update(FoodDatabaseHelper.tableName, values, FoodDatabaseHelper.Key_ID+" = " + singleId, null);
            c = db.rawQuery("select * from " + fdHelper.tableName,null);
            foodArray.set(position,foodTemp);
            foodAdapter.notifyDataSetChanged();
            if(onTablet==true)
                Snackbar.make(findViewById(R.id.tabletXML), "Food info has been modified", Snackbar.LENGTH_LONG).show();
            else
                Snackbar.make(findViewById(R.id.foodListLayout), "Food info has been modified", Snackbar.LENGTH_LONG).show();
        }
        sortInfo();
        foodAdapter.notifyDataSetChanged();
    }

    public void deleteInfo(Long id, int viewPosition){
        db.delete(FoodDatabaseHelper.tableName, FoodDatabaseHelper.Key_ID + "=" + id, null);
        c = db.rawQuery("select * from " + fdHelper.tableName,null);
        foodArray.remove(viewPosition);
        foodAdapter.notifyDataSetChanged();
        if(onTablet==true)
            Snackbar.make(findViewById(R.id.tabletXML), "Food info has been removed", Snackbar.LENGTH_LONG).show();
        else
            Snackbar.make(findViewById(R.id.foodListLayout), "Food info has been removed", Snackbar.LENGTH_LONG).show();
    }

    public void sortInfo(){
        Collections.sort(foodArray, new CustomComparator());
    }

    public class CustomComparator implements Comparator<FoodInformation> {
        @Override
        public int compare(FoodInformation obj1, FoodInformation obj2) {
            if (dayIntoNumber(obj1.day) == dayIntoNumber(obj2.day))
                return (Integer.parseInt(obj1.hour) * 60 + Integer.parseInt(obj1.minute))
                        - (Integer.parseInt(obj2.hour) * 60 + Integer.parseInt(obj2.minute));
            else
                return dayIntoNumber(obj1.day) - dayIntoNumber(obj2.day);
        }

        private int dayIntoNumber(String a) {
            int i = 0;
            if (a.equals("Monday")) i = 1;
            else if (a.equals("Tuesday")) i = 2;
            else if (a.equals("Wednesday")) i = 3;
            else if (a.equals("Thursday")) i = 4;
            else if (a.equals("Friday")) i = 5;
            else if (a.equals("Saturday")) i = 6;
            else if (a.equals("Sunday")) i = 7;
            return i;
        }
    }

    private class FoodAdapter extends ArrayAdapter<FoodInformation> {
        public FoodAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return foodArray.size();
        }

        @Override
        public FoodInformation getItem(int position){
            return foodArray.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = FoodList.this.getLayoutInflater();
            View result = null ;
            result = inflater.inflate(R.layout.food_eaten, null);
            TextView message = result.findViewById(R.id.food_info);

            FoodInformation fi = getItem(position);
            String textOnView = fi.day+", ";
            if (Integer.parseInt(fi.hour)>0 && Integer.parseInt(fi.hour)<10) textOnView += "0";
            textOnView += fi.hour +":";
            if (Integer.parseInt(fi.minute)>0 && Integer.parseInt(fi.minute)<10) textOnView+="0";
            textOnView += fi.minute;
            textOnView = textOnView + "\nFood:" + fi.food + "\nCalories:"+fi.calories+ ", Total Fat:"
                    +fi.totalFat+"g\nTotal Carbohydrate:"+fi.totalCarbohydrate+"g";
            message.setText(textOnView);
            return result;
        }

        public long getItemId(int position){
            c.moveToPosition(position);
            String x;
            x = c.getString(c.getColumnIndex(FoodDatabaseHelper.Key_ID));
            return Long.parseLong(x);
        }
    }

    class BackgroundRun extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... args) {
            while (timerCounter<25);
            onProgressUpdate(25);

            fdHelper = new FoodDatabaseHelper(getApplicationContext());
            db = fdHelper.getWritableDatabase();
            c = db.rawQuery("select * from " + fdHelper.tableName,null);
            c.moveToFirst();

            while (timerCounter<50);
            onProgressUpdate(50);
           ;
            while(!c.isAfterLast()){
                FoodInformation fi = new FoodInformation();
                fi.food = c.getString(c.getColumnIndex(FoodDatabaseHelper.Key_FOOD));
                fi.day = c.getString(c.getColumnIndex(FoodDatabaseHelper.Key_Day));
                fi.hour = c.getString(c.getColumnIndex(FoodDatabaseHelper.Key_Hour));
                fi.minute = c.getString(c.getColumnIndex(FoodDatabaseHelper.Key_Minute));
                fi.calories = c.getString(c.getColumnIndex(FoodDatabaseHelper.Key_Calories));
                fi.totalFat = c.getString(c.getColumnIndex(FoodDatabaseHelper.Key_Fat));
                fi.totalCarbohydrate = c.getString(c.getColumnIndex(FoodDatabaseHelper.Key_Carbohydrate));

                while (timerCounter<75);
                onProgressUpdate(75);

                foodArray.add(fi);
                c.moveToNext();
            }

            while (timerCounter<100);
            onProgressUpdate(100);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer ...value){
            pBar.setProgress(value[0]);
            pBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s){
            pBar.setVisibility(View.INVISIBLE);
            sortInfo();
            foodAdapter.notifyDataSetChanged();
        }
    }
}