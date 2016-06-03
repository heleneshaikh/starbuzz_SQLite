package com.hfad.dbtest;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends Activity {
    protected static final String ID = "id";
    SQLiteDatabase db;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        Intent intent = getIntent();
        int id = (int) intent.getExtras().get(ID);

        try {
            SQLiteOpenHelper dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();
            cursor = db.query("drink",
                    new String[]{"NAME", "DESCRIPTION", "IMAGE", "FAVOURITE"},
                    "_id=?",
                    new String[]{Integer.toString(id)},
                    null, null, null
            );
            if (cursor.moveToFirst()) {
                TextView name = (TextView) findViewById(R.id.tv_name);
                name.setText(cursor.getString(0));

                TextView description = (TextView) findViewById(R.id.tv_description);
                description.setText(cursor.getString(1));

                ImageView image = (ImageView) findViewById(R.id.iv_photo);
                image.setImageResource(cursor.getInt(2));
                image.setContentDescription(cursor.getString(0));

                CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox_fav);
                boolean isChecked = (cursor.getInt(3) == 1);
                checkBox.setChecked(isChecked);
            }
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    public void onFavouriteClicked(View view) {
        Intent intent = getIntent();
        int id = (int) intent.getExtras().get(ID);

        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox_fav);
        ContentValues contentValues = new ContentValues();
        contentValues.put("FAVOURITE", checkBox.isChecked());

        SQLiteOpenHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        db.update("drink", contentValues, "_id = ?", new String[]{Integer.toString(id)});
        db.close();
    }
}
