package com.example.user.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminOptionsActivity extends AppCompatActivity {

    EditText edtChangeName;
    Button btnSafeChanges;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    TextView GroupName, txtAct;
    Spinner chooseDayOfWeek;
    EditText  edtLesson0, edtLesson1, edtLesson2, edtLesson3, edtLesson4, edtLesson5, edtLesson6,
            edtLesson7, edtLesson8, edtLesson9;

    String[] weekDay = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
    String daySelectedNow = "Понедельник";
    int daySelectedNowInt = 1;
    ImageButton btnHome;
    String groupId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);

        Bundle bundle = getIntent().getExtras();
        groupId = bundle.getString("GROUPID");
        reference = reference.child(groupId);
        btnHome = findViewById(R.id.add_bar_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Intent intent = new Intent(getApplicationContext(), GroupViewActivity.class);
                intent.putExtra("GROUPID", groupId);
                startActivityForResult(intent, 0);
            }
        });
        edtChangeName = findViewById(R.id.admin_options_edt_NewName);
        btnSafeChanges = findViewById(R.id.admin_options_btn_safe);
        GroupName = findViewById(R.id.add_group_name);
        txtAct = findViewById(R.id.add_group_name);
        chooseDayOfWeek = findViewById(R.id.admin_options_set_day_of_week);

        txtAct.setText("Редактировать группу");

        edtLesson0 = findViewById(R.id.admin_options_edt_0);
        edtLesson1 = findViewById(R.id.admin_options_edt_1);
        edtLesson2 = findViewById(R.id.admin_options_edt_2);
        edtLesson3 = findViewById(R.id.admin_options_edt_3);
        edtLesson4 = findViewById(R.id.admin_options_edt_4);
        edtLesson5 = findViewById(R.id.admin_options_edt_5);
        edtLesson6 = findViewById(R.id.admin_options_edt_6);
        edtLesson7 = findViewById(R.id.admin_options_edt_7);
        edtLesson8 = findViewById(R.id.admin_options_edt_8);
        edtLesson9 = findViewById(R.id.admin_options_edt_9);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, weekDay);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        chooseDayOfWeek.setAdapter(adapter);

        chooseDayOfWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                daySelectedNowInt = i + 1;
                daySelectedNow = weekDay[i];
                adaptOption();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        reference.child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GroupName.setText(dataSnapshot.getValue(String.class));
                edtChangeName.setText(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnSafeChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] schedule = new String[10];
                schedule[0] = edtLesson0.getText().toString();
                schedule[1] = edtLesson1.getText().toString();
                schedule[2] = edtLesson2.getText().toString();
                schedule[3] = edtLesson3.getText().toString();
                schedule[4] = edtLesson4.getText().toString();
                schedule[5] = edtLesson5.getText().toString();
                schedule[6] = edtLesson6.getText().toString();
                schedule[7] = edtLesson7.getText().toString();
                schedule[8] = edtLesson8.getText().toString();
                schedule[9] = edtLesson9.getText().toString();
                for (int i = 0; i < 10; i++) {
                    reference.child("lessonsSchedule").child(daySelectedNow).child(i + "").setValue(schedule[i]);
                }
                reference.child("Name").setValue(edtChangeName.getText().toString());
                Toast.makeText(getApplicationContext(), "Изменения сохранены", Toast.LENGTH_SHORT).show();
            }
        });
        adaptOption();
    }

    public void adaptOption(){
        reference.child("lessonsSchedule").child(daySelectedNow).child("0").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edtLesson0.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child("lessonsSchedule").child(daySelectedNow).child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edtLesson1.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child("lessonsSchedule").child(daySelectedNow).child("2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edtLesson2.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child("lessonsSchedule").child(daySelectedNow).child("3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edtLesson3.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child("lessonsSchedule").child(daySelectedNow).child("4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edtLesson4.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child("lessonsSchedule").child(daySelectedNow).child("5").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edtLesson5.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child("lessonsSchedule").child(daySelectedNow).child("6").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edtLesson6.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child("lessonsSchedule").child(daySelectedNow).child("7").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edtLesson7.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child("lessonsSchedule").child(daySelectedNow).child("8").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edtLesson8.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child("lessonsSchedule").child(daySelectedNow).child("9").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edtLesson9.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
