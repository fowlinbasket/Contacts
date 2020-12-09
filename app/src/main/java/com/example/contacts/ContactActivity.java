package com.example.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.example.contacts.components.ContactCard;
import com.example.contacts.components.ImageSelector;
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
        presenter = new ContactPresenter(this);
        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
//        setContentView(mainLayout);
    }

    @Override
    public void renderContact(Contact contact) {
        runOnUiThread(() -> {
            frameLayout.removeAllViews();
            mainLayout.removeAllViews();
            frameLayout.addView(mainLayout);

            // Image

            AppCompatImageView image = new AppCompatImageView(this);
            LinearLayout.LayoutParams photoParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 900);
            image.setBackgroundColor(getResources().getColor(R.color.colorDarkBackground, null));
            if (contact.pictureUri.equals("")) {
                image.setImageResource(R.drawable.ic_baseline_add_a_photo_240);
            }
            else {
                image.setImageURI(Uri.parse(contact.pictureUri));
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            image.setLayoutParams(photoParams);
            mainLayout.addView(image);

            // Name

            nameView = new MaterialTextView(this);
            nameView.setText(contact.name);
            nameView.setTextSize(32);
            nameView.setTextColor(Color.WHITE);
            FrameLayout.LayoutParams nameParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            nameParams.gravity = Gravity.LEFT;
            nameParams.setMargins(48, 780, 0, 0);
            nameView.setLayoutParams(nameParams);
            frameLayout.addView(nameView);

            // Phone Number

            // TODO: Add call and message buttons to phoneLayout
            MaterialCardView phoneView = new MaterialCardView(this);
            LinearLayout phoneLayout = new LinearLayout(this);
            num = new MaterialTextView(this);
            num.setText(contact.phoneNumber);
            num.setTextSize(18);
            LinearLayout.LayoutParams numParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            numParams.setMargins(24, 24, 24, 24);
            num.setLayoutParams(numParams);
            LinearLayout.LayoutParams phoneViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            phoneViewParams.setMargins(48, 48, 48, 24);
            phoneView.setLayoutParams(phoneViewParams);
            phoneLayout.addView(num);
            phoneView.addView(phoneLayout);
            mainLayout.addView(phoneView);

            nameView.setOnClickListener(view -> {
                presenter.handleCallPressed(num.getText().toString());
            });
            phoneView.setOnClickListener(view -> {
                presenter.handleTextMessagePressed(num.getText().toString());
            });

            // E-Mail

            // TODO: Add email icon to emailLayout
            MaterialCardView emailView = new MaterialCardView(this);
            LinearLayout emailLayout = new LinearLayout(this);
            email = new MaterialTextView(this);
            email.setText(contact.email);
            email.setTextSize(18);
            LinearLayout.LayoutParams emailParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            emailParams.setMargins(24, 24, 24, 24);
            email.setLayoutParams(emailParams);
            LinearLayout.LayoutParams emailViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            emailViewParams.setMargins(48, 24, 48, 24);
            emailView.setLayoutParams(emailViewParams);
            emailLayout.addView(email);
            emailView.addView(emailLayout);
            mainLayout.addView(emailView);

            emailView.setOnClickListener(view -> {
                presenter.handleEmailPressed(email.getText().toString());
            });

            // Floating Action Button

            FloatingActionButton fab = new FloatingActionButton(this);
            fab.setImageResource(R.drawable.ic_baseline_edit_24);
            fab.setSize(FloatingActionButton.SIZE_MINI);
            FrameLayout.LayoutParams fabParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            fabParams.gravity = Gravity.RIGHT | Gravity.TOP;
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

//            mainLayout.addView(cardView);
            setContentView(frameLayout);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                .setPositiveButton("Delete", (view, i) -> {
                    presenter.deleteContact();
                })
                .setNeutralButton("Cancel", (view, i) -> {
                    view.dismiss();
                })
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
