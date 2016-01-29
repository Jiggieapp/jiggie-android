package com.android.jiggie.component.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rangg on 23/12/2015.
 */
public class DatabaseConnection extends SQLiteOpenHelper {
    public static final String NAME = "jiggie.db";
    public static final int VERSION = 1;

    public DatabaseConnection(Context context) { super(context, NAME, null, VERSION); }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
    @Override
    public void onCreate(SQLiteDatabase db) { ChatTable.create(db); }
}
