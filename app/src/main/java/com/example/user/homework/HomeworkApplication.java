package com.example.user.homework;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.example.user.homework.listeners.GroupListener;
import com.example.user.homework.listeners.UserListener;
import com.example.user.homework.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class HomeworkApplication extends Application implements ValueEventListener {
    @Nullable
    private UserModel userModel;
    @Nullable
    private List<String> groupsId;
    @NonNull
    private final List<UserListener> userListeners = new ArrayList<>();
    @NonNull
    private final List<GroupListener> groupsListeners = new ArrayList<>();

    public void addUserListener(@NonNull final UserListener userListener) {
        userListeners.add(userListener);
        if (userModel != null) {
            userListener.onUserUpdate(userModel);
        }
    }

    public void addGroupListener(@NonNull final GroupListener groupListener) {
        groupsListeners.add(groupListener);
        if (groupsId != null) {
            groupListener.onUpdate(groupsId);
        }
    }

    private void updateUser(DataSnapshot dataSnapshot) {
        if (userModel == null) {
            userModel = new UserModel();
        }
        for (DataSnapshot child: dataSnapshot.getChildren()) {
            switch (child.getKey()) {
                case "createCount":
                    userModel.setCreateCount(child.getValue(Integer.class));
                    break;
                case "surname":
                    userModel.setSurname((String) child.getValue());
                    break;
                case "name":
                    userModel.setName((String) child.getValue());
                    break;
                case "email":
                    userModel.setEmail((String) child.getValue());
                    break;
                case "groupModels":
                    userModel.setGroups((List<String>) child.getValue());
            }
        }
        for (final UserListener listener: userListeners) {
            listener.onUserUpdate(userModel);
        }
    }

    private void updateGroups(DataSnapshot dataSnapshot) {
        groupsId = (List<String>) dataSnapshot.getValue();
        for (final GroupListener listener: groupsListeners) {
            listener.onUpdate(groupsId);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser == null) {
                return;
            }
            final DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference userReference = rootReference.child("users").child(currentUser.getUid()).child("userInformation");
            final DatabaseReference groupsReference = rootReference.child("groupModels");
            groupsReference.addValueEventListener(this);
            userReference.addValueEventListener(this);
        });
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getKey().equals("userInformation")) {
            updateUser(dataSnapshot);
        } else {
            updateGroups(dataSnapshot);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {}
}
