package com.example.user.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import com.example.user.homework.models.GroupModel;
import com.example.user.homework.models.UserModel;
import com.example.user.homework.utils.UiUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Collections;
import java.util.Objects;

public class CreateGroupActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = Objects.requireNonNull(user).getUid();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("userInformation");
    private UserModel currentUserModel = null;

    private EditText edtName, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Button btnCreate = findViewById(R.id.btn_create_group);
        edtPassword = findViewById(R.id.et_group_password);
        edtName = findViewById(R.id.et_group_name);

        ((HomeworkApplication) getApplication()).addUserListener(userModel -> currentUserModel = userModel);

        btnCreate.setOnClickListener(v -> {
            final int count = currentUserModel.getCreateCount();
            final String groupName = edtName.getText().toString();
            final String groupPassword = edtPassword.getText().toString();
            final String groupId = count + uid;
            if (TextUtils.isEmpty(groupPassword)) {
                UiUtils.say(this, R.string.not_entered_password);
                return;
            }
            if (TextUtils.isEmpty(groupName)) {
                UiUtils.say(this, R.string.not_entered_group_name);
                return;
            }
            if (currentUserModel != null) {
                final GroupModel groupModel = new GroupModel(
                    groupName,
                    groupId,
                    groupPassword,
                    Collections.singletonList(uid)
                );
                FirebaseDatabase.getInstance().getReference().child(groupId).setValue(groupModel);
                reference.child("groupModels").child(String.valueOf(count)).setValue(groupId);
                reference.child("createCount").setValue(count + 1);
                Intent intent = new Intent(getApplicationContext(), GroupViewActivity.class);
                intent.putExtra("GROUPID", groupId);
                startActivity(intent);
            }
        });
    }
}
