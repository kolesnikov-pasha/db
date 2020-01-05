package com.example.user.homework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import com.example.user.homework.utils.UiUtils;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    private EditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_remaind);
        Button btnReset = findViewById(R.id.btn_start_reset);
        edtEmail = findViewById(R.id.edt_email_for_password_reset);
        btnReset.setOnClickListener(v -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String email = edtEmail.getText().toString();
            while (email.contains(" ")){
                email = email.substring(0, email.indexOf(" "));
            }
            System.out.println(email);
            auth.sendPasswordResetEmail(email);
            UiUtils.say(this, R.string.confirm_your_email);
        });
    }
}
