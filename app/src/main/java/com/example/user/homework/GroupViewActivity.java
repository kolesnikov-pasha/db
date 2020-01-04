package com.example.user.homework;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.homework.adapters.LessonsListAdapter;
import com.example.user.homework.models.LessonModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeSet;

public class GroupViewActivity extends AppCompatActivity{

    private TextView txtDayOfWeek, txtDate;
    private ListView lessonsList;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private String groupId = "";
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference reference;
    private Calendar calendar = Calendar.getInstance();
    private TreeSet<LessonModel> lessons = new TreeSet<>();
    private ArrayList<LessonModel> lessonArrayList = new ArrayList<>();
    private boolean isAdmin = false;
    private final static int DIALOG_DATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle from = getIntent().getExtras();
        assert from != null;
        groupId = from.getString("GROUPID");

        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        lessonsList = findViewById(R.id.list_lessons_main);
        txtDate = findViewById(R.id.date);
        txtDayOfWeek = findViewById(R.id.day_of_week);
        ImageButton btnBurger = findViewById(R.id.btn_burger);
        ImageButton btnCalendar = findViewById(R.id.btn_calendar);
        ImageButton btnNextDay = findViewById(R.id.next_day);
        ImageButton btnPrevDay = findViewById(R.id.prev_day);
        LinearLayout chooseDateLayout = findViewById(R.id.choose_date);

        reference = FirebaseDatabase.getInstance().getReference().child(groupId);
        reference.child("Admin").addChildEventListener(new ChildEventListener() {
            void getData(DataSnapshot dataSnapshot){
                String adminId = dataSnapshot.getValue(String.class);
                if (TextUtils.equals(adminId, uid)) {
                    ((LessonsListAdapter) lessonsList.getAdapter()).setAdmin();
                    isAdmin = true;
                }
            }

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnBurger.setOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));
        btnCalendar.setOnClickListener(v -> showDialog(DIALOG_DATE));
        chooseDateLayout.setOnClickListener(view -> showDialog(DIALOG_DATE));
        btnNextDay.setOnClickListener(v -> {
            nextDay();
            String date = txtDate.getText().toString();
            String dayOfWeek = txtDayOfWeek.getText().toString();
            date = date.substring(0, 2) + date.substring(3, 5) + date.substring(6, 10);
            adaptMain(date, dayOfWeek);
        });
        btnPrevDay.setOnClickListener(v -> {
            prevDay();
            String date = txtDate.getText().toString();
            String dayOfWeek = txtDayOfWeek.getText().toString();
            date = date.substring(0, 2) + date.substring(3, 5) + date.substring(6, 10);
            adaptMain(date, dayOfWeek);
        });
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            final Intent intent;
            switch (menuItem.getItemId()){
                case R.id.menu_add: {
                    intent = new Intent(this, AddNewTaskActivity.class);
                    break;
                }
                case R.id.menu_edit: {
                    intent = new Intent(this, AdminOptionsActivity.class);
                    break;
                }
                case R.id.menu_account_settings: {
                    intent = new Intent(this, GroupsListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(intent);
                    return true;
                }
                default:
                    return false;
            }
            intent.putExtra("GROUPID", groupId);
            if (isAdmin) {
                startActivity(intent);
                return true;
            }
            Toast.makeText(getApplicationContext(), "Вы не администратор группы", Toast.LENGTH_SHORT).show();
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        lessonsList.setAdapter(new LessonsListAdapter(lessonArrayList, getApplicationContext()));

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
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        changeDate(day, month, year);
        changeDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        adaptMain(txtDate.getText().toString().substring(0, 2) + txtDate.getText().toString()
                .substring(3, 5) + txtDate.getText().toString().substring(6, 10), txtDayOfWeek.getText().toString());
    }

    protected Dialog onCreateDialog(int id){
        if (id == DIALOG_DATE) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(GroupViewActivity.this, onDateSetListener, year, month, day);
        }
        else
            return super.onCreateDialog(id);
    }

    final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            changeDate(dayOfMonth, monthOfYear + 1, year);
            calendar.set(year, monthOfYear, dayOfMonth);
            changeDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
            adaptMain(txtDate.getText().toString().substring(0, 2) + txtDate.getText().toString()
                    .substring(3, 5) + txtDate.getText().toString().substring(6, 10), txtDayOfWeek.getText().toString());
        }
    };

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
        changeDate(day, month, year);
        changeDayOfWeek(weekDay);
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
        txtDate.setText(res);
    }

    final static String daysOfWeek[] = new String[]
            {"Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};

    public void changeDayOfWeek(int WeekDay){
        txtDayOfWeek.setText(daysOfWeek[WeekDay - 1]);
    }

    public void adaptMain(String day, String weekDay){
        lessons.clear();
        ((LessonsListAdapter) lessonsList.getAdapter()).setDay(day.substring(0, 2) + "." + day.substring(2, 4) + "." + day.substring(4, 8));
        ((LessonsListAdapter) lessonsList.getAdapter()).getLessons().clear();
        ((LessonsListAdapter) lessonsList.getAdapter()).setGroupId(groupId);
        ((LessonsListAdapter) lessonsList.getAdapter()).notifyDataSetChanged();
        for (int i = 0; i < 10; i++) {
            final LessonModel[] lesson = new LessonModel[1];
            lesson[0] = new LessonModel();
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
