package com.example.contacts.components;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.contacts.R;

public class ImageSelector extends FrameLayout {
    private String imageUri;
    private AppCompatImageView imageView;

    public interface ImageSelectorClickListener {
        void onClick();
    }

    public ImageSelector(Context context, ImageSelectorClickListener listener) {
        this(context, listener, "");
    }

    public ImageSelector(Context context, ImageSelectorClickListener listener, String imageUri) {
        super(context);
        setBackgroundColor(getResources().getColor(R.color.colorDarkBackground, null));
        imageView = new AppCompatImageView(context);
        ViewGroup.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600);
        this.imageUri = imageUri;
        setImageUri(imageUri);
        imageView.setLayoutParams(params);
        setOnClickListener(view -> listener.onClick());

        addView(imageView);
    }

    public void setImageUri(String uri) {
        this.imageUri = uri;
        if (imageUri.equals("")) {
            imageView.setImageResource(R.drawable.ic_baseline_add_a_photo_240);
        }
        else {
            imageView.setImageURI(Uri.parse(imageUri));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    public String getImageUri() { return imageUri; }
}
