package com.example.user.homework;

import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration1 extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    EditText EDTpassword;
    EditText EDTemail;
    EditText EDTrepassword;
    Button Registrate;


    protected void registration(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful()){
                     FirebaseUser user = mAuth.getCurrentUser();
                     startActivity(new Intent(getApplicationContext(), Registration2.class));
                }else{
                    Toast.makeText(Registration1.this, "К этой почте уже привязан аккаунт", Toast.LENGTH_LONG).show();
                    EDTpassword.setText("");
                    EDTemail.setText("");
                    EDTrepassword.setText("");
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration1);
        EDTemail = (EditText) findViewById(R.id.et_reg_email);
        EDTpassword = (EditText) findViewById(R.id.et_reg_password);
        EDTrepassword = (EditText) findViewById(R.id.et_reg_repassword);
        Registrate = (Button) findViewById(R.id.btn_registration);
        Registrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EDTpassword.getText().toString().equals(EDTrepassword.getText().toString())){
                    registration(EDTemail.getText().toString(), EDTpassword.getText().toString());
                }
                else{
                    Toast.makeText(Registration1.this, "Введенные пароли не совпадают", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
