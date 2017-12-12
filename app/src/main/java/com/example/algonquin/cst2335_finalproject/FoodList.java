package com.example.algonquin.cst2335_finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;



public class FoodList extends Activity {

    private static final String ACTIVITY_NAME = "FoodList";
    private ArrayList<String> foodArray = new ArrayList<>();
    private ListView foodListView;
    private Button addFoodButton;
    private FoodDatabaseHelper fdHelper;
    private SQLiteDatabase writeableDB;
    private FoodAdapter foodAdapter;
    private Cursor c;
    private Boolean onPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        foodListView = findViewById(R.id.foodListView);
        addFoodButton = findViewById(R.id.addFood);

        foodAdapter =new FoodAdapter( this );
        foodListView.setAdapter(foodAdapter);

        fdHelper = new FoodDatabaseHelper(this);
        writeableDB = fdHelper.getWritableDatabase();
        c = writeableDB.rawQuery("select * from " + fdHelper.tableName,null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            int i = c.getColumnIndex(FoodDatabaseHelper.Key_FOOD);
            foodArray.add(c.getString(i));
            c.moveToNext();
        }

        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nutritionDetails = new Intent(FoodList.this, NutritionDetails.class);
                startActivity(nutritionDetails);
            }
        });

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private class FoodAdapter extends ArrayAdapter<String> {
        public FoodAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return foodArray.size();
        }

        public String getItem(int position){
            return foodArray.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = FoodList.this.getLayoutInflater();
            View result = null ;
            result = inflater.inflate(R.layout.food_eaten, null);
            TextView message = (TextView)result.findViewById(R.id.food_info);
            message.setText(   getItem(position)  ); // get the string at position
            return result;
        }

        public long getItemId(int position){
            c.moveToPosition(position);
            String x;
            x = c.getString(c.getColumnIndex(FoodDatabaseHelper.Key_ID));
            return Long.parseLong(x);
        }
    }
}
