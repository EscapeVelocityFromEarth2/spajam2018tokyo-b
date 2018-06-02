package com.escapevelocityfromearth2.shohosan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.escapevelocityfromearth2.shohosan.DrugData;

import java.util.ArrayList;

public class DbManager {

    private static final String TAG = DbManager.class.getSimpleName();

    public static ArrayList<DrugData> loadData(Context context){

        DrugDbHelper helper = new DrugDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        ArrayList<DrugData> tempList = new ArrayList<DrugData>();

        String[] column = new String[] {
                helper.DB_COLUMN_ID,
                helper.DB_COLUMN_NAME,
                helper.DB_COLUMN_TIMING,
                helper.DB_COLUMN_COUNT,
                helper.DB_COLUMN_ONCE_COUNT,
                helper.DB_COLUMN_DATE
        };

        Cursor cursor = db.query(helper.DB_TABLE, column, null, null, null, null, null);

        cursor.moveToFirst();

        for (int i=0 ; i<cursor.getCount() ; i++) {

            DrugData data = new DrugData(
                    null,
                    null,
                    cursor.getInt(cursor.getColumnIndex(helper.DB_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(helper.DB_COLUMN_NAME)),
                    DrugData.MEDICAL_TIME.values()[(cursor.getInt(cursor.getColumnIndex(helper.DB_COLUMN_TIMING)))],
                    cursor.getInt(cursor.getColumnIndex(helper.DB_COLUMN_COUNT)),
                    cursor.getInt(cursor.getColumnIndex(helper.DB_COLUMN_ONCE_COUNT)),
                    cursor.getLong(cursor.getColumnIndex(helper.DB_COLUMN_DATE))
            );

//            android.util.Log.d(TAG + "_load", "load db item");
            android.util.Log.d(TAG + "_load", "load data\n"
                    + "id " + data.databaseId + "\n"
                    + "name " + data.name + "\n"
                    + "timing " + data.getMedicalTimeText() + "\n"
                    + "count " + data.count + "\n"
                    + "once count " + data.onceCount + "\n"
                    + "date " + data.date
            );

            tempList.add(data);
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return tempList;

    }

    public static void register(Context context, DrugData data){

        android.util.Log.d(TAG + "_register", "register db item");
        android.util.Log.d(TAG + "_load", "load data\n"
                + "id " + data.databaseId + "\n"
                + "name " + data.name + "\n"
                + "timing " + data.getMedicalTimeText() + "\n"
                + "count " + data.count + "\n"
                + "once count " + data.onceCount + "\n"
                + "date " + data.date
        );

        DrugDbHelper helper = new DrugDbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.clear();
        values.put(helper.DB_COLUMN_NAME, data.name);
        values.put(helper.DB_COLUMN_TIMING, data.timing.ordinal());
        values.put(helper.DB_COLUMN_COUNT, data.count);
        values.put(helper.DB_COLUMN_ONCE_COUNT, data.onceCount);
        values.put(helper.DB_COLUMN_DATE, data.date);
        db.insert(helper.DB_TABLE, null, values);

        db.close();
    }
}
