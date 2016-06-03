package com.hfad.dbtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "starbuzz";
    private final static int DB_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 0) {
            db.execSQL("CREATE TABLE drink (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NAME TEXT, " +
                    "DESCRIPTION TEXT, " +
                    "IMAGE INTEGER);");
            insertDrinks(db, "Cappuccino", "Whole, muddy pudding is best covered with canned olive oil.", R.drawable.cappuccino);
            insertDrinks(db, "Espresso", "Sichuan-style mackerel can be made shredded by marinating with peppermint tea.", R.drawable.espresso);
            insertDrinks(db, "Latte", "Meatballs soup is just not the same without lime and smooth al dente ground beef .", R.drawable.latte);
        }
        if (oldVersion <= 2) {
            db.execSQL("ALTER TABLE DRINK ADD COLUMN FAVOURITE NUMERIC");
        }
    }

    private static void insertDrinks(SQLiteDatabase db, String name, String description, int imageId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("DESCRIPTION", description);
        contentValues.put("IMAGE", imageId);
        db.insert("drink", null, contentValues);
    }
}
