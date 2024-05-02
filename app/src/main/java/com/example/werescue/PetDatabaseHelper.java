package com.example.werescue;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PetDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "petDatabase.db";
    private static final int DATABASE_VERSION = 6;

    public PetDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PETS_TABLE = "CREATE TABLE " + "Pets" + "("
                + "id" + " TEXT PRIMARY KEY," + "name" + " TEXT,"
                + "description" + " TEXT," + "gender" + " TEXT,"
                + "species" + " TEXT," + "birthday" + " TEXT,"
                + "location" + " TEXT," + "weight" + " INTEGER,"
                + "imagePath" + " TEXT," + "email" + " TEXT,"
                + "imageBitmap" + " BLOB" + ")";
        db.execSQL(CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "Pets");
        onCreate(db);
    }
}