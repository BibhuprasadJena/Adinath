package com.release.eztoll.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bibhu on 31-01-2018.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ADINATH";
    public static final String USER_TABLE = "USER";
    public static final String TRANSACTION_TABLE = "PRICE_INFO";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_QUERY);
        db.execSQL(USER_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_QUERY);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE);
        onCreate(db);
    }

    /**
     * This query is responsible to create user table .
     */
    private static final String USER_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + USER_TABLE
                    + " (Name TEXT,Password TEXT,Email TEXT,Token TEXT,ProfileImage TEXT,EmpId TEXT,Id TEXT)";




    /**
     * This query is responsible to create transaction table .
     */
    private static final String USER_TRANSACTION = "CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE + "(Id TEXT,Amount TEXT,TranStatus TEXT,Date TEXT)";
}
