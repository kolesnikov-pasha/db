package com.example.user.homework;

import android.annotation.SuppressLint;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupPasswordActivity extends AppCompatActivity {

    ImageButton btnBurger;
    DrawerLayout mDrawerLayout;
    TextView txtName;
    Button btnSignIn;
    Integer number = 0;
    EditText edtPassword;
    String name, id, truePassword, userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    boolean ready = false;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    void addGroup(){
        reference.child("users").child(userId).child("userInformation").child("groups").child(number + "").child("name").setValue(name);
        reference.child("users").child(userId).child("userInformation").child("groups").child(number + "").child("id").setValue(id);
        reference.child("users").child(userId).child("userInformation").child("groups").child(number + "").child("password").setValue(truePassword);
        reference.child("users").child(userId).child("userInformation").child("createCount").setValue(number + 1);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_password);
        name = getIntent().getExtras().getString("NAME");
        id = getIntent().getExtras().getString("UID");
        txtName = findViewById(R.id.txt_group_name_password);
        btnSignIn = findViewById(R.id.btn_sign_in_password);
        edtPassword = findViewById(R.id.edt_group_password);
        txtName.setText("Пароль для группы \n" + name + ":");
        btnBurger = findViewById(R.id.btn_burger_groups);
        mDrawerLayout = findViewById(R.id.nav_bar_group_password);
        reference.child("users").child(userId).child("userInformation").child("createCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                number = dataSnapshot.getValue(Integer.class);
                System.out.println(number);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        btnBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        reference.child(id).child("Password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                truePassword = dataSnapshot.getValue(String.class);
                ready = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ready) {
                    if (edtPassword.getText().toString().equals(truePassword)){
                        addGroup();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Неверный пароль", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
