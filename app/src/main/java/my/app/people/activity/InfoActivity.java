package my.app.people.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import my.app.people.R;
import my.app.people.dao.PersonDao;
import my.app.people.database.AppDatabase;
import my.app.people.entity.Person;

public class InfoActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView surnameTextView;
    private TextView phoneTextView;
    private TextView birthdayTextView;
    private TextView noteTextView;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Long rowID;
    private Person person;

    private LoadPerson loadPerson;
    private AppDatabase db = AppDatabase.getAppDatabase(this);
    private PersonDao dao = db.personDao();

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_item_delete:
                    deleteContact();
                    return true;
                case R.id.navigation_item_edit:
                    Intent editContact = new Intent(InfoActivity.this, EditActivity.class);
                    editContact.putExtra("row_id", rowID);
                    editContact.putExtra("name", nameTextView.getText());
                    editContact.putExtra("surname", surnameTextView.getText());
                    editContact.putExtra("phone", phoneTextView.getText());
                    editContact.putExtra("date", birthdayTextView.getText());
                    editContact.putExtra("note", noteTextView.getText());
                    startActivity(editContact);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_info);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation_save = (BottomNavigationView) findViewById(R.id.navigation_info);
        navigation_save.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        collapsingToolbarLayout = findViewById(R.id.toolbar_layout_info);;

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        surnameTextView = (TextView) findViewById(R.id.surnameTextView);
        phoneTextView = (TextView) findViewById(R.id.phoneTextView);
        birthdayTextView = (TextView) findViewById(R.id.birthdayTextView);
        noteTextView = (TextView) findViewById(R.id.noteTextView);

        Bundle extras = getIntent().getExtras();
        rowID = extras.getLong("row_id");

        person = new Person();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabcall);
        fab.setOnClickListener((v) -> {
            String phoneNumber = phoneTextView.getText().toString();
            if (phoneNumber != null) {
                String number = String.format("tel:%s", phoneNumber);
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(number)));
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
                builder.setTitle(R.string.errorTitlePhone);
                builder.setMessage(R.string.errorMessagePhone);
                builder.setPositiveButton(R.string.errorButtonPhone, null);
                builder.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPerson = new LoadPerson();
        loadPerson.execute();
    }

    @Override
    protected void onStop() {
        loadPerson = null;
        super.onStop();
    }

    private class LoadPerson extends AsyncTask<Void, Void, Person> {

        @Override
        protected Person doInBackground(Void... voids) {
            person = dao.getById(rowID);
            return person;
        }

        @Override
        protected void onPostExecute(Person person) {
            nameTextView.setText(person.getName());
            surnameTextView.setText(person.getSurname());
            phoneTextView.setText(person.getPhoneNumber());
            birthdayTextView.setText(person.getBirthday());
            noteTextView.setText(person.getNote());
            collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
            collapsingToolbarLayout.setTitle(person.getName());
        }
    }

    private void deleteContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
        builder.setTitle(R.string.confirmTitle);
        builder.setMessage(R.string.confirmMessage);
        builder.setPositiveButton(R.string.button_delete,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        new DeletePerson().execute();
                    }
                }
        );
        builder.setNegativeButton(R.string.button_cancel, null);
        builder.show();
    }

    private class DeletePerson extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            dao.delete(person);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            InfoActivity.super.onBackPressed();
        }
    }

}
