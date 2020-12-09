package com.example.contacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.FileProvider;

import com.example.contacts.components.ImageSelector;
import com.example.contacts.components.MaterialInput;
import com.example.contacts.models.Contact;
import com.example.contacts.presenters.CreateOrUpdateContactPresenter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateOrUpdateContactActivity extends BaseActivity implements CreateOrUpdateContactPresenter.MVPView {
    private CreateOrUpdateContactPresenter presenter;
    private LinearLayout mainLayout;
    private ImageSelector imageSelector;
    private MaterialInput nameInput;
    private MaterialInput phoneNumInput;
    private MaterialInput emailInput;
    String currentPhotoPath;
    private static int PICK_IMAGE = 1;
    private static int TAKE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPhotoPath = "";
        presenter = new CreateOrUpdateContactPresenter(this);
        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);
        presenter.loadContact(id);
        mainLayout = new LinearLayout(this);

        // create MaterialInputs
        imageSelector = new ImageSelector(this, () -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Select Image: ")
                    .setItems(new CharSequence[]{"From Camera", "From Photos"}, (view, i) -> {
                        if (i == 0) {
                            // from camera
                            presenter.handleTakePicturePress();
                        }
                        if (i == 1) {
                            // from photos
                            presenter.handleSelectPicturePress();
                        }
                    }).show();
        });
        nameInput = new MaterialInput(this, "Name");
        phoneNumInput = new MaterialInput(this, "Phone Number");
        phoneNumInput.setInputType(InputType.TYPE_CLASS_PHONE);
        emailInput = new MaterialInput(this, "E-Mail");

        // create buttons
        MaterialButton saveButton = new MaterialButton(this, null, R.attr.materialButtonStyle);
        MaterialButton cancelButton = new MaterialButton(this, null, R.attr.borderlessButtonStyle);
        saveButton.setText("SAVE");
        cancelButton.setText("CANCEL");

        // button layout
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
            nameInput.setErrorEnabled(false);
            phoneNumInput.setErrorEnabled(false);
            emailInput.setErrorEnabled(false);
            // save contact info into database, return to ContactsActivity
            presenter.saveContact(
                    nameInput.getText().toString(),
                    phoneNumInput.getText().toString(),
                    emailInput.getText().toString(),
                    imageSelector.getImageUri()
            );
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
    public void goBackToPreviousPage(Contact newContact) {
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
    public void renderContactForm(Contact contact) {
        runOnUiThread(() -> {
            imageSelector.setImageUri(contact.pictureUri);
            nameInput.setText(contact.name);
            phoneNumInput.setText(contact.phoneNumber);
            emailInput.setText(contact.email);
        });
    }

    @Override
    public void displayNameError() {
        runOnUiThread(() -> {
            Snackbar.make(mainLayout, "Name cannot be blank", Snackbar.LENGTH_SHORT).show();
            nameInput.setErrorEnabled(true);
            nameInput.setError("Name cannot be blank");
        });
    }

    @Override
    public void displayPhoneNumError() {
        runOnUiThread(() -> {
            Snackbar.make(mainLayout, "Contact needs a phone number", Snackbar.LENGTH_SHORT).show();
            phoneNumInput.setErrorEnabled(true);
            phoneNumInput.setError("Contact needs a phone number");
        });
    }

    @Override
    public void displayEmailError() {
        runOnUiThread(() -> {
            Snackbar.make(mainLayout, "Must have a valid e-mail address", Snackbar.LENGTH_SHORT).show();
            emailInput.setErrorEnabled(true);
            emailInput.setError("Must have a valid e-mail address");
        });
    }

    @Override
    public void takePicture() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "CONTACT_" + timeStamp + ".jpg";
        File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
        currentPhotoPath = imageFile.getAbsolutePath();
        Uri photoUri = FileProvider.getUriForFile(this, "com.example.contacts.provider", imageFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            String uri = data.getData().toString();
            presenter.handlePictureSelected(uri);
        }
        if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            presenter.handlePictureSelected(currentPhotoPath);
        }
    }
}
