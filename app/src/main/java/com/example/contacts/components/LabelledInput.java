package com.example.contacts.components;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;



public class LabelledInput extends LinearLayout {
    public AppCompatEditText data;
    public LabelledInput(Context context, String label) {
        super(context);

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(20, 0, 20, 0);
        // create label
        AppCompatTextView labelView = new AppCompatTextView(context);
        labelView.setText(label);
        // create input
        data = new AppCompatEditText(context);

        labelView.setTextSize(20);
        data.setTextSize(20);

        labelView.setLayoutParams(textParams);
        data.setLayoutParams(textParams);

        setOrientation(LinearLayout.VERTICAL);
        addView(labelView);
        addView(data);
    }
}
