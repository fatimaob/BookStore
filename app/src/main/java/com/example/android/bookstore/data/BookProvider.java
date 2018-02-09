package com.example.android.bookstore.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.security.Provider;

/**
 * Created by Fatima on 18-Jan-18.
 */

public class BookProvider extends ContentProvider{
    public static final String LOG_TAG = BookProvider.class.getSimpleName();
    private static final int BOOK = 100;
    private static final int BOOK_ID = 101;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKSTORE, BOOK);

        matcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKSTORE + "/#", BOOK_ID);
    }

    private BookDBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new BookDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;

        int match = matcher.match(uri);
        switch (match) {
            case BOOK:
                cursor = database.query(BookContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(BookContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = matcher.match(uri);
        switch (match) {
            case BOOK:
                return insertBook(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertBook(Uri uri, ContentValues values) {
        String title = values.getAsString(BookContract.BookEntry.COLUMN_TITLE);
        if (title == null) {
            throw new IllegalArgumentException("Book Title Required");
        }

        String author = values.getAsString(BookContract.BookEntry.COLUMN_AUTHOR);
        if (author == null) {
            throw new IllegalArgumentException("Book Author Required");
        }

        Integer genre = values.getAsInteger(BookContract.BookEntry.COLUMN_GENRE);
        if (genre == null || !BookContract.BookEntry.isValidGenre(genre)) {
            throw new IllegalArgumentException("Book Genre Required");
        }

        Integer price = values.getAsInteger(BookContract.BookEntry.COLUMN_PRICE);
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Book Requires A Valid Price");
        }

        Integer quantity = values.getAsInteger(BookContract.BookEntry.COLUMN_QUANTITY);
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Book Requires A Valid Quality");
        }

        String supplierName = values.getAsString(BookContract.BookEntry.COLUMN_SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("Supplier Name Required");
        }

        String supplierEmail = values.getAsString(BookContract.BookEntry.COLUMN_SUPPLIER_EMAIL);
        if (supplierEmail == null) {
            throw new IllegalArgumentException("Supplier Email Required");
        }

        Integer supplierPhone = values.getAsInteger(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE);
        if (supplierPhone == null || supplierPhone<0) {
            throw new IllegalArgumentException("Supplier Requires A Valid Phone");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long newRowId = db.insert(BookContract.BookEntry.TABLE_NAME, "", values);

        if (newRowId == -1) {
            Log.e(LOG_TAG, "FAILED TO INSERT NEW ROW " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, newRowId);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = matcher.match(uri);
        switch (match) {
            case BOOK:
                return updateBook(uri, contentValues, selection, selectionArgs);
            case BOOK_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateBook(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("UPDATE FAILED: " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(BookContract.BookEntry.COLUMN_TITLE)) {
            String title = values.getAsString(BookContract.BookEntry.COLUMN_TITLE);
            if (title == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        if (values.containsKey(BookContract.BookEntry.COLUMN_AUTHOR)) {
            String author = values.getAsString(BookContract.BookEntry.COLUMN_AUTHOR);
            if (author == null) {
                throw new IllegalArgumentException("Book Author Required");
            }
        }

        if (values.containsKey(BookContract.BookEntry.COLUMN_GENRE)) {
            Integer genre = values.getAsInteger(BookContract.BookEntry.COLUMN_GENRE);
            if (genre == null || !BookContract.BookEntry.isValidGenre(genre)) {
                throw new IllegalArgumentException("Book Genre Required");
            }
        }

        if (values.containsKey(BookContract.BookEntry.COLUMN_PRICE)) {
            Float price = values.getAsFloat(BookContract.BookEntry.COLUMN_PRICE);
            if (price == null || price < 0) {
                throw new IllegalArgumentException("Book Requires A Valid Price");
            }
        }

        if (values.containsKey(BookContract.BookEntry.COLUMN_QUANTITY)) {
            Integer quantity = values.getAsInteger(BookContract.BookEntry.COLUMN_QUANTITY);
            if (quantity == null || quantity < 0) {
                throw new IllegalArgumentException("Book Requires A Valid Quality");
            }
        }

        if (values.containsKey(BookContract.BookEntry.COLUMN_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(BookContract.BookEntry.COLUMN_SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("Supplier Name Required");
            }
        }

        if (values.containsKey(BookContract.BookEntry.COLUMN_SUPPLIER_EMAIL)) {
            String supplierEmail = values.getAsString(BookContract.BookEntry.COLUMN_SUPPLIER_EMAIL);
            if (supplierEmail == null) {
                throw new IllegalArgumentException("Supplier Email Required");
            }
        }

        if (values.containsKey(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE)) {
            Integer supplierPhone = values.getAsInteger(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE);
            if (supplierPhone == null || supplierPhone<0) {
                throw new IllegalArgumentException("Supplier Requires A Valid Phone");
            }
        }

        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int rowsUpdated = database.update(BookContract.BookEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = matcher.match(uri);
        switch (match) {
            case BOOK:
                rowsDeleted = database.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("DELETION FAILED: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = matcher.match(uri);
        switch (match) {
            case BOOK:
                return BookContract.BookEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BookContract.BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("URI UNKNOWN: " + uri + " MATCH: " + match);
        }
    }

}
