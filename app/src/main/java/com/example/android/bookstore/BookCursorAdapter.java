package com.example.android.bookstore;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookstore.data.BookContract;

/**
 * Created by Fatima on 18-Jan-18.
 */

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.book_list, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView book_title = view.findViewById(R.id.book_title);
        TextView book_author = view.findViewById(R.id.book_author);
        TextView book_quantity = view.findViewById(R.id.book_quantity);
        ImageView book_buy = view.findViewById(R.id.buy);


        int titleIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_TITLE);
        int authorIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_AUTHOR);
        int quantityIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_QUANTITY);
        int idIndex = cursor.getInt(cursor.getColumnIndex(BookContract.BookEntry._ID));

        final String bookTitle = cursor.getString(titleIndex);
        final String bookAuthor = cursor.getString(authorIndex);
        final int bookQuantityInt = cursor.getInt(quantityIndex);

        String bookQuantity = "Quantity: " + String.valueOf(bookQuantityInt);

        final Uri currentBook = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, idIndex);

        book_title.setText(bookTitle);
        book_author.setText(bookAuthor);
        book_quantity.setText(bookQuantity);

     book_buy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ContentResolver resolver = view.getContext().getContentResolver();
                ContentValues values = new ContentValues();
                if (bookQuantityInt > 0) {
                    int x = bookQuantityInt;
                    values.put(BookContract.BookEntry.COLUMN_QUANTITY, --x);
                    resolver.update(currentBook, values, null, null);
                    context.getContentResolver().notifyChange(currentBook, null);
                } else {
                    Toast.makeText(context, "Book is sold out!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
