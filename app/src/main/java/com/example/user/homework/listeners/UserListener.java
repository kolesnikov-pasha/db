package com.example.user.homework.listeners;

import android.support.annotation.NonNull;

import com.example.user.homework.models.User;

public interface UserListener {
    void onUserUpdate(@NonNull User user);
}
