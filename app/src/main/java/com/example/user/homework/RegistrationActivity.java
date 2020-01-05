package com.example.user.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.user.homework.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity {

    private EditText edtNewPassword;
    private EditText edtNewPasswordAgain;
    private EditText edtEmail;
    private EditText edtName;
    private EditText edtSurname;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    String deleteSpaces(String str) {
        while (str.length() > 0 && str.substring(str.length() - 1).equals(" ")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    boolean emptyMessage(@NonNull final String str, @StringRes final int messageId){
        if (str.isEmpty()) {
            Toast.makeText(getApplicationContext(), messageId, Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    boolean check() {
        final String email = deleteSpaces(edtEmail.getText().toString());
        final String name = deleteSpaces(edtName.getText().toString());
        final String surname = deleteSpaces(edtSurname.getText().toString());
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
                Toast.makeText(this, R.string.registration_error, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, R.string.registration_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, R.string.send_confirmation_letter, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AuthActivity.class));
            });
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edtEmail = findViewById(R.id.edt_email);
        Button btnEndRegistration = findViewById(R.id.btn_end_registration);
        edtNewPassword = findViewById(R.id.edt_password);
        edtName = findViewById(R.id.edt_name);
        edtSurname = findViewById(R.id.edt_surname);
        edtNewPasswordAgain = findViewById(R.id.edt_password_again);

        auth.signOut();

        btnEndRegistration.setOnClickListener(v -> {
            if (check()) {
                if (edtNewPassword.getText().toString().equals(edtNewPasswordAgain.getText().toString())) {
                    registration(edtEmail.getText().toString(), edtNewPassword.getText().toString());
                } else {
                    Toast.makeText(this, R.string.not_equal_passwords,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
