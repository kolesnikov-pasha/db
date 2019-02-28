package com.example.user.homework;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AddNewTaskActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference reference = firebaseDatabase.getReference();

    private TextView txtDayOfWeek, txtDate, txtGroupName;
    private View [] lessonsView = new View[10];

    private String strLesson = "";
    private int lessonNumber = -1;
    private final static int DIALOG_DATE = 1;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_hometask);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final String groupId = bundle.getString("GROUPID");
        assert groupId != null;

        reference = reference.child(groupId).child("lessonsSchedule");
        reference.getParent().child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtGroupName.setText(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        txtGroupName = findViewById(R.id.add_group_name);
        lessonsView[0] = findViewById(R.id.lesson_choose_0);
        lessonsView[1] = findViewById(R.id.lesson_choose_1);
        lessonsView[2] = findViewById(R.id.lesson_choose_2);
        lessonsView[3] = findViewById(R.id.lesson_choose_3);
        lessonsView[4] = findViewById(R.id.lesson_choose_4);
        lessonsView[5] = findViewById(R.id.lesson_choose_5);
        lessonsView[6] = findViewById(R.id.lesson_choose_6);
        lessonsView[7] = findViewById(R.id.lesson_choose_7);
        lessonsView[8] = findViewById(R.id.lesson_choose_8);
        lessonsView[9] = findViewById(R.id.lesson_choose_9);
        txtDayOfWeek = findViewById(R.id.day_of_week);
        txtDate = findViewById(R.id.date);
        LinearLayout chooseDateLayout = findViewById(R.id.choose_date);
        ImageButton btnHome = findViewById(R.id.add_bar_home);
        ImageButton btnNextDay = findViewById(R.id.next_day);
        ImageButton btnPrevDay = findViewById(R.id.prev_day);
        Button btnNext = findViewById(R.id.add_new_hometask_next_view);

        chooseDateLayout.setOnClickListener(view -> showDialog(DIALOG_DATE));
        btnHome.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), GroupViewActivity.class);
            intent.putExtra("GROUPID", groupId);
            startActivityForResult(intent, 0);
        });
        btnNextDay.setOnClickListener(view -> {
            nextDay();
            update();
            unchecked();
            lessonNumber = -1;
        });
        btnPrevDay.setOnClickListener(view -> {
            prevDay();
            update();
            unchecked();
            lessonNumber = -1;
        });
        btnNext.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EditHometaskActivity.class);
            intent.putExtra("Lesson", strLesson);
            intent.putExtra("GROUPID", groupId);
            intent.putExtra("Day", txtDate.getText());
            intent.putExtra("Lesson number", lessonNumber);
            startActivity(intent);
        });

        setDateText(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        changeDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));

        update();
        for (int i = 0; i < 10; i++) {
            final int x = i;
            (lessonsView[x].findViewById(R.id.lesson_chosen)).setOnClickListener(view -> {
                unchecked();
                ((CheckBox) lessonsView[x].findViewById(R.id.lesson_chosen)).setChecked(true);
                lessonNumber = x;
                strLesson = ((TextView) lessonsView[x].findViewById(R.id.lesson_name)).getText().toString();
            });
        }

    }

    protected Dialog onCreateDialog(int id){
        switch (id){
            case DIALOG_DATE:
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                return new DatePickerDialog(getApplicationContext(), myCallBack, year, month, day);
            default:
                return super.onCreateDialog(id);
        }
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setDateText(dayOfMonth, monthOfYear + 1, year);
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            changeDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
            getLessonsData(txtDayOfWeek.getText().toString());
        }
    };

    private void unchecked(){
        for (int j = 0; j < 10; j++) {
            ((CheckBox) lessonsView[j].findViewById(R.id.lesson_chosen)).setChecked(false);
        }
    }

    @SuppressLint("SetTextI18n")
    private void update(){
        for (int j = 0; j < 10; j++) {
            ((TextView) lessonsView[j].findViewById(R.id.lesson_number)).setText(j + ".");
            ((TextView) lessonsView[j].findViewById(R.id.lesson_name)).setText("");
        }
        getLessonsData(txtDayOfWeek.getText().toString());
    }

    private void prevDay(){
        changeDay(-1);
    }

    private void nextDay(){
        changeDay(1);
    }

    private void changeDay(int d){
        calendar.add(Calendar.DAY_OF_MONTH, d);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        setDateText(day, month, year);
        changeDayOfWeek(weekDay);
    }

    public void setDateText(int day, int month, int year){
        String res = "";
        if (day >= 10){
            res += day + ".";
        }
        else res += "0" + day + ".";
        if (month >= 10){
            res += month + ".";
        }
        else res += "0" + month + ".";
        res += year;
        txtDate.setText(res);
    }

    final String daysOfWeek[] = new String[]{"Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};

    public void changeDayOfWeek(int WeekDay){
        txtDayOfWeek.setText(daysOfWeek[WeekDay - 1]);
    }

    public void getLessonsData(String weekDay) {
        for (int j = 0; j < 10; j++) {
            final int x = j;
            reference.child(weekDay).child(String.valueOf(x)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ((TextView) lessonsView[x].findViewById(R.id.lesson_name)).setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Проверьте интернет соединение", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
