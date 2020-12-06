package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.presenters.BaseMVPView;

public abstract class BaseActivity extends AppCompatActivity implements BaseMVPView {
    @Override
    public AppDatabase getContextDatabase() {
        return Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "contacts-database").build();
    }
}

/*
Contacts Page (ContactsPresenter and ContactsActivity):
    TODO: Display a circle with contact's first initial if contact has no picture
    TODO: Display a circle with contact's picture otherwise
    TODO: Contact name should be to the right of the circle
    TODO: Create a floating action button that takes user to NewContactActivity with an icon
    TODO: make ContactsActivity mainLayout a FrameLayout
New Contact Page (NewContactPresenter and NewContactActivity):
    TODO: Make all input fields material design standard (Copy MaterialInput from Blog app)
    TODO: Allow user to select photo from camera or photos on device (Use PictureSelector from Blog app)
      Picture should be displayed on new contact page immediately after selecting it
    TODO: validate fields after user presses submit button
     Name and phone number cannot be blank
     Email must have an @ symbol in it
     Display a Snackbar informing user what happened
     Mark offending field with an error with appropriate message
     When user clicks save again remove error messages and start saving process again
Contact Page (ContactPresenter and ContactActivity):
    TODO: display image of contact or default image if there is none
    TODO: display contact info using MaterialCardView
    TODO: allow user to make calls, send messages, and send emails by clicking on the corresponding field
    TODO: add floating action button to open a menu with options to edit or delete contact
       if user selects delete, a confirmation message should pop up first
Updating Contacts:
    TODO: change NewContactPresenter and NewContactActivity to be compatible with updating
 */
