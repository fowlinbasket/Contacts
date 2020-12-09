package com.example.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.example.contacts.models.Contact;
import com.example.contacts.presenters.ContactPresenter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;


public class ContactActivity extends BaseActivity implements ContactPresenter.MVPView {
    long contactID;
    LinearLayout mainLayout;
    ContactPresenter presenter;
    FrameLayout frameLayout;
    MaterialTextView nameView;
    MaterialTextView num;
    MaterialTextView email;
    public static final int EDIT_CONTACT = 1;
    private final int REQUEST_PHONE_PERMISSIONS = 1;
    private final int REQUEST_SMS_PERMISSIONS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout = new FrameLayout(this);
        Intent intent = getIntent();
        // retrieve id from intent
        contactID = intent.getLongExtra("id", -1);
        // create presenter and layouts
        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        presenter = new ContactPresenter(this);
    }

    @Override
    public void renderContact(Contact contact) {
        runOnUiThread(() -> {
            frameLayout.removeAllViews();
            mainLayout.removeAllViews();
            frameLayout.addView(mainLayout);

            // Image

            AppCompatImageView image = new AppCompatImageView(this);
            image.setBackgroundColor(getResources().getColor(R.color.colorDarkBackground, null));
            LinearLayout.LayoutParams photoParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 900);
            image.setLayoutParams(photoParams);
            if (contact.pictureUri.equals("")) {
                image.setImageResource(R.drawable.ic_baseline_photo_240);
                image.setPadding(0, 150, 0, 150);
            }
            else {
                image.setImageURI(Uri.parse(contact.pictureUri));
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            mainLayout.addView(image);

            // Name

            nameView = new MaterialTextView(this);
            nameView.setText(contact.name);
            nameView.setTextSize(32);
            nameView.setTextColor(Color.WHITE);
            FrameLayout.LayoutParams nameParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            nameParams.gravity = Gravity.START;
            nameParams.setMargins(48, 780, 0, 0);
            nameView.setLayoutParams(nameParams);
            frameLayout.addView(nameView);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            iconParams.setMargins(24, 36, 0, 36);

            // Phone Number

            MaterialCardView phoneView = new MaterialCardView(this);
            LinearLayout phoneLayout = new LinearLayout(this);
            num = new MaterialTextView(this);
            num.setText(contact.phoneNumber);
            num.setTextSize(18);
            LinearLayout.LayoutParams numParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            numParams.setMargins(48, 36, 24, 36);
            numParams.weight = 1;
            num.setLayoutParams(numParams);
            LinearLayout.LayoutParams phoneViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            phoneViewParams.setMargins(48, 48, 48, 24);
            phoneView.setLayoutParams(phoneViewParams);
            // call icon
            AppCompatImageView phoneIcon = new AppCompatImageView(this);
            phoneIcon.setImageResource(R.drawable.ic_baseline_call_24);
            phoneIcon.setLayoutParams(iconParams);
            phoneLayout.addView(phoneIcon);
            // number
            phoneLayout.addView(num);
            phoneView.addView(phoneLayout);
            mainLayout.addView(phoneView);
            // message icon
            AppCompatImageView messageIcon = new AppCompatImageView(this);
            messageIcon.setImageResource(R.drawable.ic_baseline_message_24);
            LinearLayout.LayoutParams messageIconParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            messageIconParams.setMargins(48, 36, 24, 36);
            messageIcon.setLayoutParams(messageIconParams);
            phoneLayout.addView(messageIcon);
            // onClickListeners
            phoneIcon.setOnClickListener(view -> presenter.handleCallPressed(num.getText().toString()));
            messageIcon.setOnClickListener(view -> presenter.handleTextMessagePressed(num.getText().toString()));

            // E-Mail

            if (!contact.email.equals("")) {
                MaterialCardView emailView = new MaterialCardView(this);
                LinearLayout emailLayout = new LinearLayout(this);
                email = new MaterialTextView(this);
                email.setText(contact.email);
                email.setTextSize(18);
                LinearLayout.LayoutParams emailParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                emailParams.setMargins(48, 36, 24, 36);
                email.setLayoutParams(emailParams);
                LinearLayout.LayoutParams emailViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                emailViewParams.setMargins(48, 24, 48, 24);
                emailView.setLayoutParams(emailViewParams);
                // email icon
                AppCompatImageView emailIcon = new AppCompatImageView(this);
                emailIcon.setImageResource(R.drawable.ic_baseline_email_24);
                emailLayout.addView(emailIcon);
                emailIcon.setLayoutParams(iconParams);

                emailLayout.addView(email);
                emailView.addView(emailLayout);
                mainLayout.addView(emailView);

                emailIcon.setOnClickListener(view -> presenter.handleEmailPressed(email.getText().toString()));
            }

            // Floating Action Button

            FloatingActionButton fab = new FloatingActionButton(this);
            fab.setImageResource(R.drawable.ic_baseline_edit_24);
            fab.setSize(FloatingActionButton.SIZE_MINI);
            FrameLayout.LayoutParams fabParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            fabParams.gravity = Gravity.END | Gravity.TOP;
            fabParams.setMargins(0, 12, 12, 0);
            fab.setLayoutParams(fabParams);
            fab.setOnClickListener(button -> {
                PopupMenu popupMenu = new PopupMenu(this, button);
                popupMenu.getMenu().add("Edit");
                popupMenu.getMenu().add("Delete");
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getTitle().toString().equals("Edit")) {
                        // Handle edit press
                        presenter.handleEditPress();
                    }
                    else {
                        // Handle delete press
                        presenter.handleDeletePressed();
                    }
                    return true;
                });
                popupMenu.show();
            });
            frameLayout.addView(fab);

            setContentView(frameLayout);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_CONTACT && resultCode == Activity.RESULT_OK) {
            Contact contact = (Contact) data.getSerializableExtra("result");
            presenter.handleContactUpdated(contact);
        }
    }

    @Override
    public long getId() {
        return contactID;
    }

    @Override
    public void goBackToContactsPage(Contact contact, boolean didDelete, boolean didUpdate) {
        Intent intent = new Intent();
        if (didDelete) {
            intent.putExtra("id", contact.id);
            setResult(ContactsActivity.DELETED_RESULT, intent);
        }
        else if (didUpdate) {
            intent.putExtra("contact", contact);
            setResult(ContactsActivity.UPDATED_RESULT, intent);
        }
        else setResult(Activity.RESULT_CANCELED, null);
        finish();
    }

    @Override
    public void displayDeleteConfirmation() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Are you sure you want to delete this contact?")
                .setPositiveButton("Delete", (view, i) -> presenter.deleteContact())
                .setNeutralButton("Cancel", (view, i) -> view.dismiss())
                .show();
    }

    @Override
    public void goToEditPage(Contact contact) {
        Intent intent = new Intent(this, CreateOrUpdateContactActivity.class);
        intent.putExtra("id", contact.id);
        startActivityForResult(intent, EDIT_CONTACT);
    }

    @Override
    public void makePhoneCall(String number) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+number));
            startActivity(callIntent);
        }
        else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_PERMISSIONS);
        }
    }

    @Override
    public void sendTextMessage(String number) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("sms:"+number));
            startActivity(smsIntent);
        }
        else {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSIONS);
        }
    }

    @Override
    public void sendEmail(String address) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"+address));
        startActivity(emailIntent);
    }


    @Override
    public void onBackPressed() {
        presenter.handleBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.handleCallPressed(num.getText().toString());
            }
        }
        if (requestCode == REQUEST_SMS_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.handleTextMessagePressed(num.getText().toString());
            }
        }
    }
}
