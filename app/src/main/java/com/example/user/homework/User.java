package com.example.user.homework;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String name, surname, email;
    private ArrayList<Group> groups;
    private Integer createCount = 0;
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    ArrayList<Group> getGroups() {
        return groups;
    }

    public Integer getCreateCount() {
        return createCount;
    }

    public void setCreateCount(Integer createCount) {
        this.createCount = createCount;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public User(String name, String surname, String email, ArrayList<Group> groups) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.groups = groups;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    void addGroup(String name, String password){
        Group group = new Group();
        group.setName(name);
        if (createCount == 0) {
            group.setId(uid);
        }
        else {
            group.setId(uid + "(" + createCount + "");
        }
        createCount++;
        group.setPassword(password);
        if (groups == null)  {
            groups = new ArrayList<>();
        }
        groups.add(group);
        DatabaseReference groupRef = reference.child(group.getId());
        groupRef.child("Password").setValue(password);
        groupRef.child("Name").setValue(name);
        ArrayList<String> admins = new ArrayList<>();
        admins.add(uid);
        groupRef.child("Admin").setValue(admins);
    }

}
