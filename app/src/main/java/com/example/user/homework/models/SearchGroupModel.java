package com.example.user.homework.models;

import android.support.annotation.NonNull;

public class SearchGroupModel implements Comparable<SearchGroupModel>{
    private String name;
    private String uid;

    public SearchGroupModel() {}

    public SearchGroupModel(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "SearchGroupModel{" +
                "name='" + name + '\'' +
                ", id='" + uid + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull SearchGroupModel o) {
        if (name.equals(o.name)) {
            return uid.compareTo(o.uid);
        }
        else {
            return name.compareTo(o.name);
        }
    }
}
