package com.example.contacts.presenters;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.Contact;

public class NewContactPresenter {
    private MVPView view;
    private AppDatabase database;
    public interface MVPView extends BaseMVPView {
        void goBackToContactsPage(Contact contact);
    }

    public NewContactPresenter(MVPView view) {
        this.view = view;
        this.database = this.view.getContextDatabase();
    }

    public void createContact(String name, String phoneNumber, String email) {
        // TODO: Check to make sure contents is not empty
        new Thread(() -> {
            // create new contact
            Contact contact = new Contact();
            contact.name = name;
            contact.phoneNumber = phoneNumber;
            contact.email = email;
            // insert into database and retrieve id
            contact.id = database.getContactDao().insert(contact);
            // return to contacts page
            view.goBackToContactsPage(contact);
        }).start();
    }
}
