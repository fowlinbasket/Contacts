package com.example.contacts;

import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.contacts.components.ClickableLabel;
import com.example.contacts.models.Contact;
import com.example.contacts.presenters.ContactsPresenter;


public class ContactsActivity extends BaseActivity implements ContactsPresenter.MVPView {
   private final int CREATE_NEW_CONTACT = 1;
    ContactsPresenter presenter;
   LinearLayout mainLayout;
   LinearLayout contactsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create presenter and views
        presenter = new ContactsPresenter(this);
        mainLayout = new LinearLayout(this);
        contactsLayout = new LinearLayout(this);
        ScrollView scrollView = new ScrollView(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        contactsLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(mainLayout);

        // Button to create new contacts
        AppCompatButton createNewContactButton = new AppCompatButton(this);
        mainLayout.addView(createNewContactButton);
        mainLayout.addView(contactsLayout);
        createNewContactButton.setText("New Contact");
        // go to create new contact page
        createNewContactButton.setOnClickListener(view -> goToNewContactPage());
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(30, 10, 30, 10);
        createNewContactButton.setLayoutParams(buttonParams);

        setContentView(scrollView);
    }

    @Override
    public void renderContact(Contact contact) {
        runOnUiThread(() -> {
            // create clickable label for contact
            ClickableLabel label = new ClickableLabel(this, contact);
            label.setOnClickListener(view -> {
                Intent intent = new Intent(this, ContactActivity.class);
                intent.putExtra("id", contact.id);
                startActivity(intent);
            });
            contactsLayout.addView(label);
        });
    }

    @Override
    public void goToNewContactPage() {
        Intent intent = new Intent(this, NewContactActivity.class);
        startActivityForResult(intent, CREATE_NEW_CONTACT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_NEW_CONTACT && resultCode == Activity.RESULT_OK) {
            // load contact from result
            Contact contact = (Contact) data.getSerializableExtra("result");
            presenter.onNewContactCreated(contact);
        }
    }
}