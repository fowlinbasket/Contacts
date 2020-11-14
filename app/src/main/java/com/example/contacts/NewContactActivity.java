package com.example.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;

import com.example.contacts.components.LabelledInput;
import com.example.contacts.models.Contact;
import com.example.contacts.presenters.NewContactPresenter;

public class NewContactActivity extends BaseActivity implements NewContactPresenter.MVPView {
    private NewContactPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new NewContactPresenter(this);
        LinearLayout mainLayout = new LinearLayout(this);

        // create LabelledInputs
        LabelledInput nameInput = new LabelledInput(this, "Name: ");
        LabelledInput numInput = new LabelledInput(this, "Phone Number: ");
        LabelledInput emailInput = new LabelledInput(this, "E-Mail: ");
        AppCompatButton saveButton = new AppCompatButton(this);
        saveButton.setText("Save Contact");

        saveButton.setOnClickListener(view -> {
            // save contact info into database, return to ContactsActivity
            presenter.createContact(nameInput.data.getText().toString(), numInput.data.getText().toString(), emailInput.data.getText().toString());
        });
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(20, 0, 20, 0);
        saveButton.setLayoutParams(buttonParams);

        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(nameInput);
        mainLayout.addView(numInput);
        mainLayout.addView(emailInput);
        mainLayout.addView(saveButton);

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
}
