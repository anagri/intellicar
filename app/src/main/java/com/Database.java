package com;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;

public class Database extends SQLiteOpenHelper {

    private static Database database;
    public static int requestId = 0;

    public Database(Context context) {
        super(context, "smartcar", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table inputs(request_id integer not null, input text, record_time datetime default CURRENT_TIMESTAMP)");
        db.execSQL("create table outputs(request_id integer not null, input text, record_time datetime default CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table inputs;");
        db.execSQL("drop table outputs;");
        onCreate(db);
    }


    public static Database instance(Context context) {
        if (database == null)
            database = new Database(context);
        return database;
    }

    public void insertInput(int requestId, byte[] inputs) {
        insert("inputs", requestId, inputs);
    }

    public void insertOutput(int requestId, byte[] outputs) {
        insert("outputs", requestId, outputs);
    }

    private void insert(String table, int requestId, byte[] inputs) {
        ContentValues values = new ContentValues();
        values.put("request_id", requestId);
        values.put("input", Arrays.toString(inputs));
        database.getWritableDatabase().insert(table, null, values);
    }
}
