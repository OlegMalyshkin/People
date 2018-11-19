package my.app.people.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import my.app.people.dao.PersonDao;
import my.app.people.entity.Person;

@Database(entities = {Person.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonDao personDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "people.db")
//                            .allowMainThreadQueries()
//                            .addMigrations(MIGRATION_1_2)
                            .build();
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override public void migrate(
                SupportSQLiteDatabase database) {

        }
    };

    public static void destroyInstance() {
        INSTANCE = null;
    }
}