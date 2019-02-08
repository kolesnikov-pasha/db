package com.example.user.homework;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    private Button signIn;
    private EditText edtEmail;
    private EditText edtPassword;
    private TextView txtRegistration;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private SharedPreferences sharedPreferences;

    protected void signing(final String email, final String password){
        if (email.isEmpty()) Toast.makeText(this, "Email не введен", Toast.LENGTH_SHORT).show();
        else if (password.isEmpty()) Toast.makeText(this, "Пароль не введен", Toast.LENGTH_SHORT).show();
        else mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        sharedPreferences = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("EMAIL", email);
                        editor.putString("PASSWORD", password);
                        editor.apply();
                        user = mAuth.getCurrentUser();
                        if (user.isEmailVerified()) {
                            Toast.makeText(AuthActivity.this, "Добро пожаловать", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(AuthActivity.this, "Ваш email не подтвержден, перейдите по ссылке в письме", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), Registration2.class));
                        }
                    }else{
                        Toast.makeText(AuthActivity.this, "Ошибка входа", Toast.LENGTH_SHORT).show();
                        edtPassword.setText("");
                    }
                }
            });
    }

    protected void defaultSigning(final String email, final String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    user = mAuth.getCurrentUser();
                    if (user.isEmailVerified()) {
                        Toast.makeText(AuthActivity.this, "Добро пожаловать", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getPreferences(MODE_PRIVATE);
        String email = sharedPreferences.getString("EMAIL", "");
        String password = sharedPreferences.getString("PASSWORD", "");
        if (!email.isEmpty() && !password.isEmpty()) defaultSigning(email, password);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        signIn = findViewById(R.id.btn_sign_in);
        edtEmail = findViewById(R.id.et_email);
        edtPassword = findViewById(R.id.et_password);
        txtRegistration = findViewById(R.id.btn_registration);
        txtRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(AuthActivity.this, Registration1.class));
                startActivity(new Intent(AuthActivity.this, RegistrationActivity.class));
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

            }
        };
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signing(edtEmail.getText().toString(), edtPassword.getText().toString());
            }
        });
    }

}
