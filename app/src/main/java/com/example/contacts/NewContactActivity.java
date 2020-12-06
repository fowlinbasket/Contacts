package com.example.contacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.contacts.components.LabelledInput;
import com.example.contacts.components.MaterialInput;
import com.example.contacts.models.Contact;
import com.example.contacts.presenters.NewContactPresenter;

public class NewContactActivity extends BaseActivity implements NewContactPresenter.MVPView {
    private NewContactPresenter presenter;
    private LinearLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new NewContactPresenter(this);
        mainLayout = new LinearLayout(this);
        ScrollView scrollView = new ScrollView(this);

        // create LabelledInputs
        MaterialInput nameInput = new MaterialInput(this, "Name");
        MaterialInput phoneNumInput = new MaterialInput(this, "Phone Number");
        MaterialInput emailInput = new MaterialInput(this, "E-Mail");
        AppCompatButton saveButton = new AppCompatButton(this);
        saveButton.setText("Save");

        saveButton.setOnClickListener(view -> {
            // save contact info into database, return to ContactsActivity
            presenter.createContact(nameInput.getText().toString(), phoneNumInput.getText().toString(), emailInput.getText().toString(), "");
        });
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(20, 0, 20, 0);
        saveButton.setLayoutParams(buttonParams);

        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(nameInput);
        mainLayout.addView(phoneNumInput);
        mainLayout.addView(emailInput);
        mainLayout.addView(saveButton);

//        scrollView.addView(mainLayout);

        setContentView(mainLayout);
    }


    @Override
    public void goBackToContactsPage(Contact newContact) {
        Intent intent = new Intent();
        // Send new contact back to contacts page
        intent.putExtra("result", newContact);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void printErrorMessage() {
        AppCompatTextView errorMessage = new AppCompatTextView(this);
        errorMessage.setTextSize(20);
        errorMessage.setTextColor(Color.RED);
        errorMessage.setText("Error: Contact cannot have empty contents");
        LinearLayout.LayoutParams errorParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        errorParams.setMargins(20, 0, 20, 0);
        errorMessage.setLayoutParams(errorParams);
        mainLayout.addView(errorMessage);
    }
}
