package com.example.user.homework.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Group implements Comparable<Group>{
    private String name = "";
    private String id = "";
    private String password = "";
    private ArrayList<String> admin = new ArrayList<>();

    public ArrayList<String> getAdmin() {
        return admin;
    }

    public void setAdmin(ArrayList<String> admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Group() {}

    public Group(String name, String id, String password, ArrayList<String> admin) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.admin = admin;
    }

    @Override
    public int compareTo(@NonNull Group o) {
        return id.compareTo(o.id);
    }
}
