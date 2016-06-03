package com.hfad.dbtest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.List;

public class TopLevelActivity extends Activity {
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(TopLevelActivity.this, DrinkCategoryActivity.class);
                    startActivity(intent);
                }
            }
        };
        ListView listView = (ListView) findViewById(R.id.lv_top);
        listView.setOnItemClickListener(listener);

        ListView listFavourites = (ListView) findViewById(R.id.lv_favourites);
        try {

            SQLiteOpenHelper dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();

            cursor = db.query("drink",
                    new String[]{"_id", "NAME"},
                    "FAVOURITE = 1", null, null, null, null);

            CursorAdapter adapter = new SimpleCursorAdapter(
                    TopLevelActivity.this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0);
            listFavourites.setAdapter(adapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        try {
            SQLiteOpenHelper dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();

            Cursor newCursor = db.query("drink",
                    new String[]{"_id", "NAME"},
                    "FAVOURITE = 1", null, null, null, null);

            ListView listFavourites = (ListView) findViewById(R.id.lv_favourites);
            CursorAdapter adapter = (CursorAdapter) listFavourites.getAdapter();
            adapter.changeCursor(newCursor);
            cursor = newCursor;

        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
