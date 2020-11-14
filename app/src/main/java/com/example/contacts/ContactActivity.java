package com.example.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.contacts.models.Contact;
import com.example.contacts.presenters.ContactPresenter;


public class ContactActivity extends BaseActivity implements ContactPresenter.MVPView {
    long contactID;
    LinearLayout mainLayout;
    ContactPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        // retrieve id from intent
        contactID = intent.getLongExtra("id", -1);
        // create presenter and layouts
        presenter = new ContactPresenter(this);
        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(mainLayout);
    }

    @Override
    public void renderContact(Contact contact) {
        runOnUiThread(() -> {
            // display name
            AppCompatTextView name = new AppCompatTextView(this);
            name.setText(contact.name);
            name.setTextSize(40);
            LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            nameParams.gravity = Gravity.CENTER;
            nameParams.setMargins(30, 50 ,30, 50);
            name.setLayoutParams(nameParams);
            // display phone number
            AppCompatTextView call = new AppCompatTextView(this);
            call.setText("Call: " + contact.phoneNumber);
            call.setTextSize(25);
            LinearLayout.LayoutParams callParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            callParams.setMargins(40, 20, 40, 20);
            call.setLayoutParams(callParams);
            // display email
            AppCompatTextView email = new AppCompatTextView(this);
            email.setText("E-Mail: " + contact.email);
            email.setTextSize(25);
            LinearLayout.LayoutParams emailParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            emailParams.setMargins(40, 20, 40, 20);
            email.setLayoutParams(emailParams);

            mainLayout.addView(name);
            mainLayout.addView(call);
            mainLayout.addView(email);
        });
    }

    @Override
    public long getId() {
        return contactID;
    }
}
