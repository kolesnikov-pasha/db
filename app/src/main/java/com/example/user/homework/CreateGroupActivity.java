package com.example.user.homework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class CreateGroupActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
    User currentUser = null;

    EditText edtName, edtPassword;
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        edtPassword = findViewById(R.id.et_group_password);
        btnCreate = findViewById(R.id.btn_create_group);
        edtName = findViewById(R.id.et_group_name);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                currentUser = dataSnapshot.getValue(User.class);
                Log.e("EMAIL", currentUser.getEmail());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                currentUser = dataSnapshot.getValue(User.class);
                Log.e("EMAIL", currentUser.getEmail());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtName.getText().toString().isEmpty() && !edtPassword.getText().toString().isEmpty()) {
                    if (currentUser != null) {
                        currentUser.createGroup(edtName.getText().toString(), edtPassword.getText().toString());
                        reference.child("userInformation").setValue(currentUser);
                        Intent intent = new Intent(getApplicationContext(), GroupViewActivity.class);
                        intent.putExtra("GROUPID", currentUser.getGroups()
                                .get(currentUser.getGroups().size() - 1));
                        startActivity(intent);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Введите пароль и имя группы", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
