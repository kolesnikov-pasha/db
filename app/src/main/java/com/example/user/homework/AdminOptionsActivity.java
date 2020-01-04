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
    ImageButton btnHome;
    Button btnSafeChanges;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    TextView GroupName;
    Spinner chooseDayOfWeek;
    EditText[]  edtLesson = new EditText[10];

    String[] weekDay = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
    String daySelectedNow = "Понедельник";
    int daySelectedNowInt = 1;
    String groupId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        groupId = bundle.getString("GROUPID");
        assert groupId != null;
        reference = reference.child(groupId);

        btnHome = findViewById(R.id.add_bar_home);
        edtChangeName = findViewById(R.id.admin_options_edt_NewName);
        btnSafeChanges = findViewById(R.id.admin_options_btn_safe);
        GroupName = findViewById(R.id.add_group_name);
        chooseDayOfWeek = findViewById(R.id.admin_options_set_day_of_week);

        edtLesson[0] = findViewById(R.id.admin_options_edt_0);
        edtLesson[1] = findViewById(R.id.admin_options_edt_1);
        edtLesson[2] = findViewById(R.id.admin_options_edt_2);
        edtLesson[3] = findViewById(R.id.admin_options_edt_3);
        edtLesson[4] = findViewById(R.id.admin_options_edt_4);
        edtLesson[5] = findViewById(R.id.admin_options_edt_5);
        edtLesson[6] = findViewById(R.id.admin_options_edt_6);
        edtLesson[7] = findViewById(R.id.admin_options_edt_7);
        edtLesson[8] = findViewById(R.id.admin_options_edt_8);
        edtLesson[9] = findViewById(R.id.admin_options_edt_9);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, weekDay);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        chooseDayOfWeek.setAdapter(adapter);

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

        btnSafeChanges.setOnClickListener(view -> {
            String[] schedule = new String[10];
            for (int i = 0; i < 10; i++) {
                schedule[i] = edtLesson[i].getText().toString();
            }
            for (int i = 0; i < 10; i++) {
                reference.child("lessonsSchedule").child(daySelectedNow).child(i + "").setValue(schedule[i]);
            }
            reference.child("Name").setValue(edtChangeName.getText().toString());
            Toast.makeText(getApplicationContext(), "Изменения сохранены", Toast.LENGTH_SHORT).show();
        });
        btnHome.setOnClickListener(view -> {Intent intent = new Intent(getApplicationContext(), GroupViewActivity.class);
            intent.putExtra("GROUPID", groupId);
            startActivityForResult(intent, 0);
        });
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

        adaptOption();
    }

    public void adaptOption(){
        for (int i = 0; i < 10; i++) {
            final int x = i;
            reference.child("lessonsSchedule").child(daySelectedNow).child(i + "").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    edtLesson[x].setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
