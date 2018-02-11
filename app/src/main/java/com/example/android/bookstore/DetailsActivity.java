package com.example.android.bookstore;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookstore.data.BookContract;

/**
 * Created by Fatima on 10-Feb-18.
 */

public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

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

    private Uri currentBook;

    private ImageView bookImage;
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookGenre;
    private TextView bookPrice;
    private TextView bookQuantity;
    private TextView supplierName;
    private TextView supplierEmail;
    private TextView supplierPhone;

    private Button increment;
    private Button decrement;

    private Button orderButton;
    private Button editButton;
    private Button deleteButton;

    private int genre = BookContract.BookEntry.GENRE_NONFICTION;

    private boolean bookChanged = false;

    private String supplyEmail;
    private String bookOrder;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            bookChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        currentBook = intent.getData();

        bookImage = findViewById(R.id.book_image);
        bookTitle = findViewById(R.id.title);
        bookAuthor = findViewById(R.id.author);
        bookGenre = findViewById(R.id.genre);
        bookPrice = findViewById(R.id.price);
        bookQuantity = findViewById(R.id.quantity);
        supplierName = findViewById(R.id.supplier_name);
        supplierEmail = findViewById(R.id.supplier_email);
        supplierPhone = findViewById(R.id.supplier_phone);
        editButton=   findViewById(R.id.edit_button);
        orderButton=findViewById(R.id.order_button);
        deleteButton = findViewById(R.id.delete_button);
        increment = findViewById(R.id.increment_button);
        decrement = findViewById(R.id.decrement_button);

        increment.setOnTouchListener(touchListener);
        decrement.setOnTouchListener(touchListener);

        getLoaderManager().initLoader(LOADER, null, this);

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String quantityString = bookQuantity.getText().toString().trim();

                if (!TextUtils.isEmpty(quantityString)) {
                    int quantity = Integer.parseInt(quantityString) + 1;
                    bookQuantity.setText(Integer.toString(quantity));
                    ContentValues values = new ContentValues();
                    values.put(BookContract.BookEntry.COLUMN_QUANTITY, quantity);
                    getContentResolver().update(currentBook, values, null, null);
                }
            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String quantityString = bookQuantity.getText().toString().trim();

                if (!TextUtils.isEmpty(quantityString)) {
                    int quantity = Integer.parseInt(quantityString);

                    if (quantity > 0) {
                        quantity--;
                        bookQuantity.setText(Integer.toString(quantity));
                        ContentValues values = new ContentValues();
                        values.put(BookContract.BookEntry.COLUMN_QUANTITY, quantity);
                        getContentResolver().update(currentBook, values, null, null);
                    }


                }
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, EditorActivity.class);
                intent.setData(currentBook);
                startActivityForResult(intent, 9);
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderBook();
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, currentBook, COLS, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {

            int titleColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_TITLE);
            int authorColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_AUTHOR);
            int genreColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_GENRE);
            int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_QUANTITY);
            int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SUPPLIER_NAME);
            int emailColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SUPPLIER_EMAIL);
            int phoneColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE);


            String title = cursor.getString(titleColumnIndex);
            String author = cursor.getString(authorColumnIndex);
            int genre = cursor.getInt(genreColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String name = cursor.getString(nameColumnIndex);
            String email = cursor.getString(emailColumnIndex);
            String phone = cursor.getString(phoneColumnIndex);

            bookOrder=title;
            supplyEmail=email;

            bookTitle.setText("Title: "+title);
            bookAuthor.setText("Author: "+author);
            bookPrice.setText("Price: $"+String.valueOf(price));
            bookQuantity.setText(String.valueOf(quantity));
            supplierName.setText("Supplier Name: "+name);
            supplierEmail.setText("Supplier Email: "+email);
            supplierPhone.setText("SupplierPhone: "+phone);

            switch (genre) {
                case BookContract.BookEntry.GENRE_FICTION:
                    bookGenre.setText("Genre: Fiction");
                    break;
                default:
                    bookGenre.setText("Genre: Non Fiction");
                    break;
            }

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookTitle.setText("Title: ");
        bookAuthor.setText("Author: ");
        bookGenre.setText("Genre: ");
        bookPrice.setText("Price: $");
        bookQuantity.setText("");
        supplierName.setText("Supplier Name: ");
        supplierEmail.setText("Supplier Email: ");
        supplierPhone.setText("Supplier Phone: ");
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete book?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteBook();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void orderBook() {
        String[] TO = {supplyEmail};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "URGENT: Order Request" );
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please send a shipment of "+bookOrder+"  ASAP!");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    private void deleteBook(){
        if (currentBook != null) {

            int rowsDeleted = getContentResolver().delete(currentBook, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this,"Error Deleting Book",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Book Deleted Successfully",
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();

    }

}
