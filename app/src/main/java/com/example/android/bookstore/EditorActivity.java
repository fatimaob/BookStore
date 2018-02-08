package com.example.android.bookstore;

import android.app.LoaderManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.bookstore.data.BookContract;

/**
 * Created by Fatima on 18-Jan-18.
 */

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String TAG = EditorActivity.class.getSimpleName();
    public static final int PHOTO_REQUEST = 20;
    public static final int STORAGE_REQUEST_PERMISSION = 21;

    private static final int LOADER = 0;

    public final String[] COLS = {
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

    private Uri mCurrentProductUri; //current product _ID in edit mode null in insert mode

    private ImageView bookImage;
    private EditText bookTitle;
    private EditText bookAuthor;
    private Spinner bookGenre;
    private EditText bookPrice;
    private EditText bookQuantity;
    private EditText supplierName;
    private EditText supplierEmail;
    private EditText supplierPhone;

    private Button deleteBtn;
    private Button orderBtn;
    private Button updateBtn;
    private TextView lUpdateSave;
    private TextView lOrder;
    private TextView lDelete;

    private String mCurrentPhotoUri = "no ";
    private String mSudoEmail;
    private String mSudoProduct;
    private int mSudoQuantity = 50;

    //Validation Variables
    private boolean mProductHasChanged = false;

}
