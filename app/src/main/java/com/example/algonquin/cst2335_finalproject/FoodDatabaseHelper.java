package com.example.algonquin.cst2335_finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FoodDatabaseHelper extends SQLiteOpenHelper {
        protected static String DATABASE_NAME = "EatFood1.db";
        protected static int VERSION_NUM = 1;
        public final static String Key_ID = "_id";
        public final static String KEY_FOOD = "Food";
        public static final String name = "FoodTable";

        public FoodDatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, VERSION_NUM);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + name + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, Food text);");
            Log.i("FoodDatabaseHelper", "Calling onCreate");
        }

        @Override
        public void onOpen(SQLiteDatabase db){
            Log.i("FoodDatabaseHelper", "Calling onOpen");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + name);
            onCreate(db);
            Log.i("FoodDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
        }
    }



