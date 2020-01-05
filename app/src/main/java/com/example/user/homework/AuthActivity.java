package com.example.user.homework;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    private EditText edtEmail;
    private EditText edtPassword;
    private SharedPreferences sharedPreferences;

    private void signing(final String email, final String password){
        if (email.isEmpty()) {
            Toast.makeText(this, R.string.not_entered_email, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, R.string.not_entered_password, Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
                edtPassword.setText("");
                return;
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("EMAIL", email);
            editor.putString("PASSWORD", password);
            editor.apply();
            user = mAuth.getCurrentUser();
            if (user == null || !user.isEmailVerified()) {
                Toast.makeText(this, R.string.confirm_your_email, Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(this, R.string.welcome, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AuthActivity.this, GroupsListActivity.class);
            startActivity(intent);
        });
    }

    private void defaultSigning(final String email, final String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                user = mAuth.getCurrentUser();
                assert user != null;
                if (user.isEmailVerified()) {
                    Toast.makeText(this, R.string.welcome, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, GroupsListActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Intent from = getIntent();
        boolean signOut = false;
        try {
            signOut = from.getExtras().getBoolean("SIGNOUT");
        }
        catch (Exception ignored) {}
        sharedPreferences = getPreferences(MODE_PRIVATE);
        if (signOut){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("EMAIL");
            editor.remove("PASSWORD");
            editor.apply();
        }
        String email = sharedPreferences.getString("EMAIL", "");
        String password = sharedPreferences.getString("PASSWORD", "");

        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {
            defaultSigning(email, password);
        }

        Button signIn = findViewById(R.id.btn_sign_in);
        TextView txtRegistration = findViewById(R.id.btn_registration);
        TextView txtPassRemind = findViewById(R.id.txt_pass_remaind);
        edtEmail = findViewById(R.id.et_email);
        edtPassword = findViewById(R.id.et_password);

        txtPassRemind.setOnClickListener(view -> startActivity(new Intent(this, PasswordResetActivity.class)));
        txtRegistration.setOnClickListener(view -> startActivity(new Intent(this, RegistrationActivity.class)));
        signIn.setOnClickListener(view -> signing(edtEmail.getText().toString(), edtPassword.getText().toString()));
    }

}
