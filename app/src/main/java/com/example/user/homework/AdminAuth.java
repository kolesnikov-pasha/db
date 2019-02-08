package com.example.user.homework;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class AdminAuth extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
            .child("Admin").child("Password");

    EditText edtPassword;
    Button btnInput;
    String realPass = "", in = "";
    int next = 0;

    void logining() {
        if (realPass.equals(in)){
            sharedPreferences = getPreferences(MODE_PRIVATE);
            sharedPreferences.edit().putString("PASS", realPass).apply();
            startActivity(to);
        }
        else {
            Toast.makeText(AdminAuth.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
            edtPassword.setText("");
        }
    }

    void ready(){
        if (!in.isEmpty()) {
            logining();
        }
    }

    void waiting(String input){
        in = input;
    }

    Intent to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_auth);
        sharedPreferences = getPreferences(MODE_PRIVATE);
        if (sharedPreferences.contains("PASS")) {
            in = sharedPreferences.getString("PASS", "");
            waiting(in);

        }
        else {
            in = "";
        }
        Intent intent = getIntent();
        next = intent.getExtras().getInt("Next");
        switch (next) {
            case 1:{
                to = new Intent(getApplicationContext(), AddNewHometask.class);
                break;
            }
            case 2:{
                to = new Intent(getApplicationContext(), AdminOptions.class);
                break;
            }
            case 3:{
                to = new Intent(getApplicationContext(), EditHometaskActivity.class);
                to.putExtra("Lesson", (String) intent.getExtras().get("Lesson"));
                to.putExtra("Lesson number",
                        (Integer) intent.getExtras().get("Lesson number"));
                to.putExtra("Day", (String) intent.getExtras().get("Day"));
                break;
            }
        }
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                realPass = dataSnapshot.getValue(String.class);
                ready();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        edtPassword = findViewById(R.id.adminpasswordinput_edt_Password);
        edtPassword.setText(in);
        btnInput = findViewById(R.id.adminpasswordinput_btn_confirm_password);
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (realPass.isEmpty()) {
                    waiting(edtPassword.getText().toString());
                }
                else {
                    in = edtPassword.getText().toString();
                    logining();
                }
            }
        });
    }

}
