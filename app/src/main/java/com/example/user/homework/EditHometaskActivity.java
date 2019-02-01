package com.example.user.homework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditHometaskActivity extends AppCompatActivity {

    ImageButton btnBack, btnAddAttachments;
    String task, day;
    EditText edtHometask;
    TextView txtLesson;
    int number;
    Button btnAdd;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hometask);
        edtHometask = findViewById(R.id.add_edt_task);
        txtLesson = findViewById(R.id.chosen_lesson_name);
        String lesson = savedInstanceState.getString("Lesson");
        day = savedInstanceState.getString("Day");
        number = savedInstanceState.getInt("Lesson number");
        txtLesson.setText(lesson);
        reference = reference.child("task").child(day).child(String.valueOf(number));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                task = dataSnapshot.getValue(String.class);
                edtHometask.setText(task);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnBack = findViewById(R.id.app_bar_for_add_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddNewHometask.class));
            }
        });
        btnAdd = findViewById(R.id.add_btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = edtHometask.getText().toString();
                if (number == -1) Toast.makeText(getApplicationContext(), "Предмет не выбран", Toast.LENGTH_SHORT).show();
                else {
                    reference.setValue(task);
                    Toast.makeText(getApplicationContext(), "Задание добавлено", Toast.LENGTH_SHORT).show();
                    startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
                }
            }
        });


        btnAddAttachments = (ImageButton) findViewById(R.id.add_attachments);
        btnAddAttachments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
    }
}
