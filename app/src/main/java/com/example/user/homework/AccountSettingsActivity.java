package com.example.user.homework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountSettingsActivity extends AppCompatActivity {

    Button btnExit, btnChangeInf, btnChangePass;
    EditText edtOldPass, edtNewPass, edtNewName, edtNewSurname, edtRepeatNewPass;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

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
        edtRepeatNewPass = findViewById(R.id.edt_repeat_new_pass);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Вы вышли из аккаунта!", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                intent.putExtra("SIGNOUT", true);
                startActivity(intent);
            }
        });

        btnChangeInf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("users").child(uid).child("userInformation").child("name").setValue(edtNewName.getText().toString());
                reference.child("users").child(uid).child("userInformation").child("surname").setValue(edtNewSurname.getText().toString());
            }
        });
    }
}
