package com.example.user.homework;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    private User user;
    @NonNull
    private final List<UserListener> listeners = new ArrayList<>();

    void addUserListener(@NonNull final UserListener userListener) {
        listeners.add(userListener);
        if (user != null) {
            userListener.onUserUpdate(user);
        }
    }

    private void updateUser(DataSnapshot dataSnapshot) {
        user = new User();
        for (DataSnapshot child: dataSnapshot.getChildren()) {
            switch (child.getKey()) {
                case "createCount":
                    user.setCreateCount(child.getValue(Integer.class));
                    break;
                case "surname":
                    user.setSurname((String) child.getValue());
                    break;
                case "name":
                    user.setName((String) child.getValue());
                    break;
                case "email":
                    user.setEmail((String) child.getValue());
                    break;
                case "groups":
                    user.setGroups((List<String>) child.getValue());
            }
        }
        for (final UserListener listener: listeners) {
            listener.onUserUpdate(user);
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
            userReference.addValueEventListener(this);
        });
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        updateUser(dataSnapshot);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {}
}
