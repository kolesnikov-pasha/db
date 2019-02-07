package com.example.user.homework;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeSet;

class Lesson implements Comparable<Lesson>{
    private Integer number;
    private String lesson, homework;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    String getLesson() {
        return lesson;
    }

    void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    @Override
    public int compareTo(@NonNull Lesson lesson) {
        return number - lesson.number;
    }
}

public class MainActivity extends AppCompatActivity{


    ImageButton nextDay, prevDay;
    TextView DayOfWeek, Date, GroupName;
    LinearLayout chooseDate;
    int myYear, myMonth, myDay;
    int DIALOG_DATE = 1;
    ListView lessonsList;


    DatabaseReference reference;
    TreeSet<Lesson> lessons = new TreeSet<>();
    ArrayList<Lesson> lessonArrayList = new ArrayList<>();
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent = new Intent(getApplicationContext(), AdminAuth.class);
                switch (menuItem.getItemId()){
                    case R.id.menu_add: {
                        intent.putExtra("Next", 1);
                        startActivityForResult(intent, 0);
                        break;
                    }
                    case R.id.menu_edit: {
                        intent.putExtra("Next", 2);
                        startActivityForResult(intent, 0);
                        break;
                    }
                    case R.id.menu_account_settings: {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                        break;
                    }
                }
                return false;
            }
        });
        lessonsList = findViewById(R.id.list_lessons_main);
        GroupName = findViewById(R.id.txt_name);
        Date = findViewById(R.id.main_date);
        DayOfWeek = findViewById(R.id.main_day_of_week);

        nextDay = findViewById(R.id.main_next_day);
        prevDay = findViewById(R.id.main_prev_day);
        chooseDate = findViewById(R.id.main_choose_date);

        lessonsList.setAdapter(new LessonsListAdapter(lessonArrayList, getApplicationContext()));

        reference = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_DATE);
            }
        });

        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextDay();
                String date = Date.getText().toString();
                String dayOfWeek = DayOfWeek.getText().toString();
                date = date.substring(0, 2) + date.substring(3, 5) + date.substring(6, 10);
                myDay = Integer.valueOf(date.substring(0, 2));
                myMonth = Integer.valueOf(date.substring(2, 4));
                myYear = Integer.valueOf(date.substring(4, 8));
                adaptMain(date, dayOfWeek);
            }
        });
        prevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevDay();
                String date = Date.getText().toString();
                String dayOfWeek = DayOfWeek.getText().toString();
                date = date.substring(0, 2) + date.substring(3, 5) + date.substring(6, 10);
                myDay = Integer.valueOf(date.substring(0, 2));
                myMonth = Integer.valueOf(date.substring(2, 4));
                myYear = Integer.valueOf(date.substring(4, 8));
                adaptMain(date, dayOfWeek);
            }
        });

        reference.child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView)navigationView.getHeaderView(0).findViewById(R.id.txt_name)).setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        myDay = calendar.get(Calendar.DAY_OF_MONTH);
        myMonth = calendar.get(Calendar.MONTH);
        myYear = calendar.get(Calendar.YEAR);
        changeDate(myDay, myMonth + 1, myYear);
        changeDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));

        adaptMain(Date.getText().toString().substring(0, 2) + Date.getText().toString()
                .substring(3, 5) + Date.getText().toString().substring(6, 10), DayOfWeek.getText().toString());
    }

    protected Dialog onCreateDialog(int id){
        if (id == DIALOG_DATE) {
            return new DatePickerDialog(MainActivity.this, myCallBack, myYear, myMonth, myDay);
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
            adaptMain(Date.getText().toString().substring(0, 2) + Date.getText().toString()
                    .substring(3, 5) + Date.getText().toString().substring(6, 10), DayOfWeek.getText().toString());
        }
    };

    private void nextDay(){
        String date = Date.getText().toString();
        Integer day = Integer.valueOf(date.substring(0, 2));
        Integer month = Integer.valueOf(date.substring(3, 5));
        Integer year = Integer.valueOf(date.substring(6, 10));
        switch (month){
            case 4:
            case 6:
            case 9:
            case 11:{
                if (day < 30) day++;
                else{
                    day = 1;
                    month++;
                }
                break;
            }
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
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
        Date.setText(res);
    }

    public void changeDayOfWeek(int WeekDay){
        switch (WeekDay) {
            case 2:{
                DayOfWeek.setText("Понедельник");
                break;
            }
            case 3:{
                DayOfWeek.setText("Вторник");
                break;
            }
            case 4:{
                DayOfWeek.setText("Среда");
                break;
            }
            case 5:{
                DayOfWeek.setText("Четверг");
                break;
            }
            case 6:{
                DayOfWeek.setText("Пятница");
                break;
            }
            case 7:{
                DayOfWeek.setText("Суббота");
                break;
            }
            case 1:{
                DayOfWeek.setText("Воскресенье");
                break;
            }
        }
    }

    private void prevDay(){
        String date = Date.getText().toString();
        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year = Integer.parseInt(date.substring(6, 10));
        switch (month){
            case 5:
            case 7:
            case 10:
            case 12:{
                if (day > 1) day--;
                else{
                    day = 30;
                    month--;
                }
                break;
            }
            case 2:
            case 4:
            case 6:
            case 8:
            case 9:
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

    public void adaptMain(String day, String weekDay){
        lessons.clear();
        ((LessonsListAdapter) lessonsList.getAdapter()).setDay(day.substring(0, 2) + "." + day.substring(2, 4) + "." + day.substring(4, 8));
        ((LessonsListAdapter) lessonsList.getAdapter()).getLessons().clear();
        ((LessonsListAdapter) lessonsList.getAdapter()).notifyDataSetChanged();
        for (int i = 0; i < 10; i++) {
            final Lesson[] lesson = new Lesson[1];
            lesson[0] = new Lesson();
            lesson[0].setNumber(i);
            reference.child("lessonsSchedule").child(weekDay).child(i + "").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    lesson[0].setLesson(dataSnapshot.getValue(String.class));
                    if (lesson[0].getLesson() != null && !lesson[0].getLesson().isEmpty()) {
                        lessons.add(lesson[0]);
                        ((LessonsListAdapter) lessonsList.getAdapter()).getLessons().clear();
                        ((LessonsListAdapter) lessonsList.getAdapter()).getLessons().addAll(lessons);
                        ((LessonsListAdapter) lessonsList.getAdapter()).notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            reference.child("task").child(day).child(i + "").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    lesson[0].setHomework(dataSnapshot.getValue(String.class));
                    if (lesson[0].getLesson() != null && !lesson[0].getLesson().isEmpty()) {
                        lessons.add(lesson[0]);
                        ((LessonsListAdapter) lessonsList.getAdapter()).getLessons().clear();
                        ((LessonsListAdapter) lessonsList.getAdapter()).getLessons().addAll(lessons);
                        ((LessonsListAdapter) lessonsList.getAdapter()).notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
