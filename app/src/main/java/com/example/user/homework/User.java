package com.example.user.homework;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String name, surname, email;
    private ArrayList<String> groups;
    private Integer createCount = 0;
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    ArrayList<String> getGroups() {
        return groups;
    }

    public Integer getCreateCount() {
        return createCount;
    }

    public void setCreateCount(Integer createCount) {
        this.createCount = createCount;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    public User(String name, String surname, String email, ArrayList<String> groups) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.groups = groups;
    }

    public User() {
    }

    String getEmail() {
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

    void createGroup(String name, String password){
        String group = "";
        if (createCount == 0) {
            group = uid;
        }
        else {
            group = uid + "(" + createCount + "";
        }
        createCount++;
        if (groups == null)  {
            groups = new ArrayList<>();
        }
        groups.add(group);
        DatabaseReference groupRef = reference.child(group);
        groupRef.child("Password").setValue(password);
        groupRef.child("Name").setValue(name);
        ArrayList<String> admins = new ArrayList<>();
        admins.add(uid);
        groupRef.child("Admin").setValue(admins);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final String finalGroup = group;
        reference.child("groupsCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer count = dataSnapshot.getValue(Integer.class);
                reference.child("groups").child(count + "").setValue(finalGroup);
                reference.child("groupsCount").removeEventListener(this);
                reference.child("groupsCount").setValue(count + 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
