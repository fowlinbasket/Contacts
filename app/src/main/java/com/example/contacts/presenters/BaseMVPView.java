package com.example.contacts.presenters;

import com.example.contacts.database.AppDatabase;

public interface BaseMVPView {
    AppDatabase getContextDatabase();
}
