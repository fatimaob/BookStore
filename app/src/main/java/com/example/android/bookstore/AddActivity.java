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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.bookstore.data.BookContract;

/**
 * Created by Fatima on 11-Feb-18.
 */

public class AddActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

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
    private EditText bookTitle;
    private EditText bookAuthor;
    private Spinner bookGenreSpinner;
    private EditText bookPrice;
    private EditText bookQuantity;
    private EditText supplierName;
    private EditText supplierEmail;
    private EditText supplierPhone;

    private Button increment;
    private Button decrement;

    private Button saveButton;

    private int bookGenre = BookContract.BookEntry.GENRE_NONFICTION;

    private boolean bookChanged = false;

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
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        currentBook = intent.getData();

        bookImage = findViewById(R.id.book_image);
        bookTitle = findViewById(R.id.book_title);
        bookAuthor = findViewById(R.id.book_author);
        bookGenreSpinner = findViewById(R.id.spinner_genre);
        bookPrice = findViewById(R.id.book_price);
        bookQuantity = findViewById(R.id.book_quantity);
        supplierName = findViewById(R.id.book_supplier_name);
        supplierEmail = findViewById(R.id.book_supplier_email);
        supplierPhone = findViewById(R.id.book_supplier_phone);

        bookImage.setOnTouchListener(touchListener);
        bookTitle.setOnTouchListener(touchListener);
        bookAuthor.setOnTouchListener(touchListener);
        bookGenreSpinner.setOnTouchListener(touchListener);
        bookPrice.setOnTouchListener(touchListener);
        bookQuantity.setOnTouchListener(touchListener);
        supplierName.setOnTouchListener(touchListener);
        supplierEmail.setOnTouchListener(touchListener);
        supplierPhone.setOnTouchListener(touchListener);

        increment = findViewById(R.id.increment_button);
        decrement = findViewById(R.id.decrement_button);

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String quantityString = bookQuantity.getText().toString().trim();

                if (!TextUtils.isEmpty(quantityString)) {
                    int quantity = Integer.parseInt(quantityString) + 1;
                    bookQuantity.setText(Integer.toString(quantity));

                    bookChanged = true;
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
                    }
                    bookQuantity.setText(Integer.toString(quantity));

                    bookChanged = true;
                }
            }
        });

        saveButton = findViewById(R.id.save_button);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBook();
            }
        });

        setTitle("Add Book");

        setupSpinner();

    }

    private void setupSpinner() {
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_genre_options, android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        bookGenreSpinner.setAdapter(spinnerAdapter);

        bookGenreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.genre_nonfiction))) {
                        bookGenre = BookContract.BookEntry.GENRE_NONFICTION;
                    } else if (selection.equals(getString(R.string.genre_fiction))) {
                        bookGenre = BookContract.BookEntry.GENRE_FICTION;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bookGenre = BookContract.BookEntry.GENRE_NONFICTION;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (!bookChanged) {
                    NavUtils.navigateUpFromSameTask(AddActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavUtils.navigateUpFromSameTask(AddActivity.this);
                    }
                };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if (!bookChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, currentBook, COLS, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

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

            bookTitle.setText(title);
            bookAuthor.setText(author);
            bookPrice.setText(String.valueOf(price));
            bookQuantity.setText(String.valueOf(quantity));
            supplierName.setText(name);
            supplierEmail.setText(email);
            supplierPhone.setText(phone);

            switch (genre) {
                case BookContract.BookEntry.GENRE_FICTION:
                    bookGenreSpinner.setSelection(1);
                    break;
                default:
                    bookGenreSpinner.setSelection(0);
                    break;
            }

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookTitle.setText("");
        bookAuthor.setText("");
        bookGenreSpinner.setSelection(0);
        bookPrice.setText("");
        bookQuantity.setText("");
        supplierName.setText("");
        supplierEmail.setText("");
        supplierPhone.setText("");
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard book changes and quit?");
        builder.setPositiveButton("Discard", discardButtonClickListener);
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

    private void saveBook() {
        String titleString = bookTitle.getText().toString().trim();
        String authorString = bookAuthor.getText().toString().trim();
        String priceString = bookPrice.getText().toString().toString();
        String quantityString = bookQuantity.getText().toString().trim();
        String supplierString = supplierName.getText().toString().trim();
        String emailString = supplierEmail.getText().toString().trim();
        String phoneString = supplierPhone.getText().toString().trim();

        if (TextUtils.isEmpty(titleString) || TextUtils.isEmpty(authorString)
                || TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(emailString)
                || TextUtils.isEmpty(priceString) || TextUtils.isEmpty(phoneString)
                || TextUtils.isEmpty(supplierString)) {

            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();

        values.put(BookContract.BookEntry.COLUMN_TITLE, titleString);
        values.put(BookContract.BookEntry.COLUMN_AUTHOR, authorString);
        values.put(BookContract.BookEntry.COLUMN_GENRE, bookGenre);
        values.put(BookContract.BookEntry.COLUMN_QUANTITY, quantityString);
        values.put(BookContract.BookEntry.COLUMN_PRICE, priceString);
        values.put(BookContract.BookEntry.COLUMN_SUPPLIER_NAME, supplierString);
        values.put(BookContract.BookEntry.COLUMN_SUPPLIER_EMAIL, emailString);
        values.put(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE, phoneString);


        Uri insertedRow = getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, values);

        if (insertedRow == null) {
            Toast.makeText(this, "Error Adding Book", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Booked Added Successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }


    }

}
