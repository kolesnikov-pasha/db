package com.example.user.homework;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User implements Serializable {
    private String name;
    private String surname;
    private String email;
    private List<String> groups;
    private int createCount;

    public User() {}

    public User(String name, String surname, String email, List<String> groups, int createCount) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.groups = groups;
        this.createCount = createCount;
    }

    List<String> getGroups() {
        return groups;
    }

    String getEmail() {
        return email;
    }

    public int getCreateCount() {
        return createCount;
    }

    public String getName() {
        return name;
    }

    String getSurname() {
        return surname;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreateCount(Integer createCount) {
        this.createCount = createCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
