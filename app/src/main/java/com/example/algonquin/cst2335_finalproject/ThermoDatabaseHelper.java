//To the Glory of God!
// Thermostat activity by Nikolay Melnik. Algonquin College. Ottawa, 2018.
//
package com.example.algonquin.cst2335_finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Thermostat Database Class. Operates the database for Thermostat activity
 * By Nikolay Melnik
 */
public class ThermoDatabaseHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "Thermostat.db";
    public final static int VERSION_NUM = 3;

    //DB columns
    public static final String TABLE_NAME = "rules_table ";
    public static final String KEY_ID = "key_id";
    public static final String WEEK_DAY = "week_day";
    public static final String TIME_HRS = "time_hrs";
    public static final String TIME_MIN = "time_min";
    public static final String TEMPERATURE = "temperature";


    //SQL queries
    //
    //Create Database
    public static final String CREATE_THERMOSTAT_DATABASE =
            "create table " + TABLE_NAME + " ("+
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    WEEK_DAY + " INTEGER,"+
                    TIME_HRS + " INTEGER,"+
                    TIME_MIN + " INTEGER,"+
                    TEMPERATURE + " INTEGER"+
                    ");";
    //On upgrade query
    public static final String ON_UPGRADE_QUERY =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    //On upgrade query
    public static final String ON_DOWNGRADE_QUERY =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    /**
     * Default constructor
     * @param ctx
     */
    public ThermoDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * Creates database for Thermostat
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_THERMOSTAT_DATABASE);
//        Log.i("ChatDatabaseHelper", "Calling onCreate");

    }

    /**
     * Method for upgrading the database
     * @param db
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    db.execSQL(ON_UPGRADE_QUERY);
    this.onCreate(db);
 //       Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + i + " newVersion=" + i1);
    }

    /**
     * Method for downgrading the database
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        db.execSQL(ON_DOWNGRADE_QUERY);
        this.onCreate(db);
        //       Log.i("ChatDatabaseHelper", "Calling onDowngrade, oldVersion=" + i + " newVersion=" + i1);

    }
}

