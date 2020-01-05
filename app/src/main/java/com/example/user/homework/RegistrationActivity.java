package com.example.user.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import com.example.user.homework.models.UserModel;
import com.example.user.homework.utils.UiUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity {

    private EditText edtNewPassword;
    private EditText edtEmail;
    private EditText edtName;
    private EditText edtSurname;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private boolean emptyMessage(@NonNull final String str, @StringRes final int messageId){
        if (str.isEmpty()) {
            UiUtils.say(this, messageId);
            return true;
        }
        return false;
    }

    boolean check() {
        final String email = edtEmail.getText().toString().trim();
        final String name = edtName.getText().toString().trim();
        final String surname = edtSurname.getText().toString().trim();
        edtEmail.setText(email);
        edtName.setText(name);
        edtSurname.setText(surname);
        if (emptyMessage(email, R.string.not_entered_email)) {
            return false;
        }
        if (emptyMessage(name, R.string.not_entered_name)) {
            return false;
        }
        if (emptyMessage(surname, R.string.not_entered_surname)) {
            return false;
        }
        return true;
    }

    void registration(final String email, final String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getParent(), task -> {
            if (!task.isSuccessful()) {
                UiUtils.say(this, R.string.registration_error);
                return;
            }
            final FirebaseUser user = auth.getCurrentUser();
            if (user == null) {
                return;
            }
            final String uid = user.getUid();
            database.getReference().child("users").child(uid).child("userInformation")
                    .setValue(new UserModel(edtName.getText().toString(),
                            edtSurname.getText().toString(), email, new ArrayList<>(), 0));
            user.sendEmailVerification().addOnCompleteListener(task1 -> {
                if (!task1.isSuccessful()) {
                    UiUtils.say(this, R.string.registration_error);
                    return;
                }
                UiUtils.say(this, R.string.send_confirmation_letter);
                startActivity(new Intent(this, AuthActivity.class));
            });
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edtEmail = findViewById(R.id.edt_email);
        edtNewPassword = findViewById(R.id.edt_password);
        edtName = findViewById(R.id.edt_name);
        edtSurname = findViewById(R.id.edt_surname);

        auth.signOut();

        findViewById(R.id.btn_end_registration).setOnClickListener(v -> {
            if (!check()) {
                return;
            }
            final EditText edtNewPasswordAgain = findViewById(R.id.edt_password_again);
            final String password = edtNewPassword.getText().toString();
            final String passwordAgain = edtNewPasswordAgain.getText().toString();
            final String email = edtEmail.getText().toString();
            if (TextUtils.equals(password, passwordAgain)) {
                registration(email, password);
            } else {
                UiUtils.say(this, R.string.not_equal_passwords);
            }
        });

    }
}
