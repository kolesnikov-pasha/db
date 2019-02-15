package com.example.user.homework;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AddNewTaskActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = firebaseDatabase.getReference().child(User.getUid()).child("lessonsSchedule");
    DatabaseReference TasksReference = firebaseDatabase.getReference().child(User.getUid()).child("task");

    TextView txtDayOfWeek;
    TextView edtDay, chosenLesson, GroupName;
    String strLesson = "";
    int i;
    EditText edtTask;
    Button btnNext;
    int lessonNumber = -1;
    LinearLayout chooseDate;
    int myYear, myMonth, myDay;
    int DIALOG_DATE = 1;
    View [] lesson = new View[10];
    ImageButton nextDay, prevDay, btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_hometask);
        chooseDate = findViewById(R.id.add_layout_choose_date);
        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_DATE);
            }
        });
        GroupName = findViewById(R.id.add_group_name);
        reference.getParent().child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GroupName.setText(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnHome = findViewById(R.id.add_bar_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
            }
        });
        chosenLesson = findViewById(R.id.chosen_lesson_name);
        lesson[0] = findViewById(R.id.lesson_choose_0);
        lesson[1] = findViewById(R.id.lesson_choose_1);
        lesson[2] = findViewById(R.id.lesson_choose_2);
        lesson[3] = findViewById(R.id.lesson_choose_3);
        lesson[4] = findViewById(R.id.lesson_choose_4);
        lesson[5] = findViewById(R.id.lesson_choose_5);
        lesson[6] = findViewById(R.id.lesson_choose_6);
        lesson[7] = findViewById(R.id.lesson_choose_7);
        lesson[8] = findViewById(R.id.lesson_choose_8);
        lesson[9] = findViewById(R.id.lesson_choose_9);
        nextDay = findViewById(R.id.add_new_hometask_next_day);
        prevDay = findViewById(R.id.add_new_hometask_prev_day);
        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextday();
                update();
                unchecked();
                lessonNumber = -1;
            }
        });
        prevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevday();
                update();
                unchecked();
                lessonNumber = -1;
            }
        });
        txtDayOfWeek = findViewById(R.id.add_new_hometask_day_of_week);
        edtDay = findViewById(R.id.add_new_hometask_date);
        btnNext = findViewById(R.id.add_new_hometask_next_view);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditHometaskActivity.class);
                intent.putExtra("Lesson", strLesson);
                intent.putExtra("Day", edtDay.getText());
                intent.putExtra("Lesson number", lessonNumber);
                startActivity(intent);
            }
        });
        edtTask = (EditText) findViewById(R.id.add_edt_task);
        Calendar calendar = Calendar.getInstance();
        myDay = calendar.get(Calendar.DAY_OF_MONTH);
        myMonth = calendar.get(Calendar.MONTH);
        myYear = calendar.get(Calendar.YEAR);
        changeDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        changeDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));


        update();
        for (i = 0; i < 10; i++) {
            final int x = i;
            (lesson[x].findViewById(R.id.lesson_chosen)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    unchecked();
                    ((CheckBox) lesson[x].findViewById(R.id.lesson_chosen)).setChecked(true);
                    lessonNumber = x;
                    strLesson = ((TextView)lesson[x].findViewById(R.id.lesson_name)).getText().toString();
                }
            });
        }
    }

    protected Dialog onCreateDialog(int id){
        if (id == DIALOG_DATE) {
            return new DatePickerDialog(AddNewTaskActivity.this, myCallBack, myYear, myMonth, myDay);
        }
        else
            return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myYear = year;
            myMonth = monthOfYear;
            myDay = dayOfMonth;
            changeDate(myDay, myMonth + 1, myYear);
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, myMonth, dayOfMonth);
            changeDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
            adaptMain(txtDayOfWeek.getText().toString());
        }
    };

    private void unchecked(){
        for (int j = 0; j < 10; j++) {
            ((CheckBox)lesson[j].findViewById(R.id.lesson_chosen)).setChecked(false);
        }
    }

    private void update(){
        for (int j = 0; j < 10; j++) {
            ((TextView)lesson[j].findViewById(R.id.lesson_number)).setText(j + ".");
            ((TextView)lesson[j].findViewById(R.id.lesson_name)).setText("");

        }
        adaptMain(txtDayOfWeek.getText().toString());
    }

    private void prevday(){
        String date = edtDay.getText().toString();
        Integer day = Integer.valueOf(date.substring(0, 2));
        Integer month = Integer.valueOf(date.substring(3, 5));
        Integer year = Integer.valueOf(date.substring(6, 10));
        switch (month){
            case 5:;
            case 7:;
            case 10:;
            case 12:{
                if (day > 1) day--;
                else{
                    day = 30;
                    month--;
                }
                break;
            }
            case 2:;
            case 4:;
            case 6:;
            case 8:;
            case 9:;
            case 11:{
                if (day > 1) day--;
                else{
                    day = 31;
                    month--;
                }
                break;
            }
            case 3: {
                if ((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)){
                    if (day > 1) day--;
                    else {
                        day = 29;
                        month--;
                    }
                }
                else {
                    if (day > 1) day--;
                    else {
                        day = 28;
                        month--;
                    }
                }
                break;
            }
            case 1: {
                if (day > 1) day--;
                else {
                    day = 31;
                    month = 12;
                    year--;
                }
                break;
            }
        }
        changeDate(day, month, year);
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day);
        int WeekDay = c.get(Calendar.DAY_OF_WEEK);
        changeDayOfWeek(WeekDay);
    }

    private void nextday(){
        String date = edtDay.getText().toString();
        Integer day = Integer.valueOf(date.substring(0, 2));
        Integer month = Integer.valueOf(date.substring(3, 5));
        Integer year = Integer.valueOf(date.substring(6, 10));
        switch (month){
            case 4:;
            case 6:;
            case 9:;
            case 11:{
                if (day < 30) day++;
                else{
                    day = 1;
                    month++;
                }
                break;
            }
            case 1:;
            case 3:;
            case 5:;
            case 7:;
            case 8:;
            case 10:{
                if (day < 31) day++;
                else{
                    day = 1;
                    month++;
                }
                break;
            }
            case 2: {
                if ((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)){
                    if (day < 29) day++;
                    else {
                        day = 1;
                        month++;
                    }
                }
                else {
                    if (day < 28) day++;
                    else {
                        day = 1;
                        month++;
                    }
                }
                break;
            }
            case 12: {
                if (day < 31) day++;
                else {
                    day = 1;
                    month = 1;
                    year++;
                }
                break;
            }
        }
        changeDate(day, month, year);
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day);
        int WeekDay = c.get(Calendar.DAY_OF_WEEK);
        changeDayOfWeek(WeekDay);
    }

    public void changeDate(int day, int month, int year){
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
        edtDay.setText(res);
    }

    public void changeDayOfWeek(int WeekDay){
        switch (WeekDay) {
            case 2:{
                txtDayOfWeek.setText("Понедельник");
                break;
            }
            case 3:{
                txtDayOfWeek.setText("Вторник");
                break;
            }
            case 4:{
                txtDayOfWeek.setText("Среда");
                break;
            }
            case 5:{
                txtDayOfWeek.setText("Четверг");
                break;
            }
            case 6:{
                txtDayOfWeek.setText("Пятница");
                break;
            }
            case 7:{
                txtDayOfWeek.setText("Суббота");
                break;
            }
            case 1:{
                txtDayOfWeek.setText("Воскресенье");
                break;
            }
        }
    }

    int j = 0;

    public void adaptMain(String weekDay) {
        for (j = 0; j < 10; j++) {
            final int x = j;
            reference.child(weekDay).child(String.valueOf(x)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ((TextView) lesson[x].findViewById(R.id.lesson_name)).setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
