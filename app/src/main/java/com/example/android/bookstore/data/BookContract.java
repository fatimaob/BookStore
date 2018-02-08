package com.example.android.bookstore.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Fatima on 17-Jan-18.
 */

public final class BookContract {
    private BookContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.android.bookstore";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKSTORE = "bookstore";

    public static abstract class BookEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKSTORE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKSTORE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKSTORE;


        public static final String TABLE_NAME= "bookstore";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE= "title";
        public static final String COLUMN_AUTHOR= "author";
        public static final String COLUMN_GENRE= "genre";
        public static final String COLUMN_PRICE= "price";
        public static final String COLUMN_QUANTITY= "quantity";
        public static final String COLUMN_IMAGE= "image";
        public static final String COLUMN_SUPPLIER_NAME= "supplier";
        public static final String COLUMN_SUPPLIER_EMAIL= "email";
        public static final String COLUMN_SUPPLIER_PHONE= "phone";

        public static final int GENRE_NONFICTION=0;
        public static final int GENRE_FICTION=1;

        public static boolean isValidGenre(int gender) {
            if (gender == GENRE_NONFICTION || gender == GENRE_FICTION) {
                return true;
            }
            return false;
        }
    }
}
