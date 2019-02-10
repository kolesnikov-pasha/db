package com.example.user.homework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class AccountSettingsActivity extends AppCompatActivity {

    Button btnExit, btnChangeInf, btnChangePass;
    EditText edtOldPass, edtNewPass, edtNewName, edtNewSurname;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        btnExit = findViewById(R.id.btn_exit);
        btnChangeInf = findViewById(R.id.btn_change_inf);
        btnChangePass = findViewById(R.id.btn_change_pass);
        edtNewName = findViewById(R.id.edt_change_new_name);
        edtNewSurname = findViewById(R.id.edt_change_new_surname) ;
        edtNewPass = findViewById(R.id.edt_change_new_pass);
        edtOldPass = findViewById(R.id.edt_change_old_pass);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), AuthActivity.class));
            }
        });
    }
}
