package my.app.people.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import my.app.people.R;
import my.app.people.dao.PersonDao;
import my.app.people.database.AppDatabase;
import my.app.people.entity.Person;

public class EditActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText phoneEditText;
    private EditText birthdayEditText;
    private EditText noteEditText;

    private Long rowID;

    private SaveOrUpdatePerson saveOrUpdatePerson;

    private AppDatabase db = AppDatabase.getAppDatabase(this);
    private PersonDao dao = db.personDao();

    private boolean isUpdate = false;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_item_save:
                    saveOrUpdatePerson.execute();
                    EditActivity.super.onBackPressed();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener((v) -> {
            onBackPressed();
        });

        BottomNavigationView navigation_edit = (BottomNavigationView) findViewById(R.id.navigation_edit);
        navigation_edit.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        surnameEditText = (EditText) findViewById(R.id.surnameEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        birthdayEditText = (EditText) findViewById(R.id.birthdayEditText);
        noteEditText = (EditText) findViewById(R.id.noteEditText);

        saveOrUpdatePerson = new SaveOrUpdatePerson();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            toolbar.setTitle("Редагувати");
            rowID = extras.getLong("row_id");
            nameEditText.setText(extras.getString("name"));
            surnameEditText.setText(extras.getString("surname"));
            phoneEditText.setText(extras.getString("phone"));
            birthdayEditText.setText(extras.getString("date"));
            noteEditText.setText(extras.getString("note"));
            isUpdate = true;
        } else {
            toolbar.setTitle("Додати");
        }
    }

    private Person savePerson(){
        return new Person(nameEditText.getText().toString(),
                surnameEditText.getText().toString(),
                birthdayEditText.getText().toString(),
                phoneEditText.getText().toString(),
                noteEditText.getText().toString());
    }

    private class SaveOrUpdatePerson extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if(!nameEditText.getText().toString().equals("")) {
                if (isUpdate) {
                    dao.customUpdate(savePerson().getName(), savePerson().getSurname(),
                            savePerson().getPhoneNumber(), savePerson().getBirthday(),
                            savePerson().getNote(), rowID);
                } else {
                    dao.insert(savePerson());
                }
            }
            return null;
        }
    }

}
