package com.example.user.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class CreateGroupActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = Objects.requireNonNull(user).getUid();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("userInformation");
    private User currentUser = null;

    private EditText edtName, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Button btnCreate = findViewById(R.id.btn_create_group);
        edtPassword = findViewById(R.id.et_group_password);
        edtName = findViewById(R.id.et_group_name);

        ((HomeworkApplication) getApplication()).addUserListener(user -> currentUser = user);

        btnCreate.setOnClickListener(v -> {
            final int count = currentUser.getCreateCount();
            final String groupName = edtName.getText().toString();
            final String groupPassword = edtPassword.getText().toString();
            final String groupId = count + uid;
            if (TextUtils.isEmpty(groupName) || TextUtils.isEmpty(groupPassword)) {
                Toast.makeText(getApplicationContext(), "Введите пароль и имя группы", Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentUser != null) {
                final Group group = new Group(
                    groupName,
                    groupId,
                    groupPassword,
                    new ArrayList<>(Collections.singletonList(uid)));
                FirebaseDatabase.getInstance().getReference().child(groupId).setValue(group);
                reference.child("groups").child(String.valueOf(count)).setValue(groupId);
                reference.child("createCount").setValue(count + 1);
                Intent intent = new Intent(getApplicationContext(), GroupViewActivity.class);
                intent.putExtra("GROUPID", groupId);
                startActivity(intent);
            }
        });
    }
}
