package com.example.navigation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecordsSqliteHelper extends SQLiteOpenHelper {

    private String CREATE_RECORDS_TABLE = "create table table_records(_id integer primary key autoincrement,username varchar(200),password varchar(200))";

    public RecordsSqliteHelper(Context context) {
        super(context, "records_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_RECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

