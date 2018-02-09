package com.example.android.bookstore.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Fatima on 17-Jan-18.
 */

public class BookDBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME="bookstore.db";
    private static final int DATABASE_VERSION=1;

    public BookDBHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_BOOKS_TABLE=
                "CREATE TABLE "+ BookContract.BookEntry.TABLE_NAME + " ("
                        + BookContract.BookEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + BookContract.BookEntry.COLUMN_TITLE+ " TEXT NOT NULL, "
                        + BookContract.BookEntry.COLUMN_AUTHOR+ " TEXT NOT NULL, "
                        + BookContract.BookEntry.COLUMN_GENRE+ " INTEGER NOT NULL, "
                        + BookContract.BookEntry.COLUMN_PRICE+ " INTEGER NOT NULL, "
                        + BookContract.BookEntry.COLUMN_QUANTITY+ " INTEGER NOT NULL DEFAULT 0, "
                        + BookContract.BookEntry.COLUMN_IMAGE+ " BLOB, "
                        + BookContract.BookEntry.COLUMN_SUPPLIER_NAME+ " TEXT NOT NULL, "
                        + BookContract.BookEntry.COLUMN_SUPPLIER_EMAIL+ " TEXT NOT NULL, "
                        + BookContract.BookEntry.COLUMN_SUPPLIER_PHONE+ " INTEGER NOT NULL)";

        sqLiteDatabase.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //nothing needed here because this is the first version of the app
    }
}