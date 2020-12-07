package com.example.contacts.presenters;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.Contact;

public class NewContactPresenter {
    private MVPView view;
    private AppDatabase database;

    public void handleCancelPress() {
        view.goBackToContactsPage(null);
    }

    public void handleSelectPicturePress() {
        view.goToPhotos();
    }

    public void handlePictureSelected(String uri) {
        view.displayImage(uri);
    }

    public interface MVPView extends BaseMVPView {
        void goBackToContactsPage(Contact contact);
        void printErrorMessage();

        void goToPhotos();

        void displayImage(String uri);
    }

    public NewContactPresenter(MVPView view) {
        this.view = view;
        this.database = this.view.getContextDatabase();
    }

    public void createContact(String name, String phoneNumber, String email, String pictureUri) {
        if (isNotEmpty(name, phoneNumber, email)) {
            new Thread(() -> {
                // create new contact
                Contact contact = new Contact();
                contact.name = name;
                contact.phoneNumber = phoneNumber;
                contact.email = email;
                contact.pictureUri = pictureUri;
                // insert into database and retrieve id
                contact.id = database.getContactDao().insert(contact);
                // return to contacts page
                view.goBackToContactsPage(contact);
            }).start();
        }
        else view.printErrorMessage();
    }

    public Boolean isNotEmpty(String name, String phoneNumber, String email) {
        return name != null && name.length() > 0 &&
                phoneNumber != null && phoneNumber.length() > 0 &&
                email != null && email.length() > 0;
    }
}
