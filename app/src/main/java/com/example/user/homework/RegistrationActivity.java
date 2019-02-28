package com.example.user.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity {

    Button btnEndRegistration;
    EditText edtNewPassword, edtNewPasswordAgain, edtEmail, edtName, edtSurname;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    String deleteSpaces(String str) {
        while (str.length() > 0 && str.substring(str.length() - 1).equals(" ")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    boolean emptyMessage(String str,String message){
        if (str.isEmpty()) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    boolean check() {
        String email = deleteSpaces(edtEmail.getText().toString());
        String name = deleteSpaces(edtName.getText().toString());
        String surname = deleteSpaces(edtSurname.getText().toString());
        edtEmail.setText(email);
        edtName.setText(name);
        edtSurname.setText(surname);
        if (emptyMessage(email, "Email не введен")) {
            return false;
        }
        if (emptyMessage(name, "Имя не введено")) {
            return false;
        }
        if (emptyMessage(surname, "Фамилия не введена")) {
            return false;
        }
        return true;
    }

    void registration(final String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getParent(), task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                FirebaseUser user = auth.getCurrentUser();
                String uid = user.getUid();
                database.getReference().child("users").child(uid).child("userInformation")
                        .setValue(new User(edtName.getText().toString(),
                                edtSurname.getText().toString(), email, new ArrayList<>()));
                user.sendEmailVerification().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "На адрес " + email +
                                " выслано письмо для подтверждения регистрации", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                    }else{
                        Toast.makeText(getApplicationContext(), "Отправить письмо для подтверждения " +
                                "регистрации не получилось", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edtEmail = findViewById(R.id.edt_email);
        btnEndRegistration = findViewById(R.id.btn_end_registration);
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
                    Toast.makeText(getApplicationContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
