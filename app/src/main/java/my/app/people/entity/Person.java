package my.app.people.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "people")
public class Person {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private Long id;
    private String name;
    private String surname;
    private String birthday;
    private String phoneNumber;
    private String note;

    public Person(){}

    public Person(String name, String surname, String birthday, String phoneNumber, String note) {
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String data) {
        this.birthday = data;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return id + " " + name + " " + surname;
    }
}
