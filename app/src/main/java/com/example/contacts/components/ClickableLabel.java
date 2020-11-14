package com.example.contacts.components;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.contacts.models.Contact;

public class ClickableLabel extends LinearLayout {
    Contact contact;
    public ClickableLabel(Context context, Contact contact) {
        super(context);
        this.contact = contact;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 20, 30, 20);
        AppCompatTextView name = new AppCompatTextView(context);
        name.setText(contact.name);
        name.setTextSize(25);
        addView(name);

        setLayoutParams(params);
    }
}
