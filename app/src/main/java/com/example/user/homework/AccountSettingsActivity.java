package com.example.user.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountSettingsActivity extends AppCompatActivity {

    Button btnExit, btnChangeInf, btnChangePass;
    EditText edtOldPass, edtNewPass, edtNewName, edtNewSurname, edtRepeatNewPass;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNewPass.getText().toString().equals(edtRepeatNewPass.getText().toString())) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.signInWithEmailAndPassword(user.getEmail(), String.valueOf(edtOldPass.getText())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(String.valueOf(edtNewPass.getText())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Пароль успешно изменен", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Попробуйте другой пароль", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Неверный пароль", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Введенные пароли не совпадают", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnExit.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Вы вышли из аккаунта!", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            intent.putExtra("SIGNOUT", true);
            startActivity(intent);
        });

        btnChangeInf.setOnClickListener(v -> {
            reference.child("users").child(uid).child("userInformation").child("name").setValue(edtNewName.getText().toString());
            reference.child("users").child(uid).child("userInformation").child("surname").setValue(edtNewSurname.getText().toString());
            Toast.makeText(getApplicationContext(), "Данные успешно изменены", Toast.LENGTH_SHORT).show();
        });
    }
}
