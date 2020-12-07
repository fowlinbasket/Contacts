package com.example.contacts;

import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.contacts.components.ClickableLabel;
import com.example.contacts.models.Contact;
import com.example.contacts.presenters.ContactsPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ContactsActivity extends BaseActivity implements ContactsPresenter.MVPView {
   private final int CREATE_NEW_CONTACT = 1;
    ContactsPresenter presenter;
   FrameLayout mainLayout;
   LinearLayout contactsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create presenter and views
        presenter = new ContactsPresenter(this);
        mainLayout = new FrameLayout(this);
        contactsLayout = new LinearLayout(this);
        ScrollView scrollView = new ScrollView(this);

        contactsLayout.setOrientation(LinearLayout.VERTICAL);


        FloatingActionButton fab = new FloatingActionButton(this);
        FrameLayout.LayoutParams fabParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fabParams.setMargins(0, 0, 48, 48);
        fabParams.gravity = (Gravity.BOTTOM | Gravity.RIGHT);
        fab.setLayoutParams(fabParams);
        fab.setImageResource(R.drawable.ic_baseline_add_24);

        // go to create new contact page
        fab.setOnClickListener(view -> goToNewContactPage());
        scrollView.addView(contactsLayout);

        mainLayout.addView(scrollView);
        mainLayout.addView(fab);

        setContentView(mainLayout);
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