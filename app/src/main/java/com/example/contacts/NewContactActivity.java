package com.example.contacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.contacts.components.ImageSelector;
import com.example.contacts.components.LabelledInput;
import com.example.contacts.components.MaterialInput;
import com.example.contacts.models.Contact;
import com.example.contacts.presenters.NewContactPresenter;
import com.google.android.material.button.MaterialButton;

public class NewContactActivity extends BaseActivity implements NewContactPresenter.MVPView {
    private NewContactPresenter presenter;
    private LinearLayout mainLayout;
    private ImageSelector imageSelector;
    private static int PICK_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new NewContactPresenter(this);
        mainLayout = new LinearLayout(this);
        ScrollView scrollView = new ScrollView(this);

        // create LabelledInputs
        imageSelector = new ImageSelector(this, () -> {presenter.handleSelectPicturePress();});
        MaterialInput nameInput = new MaterialInput(this, "Name");
        MaterialInput phoneNumInput = new MaterialInput(this, "Phone Number");
        MaterialInput emailInput = new MaterialInput(this, "E-Mail");
        MaterialButton saveButton = new MaterialButton(this, null, R.attr.materialButtonStyle);
        MaterialButton cancelButton = new MaterialButton(this, null, R.attr.borderlessButtonStyle);
        saveButton.setText("SAVE");
        cancelButton.setText("CANCEL");

        LinearLayout buttonsLayout = new LinearLayout(this);
        buttonsLayout.addView(cancelButton);
        buttonsLayout.addView(saveButton);
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLayout.setPadding(48, 0, 48, 0);
        buttonsLayout.setGravity(Gravity.RIGHT);

        cancelButton.setOnClickListener(view -> {
            presenter.handleCancelPress();
        });

        saveButton.setOnClickListener(view -> {
            // save contact info into database, return to ContactsActivity
            presenter.createContact(nameInput.getText().toString(), phoneNumInput.getText().toString(), emailInput.getText().toString(), imageSelector.getImageUri());
        });

        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(imageSelector);
        mainLayout.addView(nameInput);
        mainLayout.addView(phoneNumInput);
        mainLayout.addView(emailInput);
        mainLayout.addView(buttonsLayout);

//        scrollView.addView(mainLayout);

        setContentView(mainLayout);
    }


    @Override
    public void goBackToContactsPage(Contact newContact) {
        if (newContact == null) {
            setResult(Activity.RESULT_CANCELED, null);
        }
        else {
            Intent intent = new Intent();
            // Send new contact back to contacts page
            intent.putExtra("result", newContact);
            setResult(Activity.RESULT_OK, intent);
        }
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

    @Override
    public void goToPhotos() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void displayImage(String uri) {
        imageSelector.setImageUri(uri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            String uri = data.getData().toString();
            presenter.handlePictureSelected(uri);
        }
    }
}
