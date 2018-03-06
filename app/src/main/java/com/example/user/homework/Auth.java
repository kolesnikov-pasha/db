package com.example.user.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Auth extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser User = mAuth.getCurrentUser();

    private Button SignIn;
    private EditText EDTemail;
    private EditText EDTpassword;
    private TextView Registration;
    private FirebaseAuth.AuthStateListener mAuthListener;

    protected void signing(String email, String password){
        if (email.isEmpty()) Toast.makeText(this, "Email не введен", Toast.LENGTH_SHORT).show();
        else if (password.isEmpty()) Toast.makeText(this, "Пароль не введен", Toast.LENGTH_SHORT).show();
        else mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        User = mAuth.getCurrentUser();
                        if (User.isEmailVerified()) {
                            Toast.makeText(Auth.this, "Добро пожаловать", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Auth.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(Auth.this, "Ваш email не подтвержден, перейдите по ссылке в письме", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), Registration2.class));
                        }
                    }else{
                        Toast.makeText(Auth.this, "Ошибка входа", Toast.LENGTH_SHORT).show();
                        EDTpassword.setText("");
                    }
                }
            });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        SignIn = findViewById(R.id.btn_sign_in);
        EDTemail = findViewById(R.id.et_email);
        EDTpassword = findViewById(R.id.et_password);
        Registration = findViewById(R.id.btn_registration);
        if (User != null) {
            startActivityForResult(new Intent(Auth.this, MainActivity.class), 0);
        }
        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Auth.this, Registration1.class));
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                User = firebaseAuth.getCurrentUser();

            }
        };
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signing(EDTemail.getText().toString(), EDTpassword.getText().toString());
            }
        });
    }

}
