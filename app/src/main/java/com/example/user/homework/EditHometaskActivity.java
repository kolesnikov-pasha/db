package com.example.user.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditHometaskActivity extends AppCompatActivity {

    ImageButton btnBack, btnAddAttachments;
    String task, day;
    EditText edtHometask;
    TextView txtLesson, txtDate;
    int number;
    String groupId;
    Button btnAdd;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hometask);
        edtHometask = findViewById(R.id.add_edt_task);
        txtLesson = findViewById(R.id.chosen_lesson_name);
        txtDate = findViewById(R.id.chosen_date);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String lesson = bundle.getString("Lesson");
        groupId = bundle.getString("GROUPID");
        assert groupId != null;
        reference = reference.child(groupId);
        day = bundle.getString("Day");
        number = bundle.getInt("Lesson number");
        txtDate.setText(day);
        day = day.substring(0, 2) + day.substring(3, 5) + day.substring(6, 10);
        txtLesson.setText(lesson);
        Log.e("DAY", day);
        Log.e("NUMBER", number + "");
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
        btnBack = findViewById(R.id.add_bar_home);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupViewActivity.class);
                intent.putExtra("GROUPID", groupId);
                startActivityForResult(intent, 0);
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
                    Intent intent = new Intent(getApplicationContext(), GroupViewActivity.class);
                    intent.putExtra("GROUPID", groupId);
                    startActivityForResult(intent, 0);
                }
            }
        });

        btnAddAttachments = findViewById(R.id.add_attachments);
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
