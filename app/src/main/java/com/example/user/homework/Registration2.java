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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration2 extends AppCompatActivity {

    EditText edtGroupName;
    EditText edtAdminPassword;
    EditText edtAdminRePassword;
    Button Confirm;

    FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(User.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        edtAdminPassword = (EditText) findViewById(R.id.et_admin_password);
        edtAdminRePassword = (EditText) findViewById(R.id.et_admin_repassword);
        edtGroupName = (EditText) findViewById(R.id.et_group_name);
        Confirm = (Button) findViewById(R.id.btn_admin_enter);

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtAdminPassword.getText().toString().isEmpty() || edtAdminRePassword.getText().toString().isEmpty()
                        || edtGroupName.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (edtAdminPassword.getText().toString().equals(edtAdminRePassword.getText().toString())) {
                        reference.child("Name").setValue(edtGroupName.getText().toString());
                        reference.child("Admin").child("Password").setValue(edtAdminPassword.getText().toString());
                        User.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "На адрес " + User.getEmail() +
                                            " выслано письмо для подтверждения регистрации", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), AdminOptions.class));
                                }else{
                                    Toast.makeText(getApplicationContext(), "Отправить письмо для подтверждения " +
                                            "регистрации не получилось", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    else{
                        edtAdminRePassword.setText("");
                        Toast.makeText(getApplicationContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
