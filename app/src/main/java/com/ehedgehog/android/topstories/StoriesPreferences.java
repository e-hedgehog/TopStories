package com.ehedgehog.android.topstories;

import android.content.Context;
import android.preference.PreferenceManager;

public class StoriesPreferences {

    private static final String PREF_CATEGORY = "category";

    private static final int DEFAULT_CATEGORY_POSITION = 7;

    public static int getStoredCategory(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_CATEGORY, 0);
    }

    public static void setStoredCategory(Context context, int category) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_CATEGORY, category)
                .apply();
    }

}
