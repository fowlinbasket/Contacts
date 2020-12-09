package com.example.contacts.presenters;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.Contact;

public class CreateOrUpdateContactPresenter {
    private MVPView view;
    private AppDatabase database;
    private Contact contact;
    public static final int DEFAULT_ID = -1;

    public interface MVPView extends BaseMVPView {
        void goBackToPreviousPage(Contact contact);
        void goToPhotos();
        void displayImage(String uri);
        void renderContactForm(Contact contact);
        void displayNameError();
        void displayPhoneNumError();
        void displayEmailError();
        void takePicture();
    }

    public CreateOrUpdateContactPresenter(MVPView view) {
        this.view = view;
        this.database = this.view.getContextDatabase();
    }

    public void saveContact(String name, String phoneNumber, String email, String pictureUri) {
        if (name.equals("")) {
            view.displayNameError();
            return;
        }
        if (phoneNumber.equals("")) {
            view.displayPhoneNumError();
            return;
        }
        if (!email.equals("") && !email.contains("@")) {
            view.displayEmailError();
            return;
        }
        new Thread(() -> {
            Contact contactToSave;
            if (contact != null) {
                contactToSave = contact;
            }
            else {
                // create new contact
                contactToSave = new Contact();
            }
            contactToSave.name = name;
            contactToSave.phoneNumber = phoneNumber;
            contactToSave.email = email;
            contactToSave.pictureUri = pictureUri;

            if (contact != null) {
                database.getContactDao().update(contactToSave);
            }
            else {
                // insert into database and retrieve id
                contactToSave.id = database.getContactDao().insert(contactToSave);
                // return to contacts page
            }
            view.goBackToPreviousPage(contactToSave);
        }).start();
    }

    public Boolean isNotEmpty(String name, String phoneNumber, String email) {
        return name != null && name.length() > 0 &&
                phoneNumber != null && phoneNumber.length() > 0 &&
                email != null && email.length() > 0;
    }

    public void loadContact(long id) {
        if (id != DEFAULT_ID) {
            new Thread(() -> {
                contact = database.getContactDao().findByID(id);
                view.renderContactForm(contact);
            }).start();
        }
    }


    public void handleCancelPress() {
        view.goBackToPreviousPage(null);
    }

    public void handleSelectPicturePress() {
        view.goToPhotos();
    }

    public void handlePictureSelected(String uri) {
        view.displayImage(uri);
    }

    public void handleTakePicturePress() { view.takePicture(); }
}
