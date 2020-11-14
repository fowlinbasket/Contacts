package com.example.contacts.presenters;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.Contact;

public class ContactPresenter {
    private MVPView view;
    private AppDatabase database;
    public interface MVPView extends BaseMVPView {
        void renderContact(Contact contact);
        long getId();
    }

    public ContactPresenter(MVPView view) {
        this.view = view;
        this.database = view.getContextDatabase();
        loadContactByID();
    }

    public void loadContactByID() {
        new Thread(() -> {
            // retrieve contact from database
            Contact contact = database.getContactDao().findByID(view.getId());
            view.renderContact(contact);
        }).start();
    }
}
