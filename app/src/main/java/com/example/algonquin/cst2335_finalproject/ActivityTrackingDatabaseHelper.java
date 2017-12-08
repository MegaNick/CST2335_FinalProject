package com.example.algonquin.cst2335_finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by YUSHUWEI on 2017-12-06.
 */

public class ActivityTrackingDatabaseHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "ActivityTrackingDatabase.db";
    private final static int VERSION_NUM = 2;
    public final static String name = "activityTrackingTable";
    public final static String KEY_ID = "_id";
    public final static String KEY_date = "DATE";
    public final static String KEY_ACTIVITYTYPE = "ACTIVITYTYPE";
    public final static String KEY_DURATION = "DURATION";
    public final static String KEY_COMMENT = "COMMENT";

    public ActivityTrackingDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + name + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, DATE text, ACTIVITYTYPE text, DURATION text, COMMENT text);");
        Log.i("ActivityTrackingDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ name); //delete what was there previously
        this.onCreate(db);
        Log.i("ActivityTrackingDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        Log.i("ActivityTrackingDatabaseHelper", "onOpen was called");
    }

}
