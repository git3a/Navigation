package com.example.navigation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SearchSqliteHelper extends SQLiteOpenHelper {

    private String CREATE_TABLE = "create table table_search(_id integer primary key autoincrement,username varchar(200),password varchar(200))";

    public SearchSqliteHelper(Context context) {
        super(context, "search_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
