package my.app.people.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

import my.app.people.entity.Person;

@Dao
public interface PersonDao {

    @Query("SELECT * FROM people")
    List<Person> getAll();

    @Query("SELECT * FROM people")
    Cursor getCursorAll();

    @Query("SELECT * FROM people WHERE _id = :id")
    Person getById(Long id);

    @Insert
    void insert(Person person);

    @Query("UPDATE people SET name = :name, surname = :surname, phoneNumber = :phoneNumber, birthday = :birthday, note = :note WHERE _id = :id")
    void customUpdate(String name, String surname, String phoneNumber , String birthday, String note, Long id);

    @Update
    void update(Person person);

    @Delete
    void delete(Person person);

}
