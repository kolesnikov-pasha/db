package com.example.user.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminOptionsActivity extends AppCompatActivity {

    private EditText edtChangeName;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private TextView groupName;
    private EditText[]  edtLesson = new EditText[10];

    private String[] weekDay;
    private String daySelectedNow;
    private String groupId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);

        weekDay = getResources().getStringArray(R.array.days_of_week);
        daySelectedNow = weekDay[0];
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        groupId = bundle.getString("GROUPID");
        if (groupId == null) {
            return;
        }
        reference = reference.child(groupId);

        edtChangeName = findViewById(R.id.admin_options_edt_NewName);
        groupName = findViewById(R.id.add_group_name);
        final Spinner chooseDayOfWeek = findViewById(R.id.admin_options_set_day_of_week);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weekDay);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseDayOfWeek.setAdapter(adapter);
        chooseDayOfWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                daySelectedNow = weekDay[i];
                adaptOption();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        reference.child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupName.setText(dataSnapshot.getValue(String.class));
                edtChangeName.setText(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        findViewById(R.id.admin_options_btn_safe).setOnClickListener(view -> {
            String[] schedule = new String[10];
            for (int i = 0; i < 10; i++) {
                schedule[i] = edtLesson[i].getText().toString();
            }
            for (int i = 0; i < 10; i++) {
                reference.child("lessonsSchedule").child(daySelectedNow).child(i + "").setValue(schedule[i]);
            }
            reference.child("Name").setValue(edtChangeName.getText().toString());
            Toast.makeText(getApplicationContext(), R.string.changes_saved, Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.add_bar_home).setOnClickListener(view -> {
            final Intent intent = new Intent(this, GroupViewActivity.class);
            intent.putExtra("GROUPID", groupId);
            startActivityForResult(intent, 0);
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
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }
}
