package com.example.contacts.components;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.contacts.models.Contact;
import com.google.android.material.textview.MaterialTextView;

public class ClickableLabel extends LinearLayout {
    Contact contact;
    public ClickableLabel(Context context, Contact contact) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        this.contact = contact;

        CircleDisplay initialDisplay = new CircleDisplay(context, Character.toString(contact.name.charAt(0)));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(48, 20, 0, 20);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nameParams.setMargins(48, 12, 0, 12);
//        initialDisplay.setLayoutParams(params);
        MaterialTextView name = new MaterialTextView(context);
        name.setText(contact.name);
        name.setTextSize(21);
        name.setLayoutParams(nameParams);
        addView(initialDisplay);
        addView(name);

        setLayoutParams(params);
    }
}
