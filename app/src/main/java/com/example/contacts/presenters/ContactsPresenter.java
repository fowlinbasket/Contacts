package com.example.contacts.presenters;


import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.Contact;

import java.util.ArrayList;

public class ContactsPresenter {
    private MVPView view;
    private ArrayList<Contact> contacts = new ArrayList<>();
    private AppDatabase database;
    public interface MVPView extends BaseMVPView {
        void renderContact(Contact contact);
        void goToNewContactPage();
    }

    public ContactsPresenter(MVPView view) {
        this.view = view;
        this.database = view.getContextDatabase();
        loadContacts();
    }

    public void loadContacts() {
        new Thread(() -> {
            // retrieve all contacts from database
            contacts = (ArrayList<Contact>) database.getContactDao().getAllContacts();
            contacts.forEach(contact -> {
                view.renderContact(contact);
            });
        }).start();
    }

    public void onNewContactCreated(Contact contact) {
        // add contact to local ArrayList of contacts
        contacts.add(contact);
        view.renderContact(contact);
    }
}
