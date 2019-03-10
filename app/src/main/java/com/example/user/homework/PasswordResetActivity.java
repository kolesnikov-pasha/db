package com.example.user.homework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    Button btnReset;
    EditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_remaind);
        btnReset = findViewById(R.id.btn_start_reset);
        edtEmail = findViewById(R.id.edt_email_for_password_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String email = edtEmail.getText().toString();
                while (email.contains(" ")){
                    email = email.substring(0, email.indexOf(" "));
                }
                System.out.println(email);
                auth.sendPasswordResetEmail(email);
                Toast.makeText(getApplicationContext(), "Перейдите по ссылке в письме", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
