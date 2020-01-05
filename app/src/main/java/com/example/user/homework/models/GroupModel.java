package com.example.user.homework.models;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public class GroupModel implements Comparable<GroupModel>{
    private String name = "";
    private String id = "";
    private String password = "";
    private List<String> admin = new ArrayList<>();

    public List<String> getAdmin() {
        return admin;
    }

    public void setAdmin(List<String> admin) {
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

    public GroupModel() {}

    public GroupModel(String name, String id, String password, List<String> admin) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.admin = admin;
    }

    @Override
    public int compareTo(@NonNull GroupModel o) {
        return id.compareTo(o.id);
    }
}
