package com.example.user.homework.listeners;

import android.support.annotation.NonNull;

import com.example.user.homework.models.UserModel;

public interface UserListener {
    void onUserUpdate(@NonNull UserModel userModel);
}
