package com.example.algonquin.cst2335_finalproject;

import android.app.Activity;
import android.content.ContentValues;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.algonquin.cst2335_finalproject.FoodDatabaseHelper.KEY_FOOD;

public class FoodList extends Activity {

    private ArrayList<String> foodArray = new ArrayList<>();
    private ListView foodListView;
    private EditText enterFood;
    private Button editFoodButton;
    private Button deleteFoodButton;
    private Button addFoodButton;
    private SQLiteDatabase writeableDB;
    private Cursor c;
    private static final String ACTIVITY_NAME = "FoodList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        foodListView = findViewById(R.id.foodListView);
        enterFood = findViewById(R.id.enterFood);
        addFoodButton = findViewById(R.id.addFood);
        editFoodButton = findViewById(R.id.editFood);
        deleteFoodButton = findViewById(R.id.deleteFood);

        final FoodDatabaseHelper fdHelper = new FoodDatabaseHelper(this);
        writeableDB = fdHelper.getWritableDatabase();
        final FoodAdapter foodAdapter =new FoodAdapter( this );
        foodListView.setAdapter(foodAdapter);
        c = writeableDB.rawQuery("select * from " + fdHelper.name,null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            int i = c.getColumnIndex(KEY_FOOD);
            foodArray.add(c.getString(i));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + c.getString(i));
            c.moveToNext();
        }

        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEnter = enterFood.getText().toString();
                ContentValues cValues = new ContentValues();
                foodArray.add(userEnter);
                cValues.put(KEY_FOOD,userEnter);
                writeableDB.insert(fdHelper.name,null,cValues);
                foodAdapter.notifyDataSetChanged();
                enterFood.setText("");
            }
        });

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent nutritionDetails = new Intent(FoodList.this, NutritionDetails.class);
                //phoneIntent.putExtra("bundle",bd);
                //startActivityForResult(phoneIntent,requestCode);
                startActivity(nutritionDetails);
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
    }
}
