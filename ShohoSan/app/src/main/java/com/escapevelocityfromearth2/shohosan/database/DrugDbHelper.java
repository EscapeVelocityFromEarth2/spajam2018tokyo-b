package com.escapevelocityfromearth2.shohosan.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DrugDbHelper extends SQLiteOpenHelper {

        private static final String TAG = "DBHelper";

        final static private int DB_VERSION = 1;
        public static final String DB_FILE_NAME = "shohosan.db";

        public final String DB_COLUMN_ID = "_id";                   //databaseId
        public final String DB_COLUMN_NAME = "name";                //名前
        public final String DB_COLUMN_TIMING = "timing";            //服用タイミング
        public final String DB_COLUMN_COUNT = "count";              //残数
        public final String DB_COLUMN_ONCE_COUNT = "once_count";    //一回の摂取数
        public final String DB_COLUMN_DATE = "date";                //調剤日（long）

        public final String DB_TABLE = "data_table";

        public DrugDbHelper(Context context) {
            super(context, DB_FILE_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table "
                    + DB_TABLE + "("
                    + DB_COLUMN_ID + " integer primary key autoincrement ,"
                    + DB_COLUMN_NAME + ", "
                    + DB_COLUMN_TIMING + ", "
                    + DB_COLUMN_COUNT + ", "
                    + DB_COLUMN_ONCE_COUNT + ", "
                    + DB_COLUMN_DATE + ");"
            );
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //not implemented
    }



}

