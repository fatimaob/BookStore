package com.example.android.bookstore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.bookstore.data.BookContract;
import com.example.android.bookstore.data.BookDBHelper;

public class MainActivity extends AppCompatActivity {
    private BookDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new BookDBHelper(this);
    }

    private Cursor queryStock(){
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] col={
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_TITLE,
                BookContract.BookEntry.COLUMN_AUTHOR,
                BookContract.BookEntry.COLUMN_QUANTITY};
        Cursor cursor=db.query(BookContract.BookEntry.TABLE_NAME, col, null, null, null, null,null);
        return cursor;
    }
    private void insertBooks(){

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues book1 = new ContentValues();
        book1.put(BookContract.BookEntry.COLUMN_TITLE, "1984");
        book1.put(BookContract.BookEntry.COLUMN_AUTHOR, "George Orwell");
        book1.put(BookContract.BookEntry.COLUMN_GENRE, BookContract.BookEntry.GENRE_FICTION);
        book1.put(BookContract.BookEntry.COLUMN_PRICE, 2000);
        book1.put(BookContract.BookEntry.COLUMN_QUANTITY,34);
        book1.put(BookContract.BookEntry.COLUMN_SUPPLIER_NAME, "CJ Books");
        book1.put(BookContract.BookEntry.COLUMN_SUPPLIER_EMAIL,"books@cj.com");
        book1.put(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE, 9519512);

        ContentValues book2 = new ContentValues();
        book2.put(BookContract.BookEntry.COLUMN_TITLE, "The Diary of a Young Girl");
        book2.put(BookContract.BookEntry.COLUMN_AUTHOR, "Anne Frank");
        book2.put(BookContract.BookEntry.COLUMN_GENRE, BookContract.BookEntry.GENRE_NONFICTION);
        book2.put(BookContract.BookEntry.COLUMN_PRICE, 1300);
        book2.put(BookContract.BookEntry.COLUMN_QUANTITY,21);
        book2.put(BookContract.BookEntry.COLUMN_SUPPLIER_NAME, "Book Supplies");
        book2.put(BookContract.BookEntry.COLUMN_SUPPLIER_EMAIL,"info@bs.org");
        book2.put(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE, 7123429);

        long rowId_1=db.insert(BookContract.BookEntry.TABLE_NAME, null,book1);
        long rowId_2=db.insert(BookContract.BookEntry.TABLE_NAME, null,book2);

    }

}
