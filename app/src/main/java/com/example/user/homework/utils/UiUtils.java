package com.example.user.homework.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class UiUtils {
    public static void say(@NonNull final Context context, @StringRes final int textId) {
        Toast.makeText(context, textId, Toast.LENGTH_SHORT).show();
    }

    public static void say(@NonNull final Context context, @NonNull final String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
