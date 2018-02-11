package com.example.android.bookstore;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bookstore.data.BookContract;
import com.example.android.bookstore.data.BookDBHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER = 0;
    public final String[] COL={
            BookContract.BookEntry._ID,
            BookContract.BookEntry.COLUMN_TITLE,
            BookContract.BookEntry.COLUMN_AUTHOR,
            BookContract.BookEntry.COLUMN_GENRE,
            BookContract.BookEntry.COLUMN_PRICE,
            BookContract.BookEntry.COLUMN_QUANTITY,
            BookContract.BookEntry.COLUMN_IMAGE,
            BookContract.BookEntry.COLUMN_SUPPLIER_NAME,
            BookContract.BookEntry.COLUMN_SUPPLIER_EMAIL,
            BookContract.BookEntry.COLUMN_SUPPLIER_PHONE
    };

    private BookCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        ListView bookListView = findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        cursorAdapter = new BookCursorAdapter(this, null);
        bookListView.setAdapter(cursorAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);

                Uri currentBook = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, id);
                intent.setData(currentBook);
                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(LOADER, null, this);

    }

    private void insertBook(){
        ContentValues book1 = new ContentValues();
        book1.put(BookContract.BookEntry.COLUMN_TITLE, "1984");
        book1.put(BookContract.BookEntry.COLUMN_AUTHOR, "George Orwell");
        book1.put(BookContract.BookEntry.COLUMN_GENRE, BookContract.BookEntry.GENRE_FICTION);
        book1.put(BookContract.BookEntry.COLUMN_PRICE, 2000);
        book1.put(BookContract.BookEntry.COLUMN_QUANTITY,34);
        book1.put(BookContract.BookEntry.COLUMN_SUPPLIER_NAME, "CJ Books");
        book1.put(BookContract.BookEntry.COLUMN_SUPPLIER_EMAIL,"books@cj.com");
        book1.put(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE, 9519512);

        Uri bookUri= getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, book1);
    }

    private void deleteAllBooks() {
        int rowsDeleted = getContentResolver().delete(BookContract.BookEntry.CONTENT_URI, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem book) {
        switch (book.getItemId()) {
            case R.id.insert_book:
                insertBook();
                return true;

            case R.id.delete_all_books:
                deleteAllBooks();
                return true;
        }
        return super.onOptionsItemSelected(book);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_TITLE,
                BookContract.BookEntry.COLUMN_AUTHOR,
                BookContract.BookEntry.COLUMN_QUANTITY,
                BookContract.BookEntry.COLUMN_IMAGE};

        return new CursorLoader(this, BookContract.BookEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

}
