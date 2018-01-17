package com.example.android.bookstore.data;

import android.provider.BaseColumns;

/**
 * Created by Fatima on 17-Jan-18.
 */

public final class BookContract {
    public static abstract class BookEntry implements BaseColumns{
        public static final String TABLE_NAME= "books";

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

    }
}
