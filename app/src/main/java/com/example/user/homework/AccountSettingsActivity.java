package com.example.user.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountSettingsActivity extends AppCompatActivity {

    private EditText edtOldPass;
    private EditText edtNewPass;
    private EditText edtNewName;
    private EditText edtNewSurname;
    private EditText edtRepeatNewPass;

    private String uid;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        uid = user != null ? user.getUid() : "";
        edtNewName = findViewById(R.id.edt_change_new_name);
        edtNewSurname = findViewById(R.id.edt_change_new_surname) ;
        edtNewPass = findViewById(R.id.edt_change_new_pass);
        edtOldPass = findViewById(R.id.edt_change_old_pass);
        edtRepeatNewPass = findViewById(R.id.edt_repeat_new_pass);

        findViewById(R.id.btn_change_pass).setOnClickListener(v -> changePassword());
        findViewById(R.id.btn_exit).setOnClickListener(v -> exit());
        findViewById(R.id.btn_change_inf).setOnClickListener(v -> changeUserInformation());
    }

    private void changeUserInformation() {
        reference.child("users").child(uid).child("userInformation").child("name").setValue(edtNewName.getText().toString());
        reference.child("users").child(uid).child("userInformation").child("surname").setValue(edtNewSurname.getText().toString());
        Toast.makeText(this, R.string.data_changed_successfully, Toast.LENGTH_SHORT).show();
    }

    private void exit() {
        Toast.makeText(this, R.string.logout, Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
        intent.putExtra("SIGNOUT", true);
        startActivity(intent);
    }

    private void changePassword() {
        if (user == null) {
            return;
        }
        final String password = edtNewPass.getText().toString();
        final String passwordRepeat = edtRepeatNewPass.getText().toString();
        if (password.equals(passwordRepeat)) {
            final FirebaseAuth auth = FirebaseAuth.getInstance();
            final String email = user.getEmail();
            final String oldPassword = String.valueOf(edtOldPass.getText());
            if (email == null) {
                return;
            }
            auth.signInWithEmailAndPassword(email, oldPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user.updatePassword(password).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.password_cahnged_successfully, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.try_another_password, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), R.string.wrong_password, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.not_equal_passwords, Toast.LENGTH_SHORT).show();
        }
    }
}
