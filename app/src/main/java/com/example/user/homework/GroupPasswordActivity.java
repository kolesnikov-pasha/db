package com.example.user.homework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.user.homework.utils.UiUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupPasswordActivity extends AppCompatActivity {

    TextView txtName;
    Button btnSignIn;
    Integer number = 0;
    EditText edtPassword;
    String name, id, truePassword, userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    boolean ready = false;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    void addGroup(){
        reference.child("groupModels").child(number + "").child("name").setValue(name);
        reference.child("groupModels").child(number + "").child("id").setValue(id);
        reference.child("groupModels").child(number + "").child("password").setValue(truePassword);
        reference.child("createCount").setValue(number + 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_password);

        final Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        name = bundle.getString("NAME");
        id = bundle.getString("UID");

        txtName = findViewById(R.id.txt_group_name_password);
        btnSignIn = findViewById(R.id.btn_sign_in_password);
        edtPassword = findViewById(R.id.edt_group_password);

        txtName.setText(
            getString(R.string.password_for_group).replace(getString(R.string.name_placeholder), name)
        );

        reference = reference.child("users").child(userId).child("userInformation");
        reference.child("createCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                number = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                UiUtils.say(getApplicationContext(), R.string.check_internet_connection);
            }
        });
        reference.child(id).child("Password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                truePassword = dataSnapshot.getValue(String.class);
                ready = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                UiUtils.say(getApplicationContext(), R.string.check_internet_connection);
            }
        });

        btnSignIn.setOnClickListener(v -> {
            if (ready) {
                if (edtPassword.getText().toString().equals(truePassword)){
                    addGroup();
                }
                else {
                    UiUtils.say(getApplicationContext(), R.string.wrong_password);
                }
            }
        });
    }
}
