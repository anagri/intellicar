package com.arundavid.odbread;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.Database;

public class DbActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_db, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Database instance = Database.instance(this);
        SQLiteDatabase database = instance.getReadableDatabase();
        Log.d("DBAPP", "Total input rows " + DatabaseUtils.longForQuery(database, "select count(*) from inputs", new String[0]));
        Log.d("DBAPP", "Total output rows " + DatabaseUtils.longForQuery(database, "select count(*) from outputs", new String[0]));
        Cursor cursor = database.query("inputs", new String[]{"request_id", "input"}, null, null, null, null, null, "100");
        while (cursor.moveToNext())
            Log.d("DBAPP", String.format("Input %d %s", cursor.getInt(0), cursor.getString(1)));
        cursor.close();

        cursor = database.query("outputs", new String[]{"request_id", "input"}, null, null, null, null, null, "100");
        while (cursor.moveToNext())
            Log.d("DBAPP", String.format("Output %d %s", cursor.getInt(0), cursor.getString(1)));
        cursor.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
