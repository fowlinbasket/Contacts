package com.example.contacts.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contacts.models.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact")
    List<Contact> getAllContacts();

    @Query("SELECT * FROM contact WHERE id = :id LIMIT 1")
    Contact findByID(long id);

    @Update
    void update(Contact contact);

    @Insert
    long insert(Contact contact);
}
