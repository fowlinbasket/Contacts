package com.example.contacts.presenters;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.Contact;

public class ContactPresenter {
    private MVPView view;
    private Contact contact;
    private AppDatabase database;
    private boolean didUpdate = false;

    public interface MVPView extends BaseMVPView {
        void renderContact(Contact contact);
        long getId();
        void goBackToContactsPage(Contact contact, boolean didDelete, boolean didUpdate);
        void displayDeleteConfirmation();
        void goToEditPage(Contact contact);
        void makePhoneCall(String number);
        void sendTextMessage(String number);
        void sendEmail(String address);
    }

    public ContactPresenter(MVPView view) {
        this.view = view;
        this.database = view.getContextDatabase();
        loadContactByID();
    }

    public void loadContactByID() {
        new Thread(() -> {
            // retrieve contact from database
            contact = database.getContactDao().findByID(view.getId());
            view.renderContact(contact);
        }).start();
    }

    public void deleteContact(){
        new Thread(() -> {
            database.getContactDao().delete(contact);
            view.goBackToContactsPage(contact, true, false);
        }).start();
    }

    public void handleEditPress() {
        view.goToEditPage(contact);
    }

    public void handleContactUpdated(Contact contact) {
        didUpdate = true;
        this.contact = contact;
        view.renderContact(contact);
    }

    public void handleDeletePressed() {
        view.displayDeleteConfirmation();
    }

    public void handleBackPressed() {
        view.goBackToContactsPage(contact, false, didUpdate);
    }

    public void handleCallPressed(String number) {
        view.makePhoneCall(number);
    }

    public void handleTextMessagePressed(String number) {
        view.sendTextMessage(number);
    }

    public void handleEmailPressed(String address) {
        view.sendEmail(address);
    }
}
