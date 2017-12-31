package com.example.algonquin.cst2335_finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class AutomobileDatabaseHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "AutomobileDatabse.db";
    public static int VERSION_NUM = 2;

    public static String name = "AutomobileTable";
    public final static String KEY_ID = "_id";
    public final static String KEY_DATE = "Date";
    public final static String KEY_GASPRICE = "Gas_price";
    public final static String KEY_VOLUME = "Gas_Volume";

    public AutomobileDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    public static final String CREATE_MESSAGES_DATABASE =
            "create table " + name + " ("+
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    KEY_DATE + " TEXT,"+
                    KEY_GASPRICE + " TEXT,"+
                    KEY_VOLUME + " TEXT"+
                    ");";


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("test_query", CREATE_MESSAGES_DATABASE);
        sqLiteDatabase.execSQL(CREATE_MESSAGES_DATABASE);
        Log.i("test_query", CREATE_MESSAGES_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        Log.i("test_query", CREATE_MESSAGES_DATABASE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ name);
        this.onCreate(sqLiteDatabase);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        super.onDowngrade(db, oldVersion, newVersion);
        db.execSQL("DROP TABLE IF EXISTS "+ name);
        this.onCreate(db);
    }
}
