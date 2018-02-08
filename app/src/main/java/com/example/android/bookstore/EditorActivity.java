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
 * Created by Fatima on 18-Jan-18.
 */

public class EditorActivity extends AppCompatActivity implements
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
    private EditText bookTitle;
    private EditText bookAuthor;
    private Spinner bookGenreSpinner;
    private EditText bookPrice;
    private EditText bookQuantity;
    private EditText supplierName;
    private EditText supplierEmail;
    private EditText supplierPhone;

    private Button orderButton;
    private Button saveButton;
    private Button deleteButton;

    private int bookGenre = BookContract.BookEntry.GENRE_NONFICTION;

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
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        currentBook = intent.getData();

        bookImage = findViewById(R.id.book_image);
        bookTitle = findViewById(R.id.book_title);
        bookAuthor = findViewById(R.id.book_author);
        bookGenreSpinner = (Spinner) findViewById(R.id.spinner_genre);
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

        orderButton = findViewById(R.id.order_button);
        saveButton = findViewById(R.id.save_button);
        deleteButton = findViewById(R.id.delete_button);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderBook();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBook();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });

        if (currentBook == null) {
            setTitle("Add Book");
            saveButton.setVisibility(View.VISIBLE);
            orderButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);

        } else {
            setTitle("Edit Book");
            saveButton.setVisibility(View.VISIBLE);
            orderButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);

            getLoaderManager().initLoader(LOADER, null, this);
        }


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

            // Because AdapterView is an abstract class, onNothingSelected must be defined
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
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
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

            supplyEmail=email;
            bookOrder=title;

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

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    private void saveBook(){
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

        if (currentBook == null) {

            Uri insertedRow = getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, values);

            if (insertedRow == null) {
                Toast.makeText(this, "Error Adding Book", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Booked Added Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        } else {
            // We are Updating
            int rowUpdated = getContentResolver().update(currentBook, values, null, null);

            if (rowUpdated == 0) {
                Toast.makeText(this, "Error Updating Book", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Book Updated Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            }

        }


    }

}
