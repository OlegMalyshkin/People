package my.app.people.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

import my.app.people.R;
import my.app.people.dao.PersonDao;
import my.app.people.database.AppDatabase;
import my.app.people.entity.Person;

public class PeopleActivity extends AppCompatActivity {

    private final String ROW_ID = "row_id";
    private ListView listPeople;
    private List<Person> personList;
    private ArrayAdapter<Person> adapter;
    private Cursor cursor;
    private SimpleCursorAdapter cursorAdapter;
    private AppDatabase db;
    private PersonDao dao;
    private GetPeopleList getPeopleList;
    private String[] from;
    private int[] to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listPeople = (ListView) findViewById(R.id.contactList);
        db = AppDatabase.getAppDatabase(this);
        dao = db.personDao();

        from = new String[] { "name", "surname" };
        to = new int[] { R.id.tvName, R.id.tvSurname };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabadd);
        fab.setOnClickListener((v) -> {
            Intent infoPerson = new Intent(PeopleActivity.this, EditActivity.class);
            startActivity(infoPerson);
        });

        listPeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent addPerson = new Intent(PeopleActivity.this, InfoActivity.class);
//                addPerson.putExtra(ROW_ID, id + 1);
                addPerson.putExtra(ROW_ID, id);
                startActivity(addPerson);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPeopleList = new GetPeopleList();
        getPeopleList.execute();
    }

    @Override
    protected void onStop() {
        getPeopleList = null;
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_people, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_action_add) {
            Intent infoPerson = new Intent(PeopleActivity.this, EditActivity.class);
            startActivity(infoPerson);
            return true;
        }
        if (id == R.id.menu_action_about) {
            Snackbar.make(findViewById(R.id.coordinator_layout_people),
                    "version 2.0(with Room framework)\n" +
                    "author: Oleg Malyshkin", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetPeopleList extends AsyncTask<Void, Void, Void> {

        private boolean alreadyRun = false;

        public boolean isAlreadyRun() {
            return alreadyRun;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            personList = dao.getAll();
            cursor = dao.getCursorAll();
            alreadyRun = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            listPeople.setAdapter(getAdapter());
            listPeople.setAdapter(getSimpleCursorAdapter());
        }
    }

    private ArrayAdapter<Person> getAdapter(){
        return adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, personList);
    }

    private SimpleCursorAdapter getSimpleCursorAdapter() {
        return cursorAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to, 0);
    }

}
