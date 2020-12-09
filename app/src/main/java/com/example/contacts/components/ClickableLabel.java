package com.example.contacts.components;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.contacts.models.Contact;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

public class ClickableLabel extends LinearLayout {
    Contact contact;

    public ClickableLabel(Context context, Contact contact) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        this.contact = contact;
        setTag(contact.id);
        createViews();
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        createViews();
    }

    public long getContactId() {
        return contact.id;
    }

    private void createViews() {
        removeAllViews();

        // picture

        if (contact.pictureUri.equals("")) {
            // display first initial if there is no picture
            CircleDisplay initialDisplay = new CircleDisplay(getContext(), Character.toString(contact.name.charAt(0)));
            addView(initialDisplay);
        }
        else {
            // display picture
            float density = getResources().getDisplayMetrics().density;
            MaterialCardView photoView = new MaterialCardView(getContext());
            AppCompatImageView photo = new AppCompatImageView(getContext());
            photo.setImageURI(Uri.parse(contact.pictureUri));
            photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams photoParams = new LinearLayout.LayoutParams((int)(40 * density) , (int)(40 * density));
            photoView.addView(photo);
            photoView.setLayoutParams(photoParams);
            photoView.setRadius(20 * density);
            addView(photoView);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(48, 20, 0, 20);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nameParams.setMargins(48, 12, 0, 12);

        // name

        MaterialTextView name = new MaterialTextView(getContext());
        name.setText(contact.name);
        name.setTextSize(21);
        name.setLayoutParams(nameParams);
        addView(name);

        setLayoutParams(params);
    }

    public String getContactName() {return contact.name;}
}
