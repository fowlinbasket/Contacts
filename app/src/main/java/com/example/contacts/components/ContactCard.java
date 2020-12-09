package com.example.contacts.components;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.contacts.R;
import com.example.contacts.models.Contact;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

public class ContactCard extends MaterialCardView {
    Contact contact;
    MaterialTextView nameView;
    MaterialTextView phoneView;
    MaterialTextView emailView;
    FloatingActionButton fab;

    public ContactCard(Context context, Contact contact) {
        super(context);
        setTag(contact.id);
        this.contact = contact;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(48, 24, 48, 24);
        setLayoutParams(params);

        LinearLayout mainLayout = new LinearLayout(context);

        LinearLayout body = new LinearLayout(context);
        body.setPadding(0, 32, 0, 0);
        body.setOrientation(LinearLayout.VERTICAL);

        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(body);

        addView(mainLayout);

//        // Floating Action Button
//
//        fab = new FloatingActionButton(context);
//        fab.setImageResource(R.drawable.ic_baseline_edit_24);
//        MaterialCardView.LayoutParams fabParams = new MaterialCardView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        fabParams.gravity = Gravity.RIGHT;
//        fabParams.setMargins(0, 0, 48, 0);
//        fab.setLayoutParams(fabParams);
//        addView(fab);

        // Name

        nameView = new MaterialTextView(context, null, R.attr.textAppearanceHeadline6);
        nameView.setText(contact.name);
        nameView.setLayoutParams(params);
        body.addView(nameView);

        // Phone Number

        phoneView = new MaterialTextView(context);
        phoneView.setText(contact.phoneNumber);
        phoneView.setTextSize(18);
        phoneView.setLayoutParams(params);
        body.addView(phoneView);

        // E-Mail

        emailView = new MaterialTextView(context);
        emailView.setText(contact.email);
        emailView.setTextSize(18);
        emailView.setLayoutParams(params);
        body.addView(emailView);
    }

//    public void setFabOnClickListener(OnClickListener listener) { fab.setOnClickListener(listener); }
}
