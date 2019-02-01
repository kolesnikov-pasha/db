package com.example.user.homework;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AddNewHometask extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = firebaseDatabase.getReference().child(User.getUid()).child("lessonsSchedule");
    DatabaseReference TasksReference = firebaseDatabase.getReference().child(User.getUid()).child("task");

    Button btnAdd;
    TextView txtDayOfWeek;
    TextView edtDay, chosenDay, chosenLesson, GroupName;
    String strLesson = "";
    EditText edtTask;
    Button btnNext;
    String lessonNumber = "-1";
    LinearLayout chooseDate;
    int myYear, myMonth, myDay;
    int DIALOG_DATE = 1;
    View lesson0, lesson1, lesson2, lesson3, lesson4, lesson5, lesson6, lesson7, lesson8, lesson9;
    ImageButton nextDay, prevDay, btnAddAttachments, btnBack, btnHome;

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
        GroupName = (TextView) findViewById(R.id.add_group_name);
        reference.getParent().child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GroupName.setText(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnBack = findViewById(R.id.app_bar_for_add_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.choose_day_lesson).setVisibility(View.VISIBLE);
                findViewById(R.id.add_layout).setVisibility(View.INVISIBLE);
                btnBack.setVisibility(View.INVISIBLE);
            }
        });
        btnHome = findViewById(R.id.add_bar_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
            }
        });
        chosenDay = findViewById(R.id.chosen_date);
        chosenLesson = findViewById(R.id.chosen_lesson_name);
        lesson0 = findViewById(R.id.lesson_choose_0);
        lesson1 = findViewById(R.id.lesson_choose_1);
        lesson2 = findViewById(R.id.lesson_choose_2);
        lesson3 = findViewById(R.id.lesson_choose_3);
        lesson4 = findViewById(R.id.lesson_choose_4);
        lesson5 = findViewById(R.id.lesson_choose_5);
        lesson6 = findViewById(R.id.lesson_choose_6);
        lesson7 = findViewById(R.id.lesson_choose_7);
        lesson8 = findViewById(R.id.lesson_choose_8);
        lesson9 = findViewById(R.id.lesson_choose_9);
        nextDay = findViewById(R.id.add_new_hometask_next_day);
        prevDay = findViewById(R.id.add_new_hometask_prev_day);
        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextday();
                update();
                chosenDay.setText(edtDay.getText());
                unchecked();
                lessonNumber = "-1";
            }
        });
        prevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevday();
                update();
                unchecked();
                chosenDay.setText(edtDay.getText());
                lessonNumber = "-1";
            }
        });
        txtDayOfWeek = findViewById(R.id.add_new_hometask_day_of_week);
        edtDay = findViewById(R.id.add_new_hometask_date);
        btnNext = findViewById(R.id.add_new_hometask_next_view);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.choose_day_lesson).setVisibility(View.INVISIBLE);
                findViewById(R.id.add_layout).setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.VISIBLE);
            }
        });

        btnAdd = (Button) findViewById(R.id.add_btn_add);
        edtTask = (EditText) findViewById(R.id.add_edt_task);
        Calendar calendar = Calendar.getInstance();
        myDay = calendar.get(Calendar.DAY_OF_MONTH);
        myMonth = calendar.get(Calendar.MONTH);
        myYear = calendar.get(Calendar.YEAR);
        changeDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        changeDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        btnAddAttachments = (ImageButton) findViewById(R.id.add_attachments);
        btnAddAttachments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = edtDay.getText().toString();
                day = day.substring(0, 2) + day.substring(3, 5) + day.substring(6, 10);
                String task = edtTask.getText().toString();
                if (lessonNumber.equals("-1")) Toast.makeText(getApplicationContext(), "Предмет не выбран", Toast.LENGTH_SHORT).show();
                else {
                    TasksReference.child(day).child(lessonNumber).setValue(task);
                    Toast.makeText(getApplicationContext(), "Задание добавлено", Toast.LENGTH_SHORT).show();
                    startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
                }
            }
        });
        update();
        ((CheckBox) lesson0.findViewById(R.id.lesson_chosen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) lesson0.findViewById(R.id.lesson_chosen)).isChecked()){
                    unchecked();
                    ((CheckBox) lesson0.findViewById(R.id.lesson_chosen)).setChecked(true);
                    lessonNumber = "0";
                    strLesson = ((TextView)lesson0.findViewById(R.id.lesson_name)).getText().toString();
                    chosenLesson.setText(strLesson);
                }
                else{
                    strLesson = "Предмет не выбран";
                    chosenLesson.setText(strLesson);
                    lessonNumber = "-1";
                }

            }
        });
        ((CheckBox) lesson1.findViewById(R.id.lesson_chosen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) lesson1.findViewById(R.id.lesson_chosen)).isChecked()){
                    unchecked();
                    ((CheckBox) lesson1.findViewById(R.id.lesson_chosen)).setChecked(true);
                    lessonNumber = "1";
                    strLesson = ((TextView)lesson1.findViewById(R.id.lesson_name)).getText().toString();
                    chosenLesson.setText(strLesson);
                }
                else{
                    strLesson = "Предмет не выбран";
                    chosenLesson.setText(strLesson);
                    lessonNumber = "-1";
                }

            }
        });
        ((CheckBox) lesson2.findViewById(R.id.lesson_chosen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) lesson2.findViewById(R.id.lesson_chosen)).isChecked()){
                    unchecked();
                    ((CheckBox) lesson2.findViewById(R.id.lesson_chosen)).setChecked(true);
                    lessonNumber = "2";
                    strLesson = ((TextView)lesson2.findViewById(R.id.lesson_name)).getText().toString();
                    chosenLesson.setText(strLesson);
                }
                else{
                    strLesson = "Предмет не выбран";
                    chosenLesson.setText(strLesson);
                    lessonNumber = "-1";
                }

            }
        });
        ((CheckBox) lesson3.findViewById(R.id.lesson_chosen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) lesson3.findViewById(R.id.lesson_chosen)).isChecked()){
                    unchecked();
                    ((CheckBox) lesson3.findViewById(R.id.lesson_chosen)).setChecked(true);
                    lessonNumber = "3";
                    strLesson = ((TextView)lesson3.findViewById(R.id.lesson_name)).getText().toString();
                    chosenLesson.setText(strLesson);
                }
                else{
                    strLesson = "Предмет не выбран";
                    chosenLesson.setText(strLesson);
                    lessonNumber = "-1";
                }

            }
        });
        ((CheckBox) lesson4.findViewById(R.id.lesson_chosen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) lesson4.findViewById(R.id.lesson_chosen)).isChecked()){
                    unchecked();
                    ((CheckBox) lesson4.findViewById(R.id.lesson_chosen)).setChecked(true);
                    lessonNumber = "4";
                    strLesson = ((TextView)lesson4.findViewById(R.id.lesson_name)).getText().toString();
                    chosenLesson.setText(strLesson);
                }
                else{
                    strLesson = "Предмет не выбран";
                    chosenLesson.setText(strLesson);
                    lessonNumber = "-1";
                }

            }
        });
        ((CheckBox) lesson5.findViewById(R.id.lesson_chosen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) lesson5.findViewById(R.id.lesson_chosen)).isChecked()){
                    unchecked();
                    ((CheckBox) lesson5.findViewById(R.id.lesson_chosen)).setChecked(true);
                    lessonNumber = "5";
                    strLesson = ((TextView)lesson5.findViewById(R.id.lesson_name)).getText().toString();
                    chosenLesson.setText(strLesson);
                }
                else{
                    strLesson = "Предмет не выбран";
                    chosenLesson.setText(strLesson);
                    lessonNumber = "-1";
                }

            }
        });
        ((CheckBox) lesson6.findViewById(R.id.lesson_chosen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) lesson6.findViewById(R.id.lesson_chosen)).isChecked()){
                    unchecked();
                    ((CheckBox) lesson6.findViewById(R.id.lesson_chosen)).setChecked(true);
                    lessonNumber = "6";
                    strLesson = ((TextView)lesson6.findViewById(R.id.lesson_name)).getText().toString();
                    chosenLesson.setText(strLesson);
                }
                else{
                    strLesson = "Предмет не выбран";
                    chosenLesson.setText(strLesson);
                    lessonNumber = "-1";
                }

            }
        });
        ((CheckBox) lesson7.findViewById(R.id.lesson_chosen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) lesson7.findViewById(R.id.lesson_chosen)).isChecked()) {
                    unchecked();
                    ((CheckBox) lesson7.findViewById(R.id.lesson_chosen)).setChecked(true);
                    lessonNumber = "7";
                    strLesson = ((TextView)lesson7.findViewById(R.id.lesson_name)).getText().toString();
                    chosenLesson.setText(strLesson);
                }
                else{
                    strLesson = "Предмет не выбран";
                    chosenLesson.setText(strLesson);
                    lessonNumber = "-1";
                }

            }
        });
        ((CheckBox) lesson8.findViewById(R.id.lesson_chosen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) lesson8.findViewById(R.id.lesson_chosen)).isChecked()){
                    unchecked();
                    ((CheckBox) lesson8.findViewById(R.id.lesson_chosen)).setChecked(true);
                    lessonNumber = "8";
                    strLesson = ((TextView)lesson8.findViewById(R.id.lesson_name)).getText().toString();
                    chosenLesson.setText(strLesson);
                }
                else {
                    strLesson = "Предмет не выбран";
                    chosenLesson.setText(strLesson);
                    lessonNumber = "-1";
                }

            }
        });
        ((CheckBox) lesson9.findViewById(R.id.lesson_chosen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) lesson9.findViewById(R.id.lesson_chosen)).isChecked()){
                    unchecked();
                    ((CheckBox) lesson9.findViewById(R.id.lesson_chosen)).setChecked(true);
                    lessonNumber = "9";
                    strLesson = ((TextView)lesson9.findViewById(R.id.lesson_name)).getText().toString();
                    chosenLesson.setText(strLesson);
                }
                else {
                    strLesson = "Предмет не выбран";
                    chosenLesson.setText(strLesson);
                    lessonNumber = "-1";
                }
            }
        });
        chosenDay.setText(edtDay.getText());
    }

    protected Dialog onCreateDialog(int id){
        if (id == DIALOG_DATE) {
            return new DatePickerDialog(AddNewHometask.this, myCallBack, myYear, myMonth, myDay);
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
        ((CheckBox)lesson0.findViewById(R.id.lesson_chosen)).setChecked(false);
        ((CheckBox)lesson1.findViewById(R.id.lesson_chosen)).setChecked(false);
        ((CheckBox)lesson2.findViewById(R.id.lesson_chosen)).setChecked(false);
        ((CheckBox)lesson3.findViewById(R.id.lesson_chosen)).setChecked(false);
        ((CheckBox)lesson4.findViewById(R.id.lesson_chosen)).setChecked(false);
        ((CheckBox)lesson5.findViewById(R.id.lesson_chosen)).setChecked(false);
        ((CheckBox)lesson6.findViewById(R.id.lesson_chosen)).setChecked(false);
        ((CheckBox)lesson7.findViewById(R.id.lesson_chosen)).setChecked(false);
        ((CheckBox)lesson8.findViewById(R.id.lesson_chosen)).setChecked(false);
        ((CheckBox)lesson9.findViewById(R.id.lesson_chosen)).setChecked(false);
    }

    private void update(){
        ((TextView)lesson0.findViewById(R.id.lesson_number)).setText("0.");
        ((TextView)lesson1.findViewById(R.id.lesson_number)).setText("1.");
        ((TextView)lesson2.findViewById(R.id.lesson_number)).setText("2.");
        ((TextView)lesson3.findViewById(R.id.lesson_number)).setText("3.");
        ((TextView)lesson4.findViewById(R.id.lesson_number)).setText("4.");
        ((TextView)lesson5.findViewById(R.id.lesson_number)).setText("5.");
        ((TextView)lesson6.findViewById(R.id.lesson_number)).setText("6.");
        ((TextView)lesson7.findViewById(R.id.lesson_number)).setText("7.");
        ((TextView)lesson8.findViewById(R.id.lesson_number)).setText("8.");
        ((TextView)lesson9.findViewById(R.id.lesson_number)).setText("9.");
        ((TextView)lesson0.findViewById(R.id.lesson_name)).setText("");
        ((TextView)lesson1.findViewById(R.id.lesson_name)).setText("");
        ((TextView)lesson2.findViewById(R.id.lesson_name)).setText("");
        ((TextView)lesson3.findViewById(R.id.lesson_name)).setText("");
        ((TextView)lesson4.findViewById(R.id.lesson_name)).setText("");
        ((TextView)lesson5.findViewById(R.id.lesson_name)).setText("");
        ((TextView)lesson6.findViewById(R.id.lesson_name)).setText("");
        ((TextView)lesson7.findViewById(R.id.lesson_name)).setText("");
        ((TextView)lesson8.findViewById(R.id.lesson_name)).setText("");
        ((TextView)lesson9.findViewById(R.id.lesson_name)).setText("");
        String date = edtDay.getText().toString();
        String day = (date.substring(0, 2));
        String month = (date.substring(3, 5));
        String year = (date.substring(6, 10));
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

    public void adaptMain(String weekDay) {
        reference.child(weekDay).child("0").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView)lesson0.findViewById(R.id.lesson_name)).setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child(weekDay).child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView)lesson1.findViewById(R.id.lesson_name)).setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child(weekDay).child("2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView)lesson2.findViewById(R.id.lesson_name)).setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child(weekDay).child("3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView)lesson3.findViewById(R.id.lesson_name)).setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child(weekDay).child("4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView)lesson4.findViewById(R.id.lesson_name)).setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child(weekDay).child("5").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView)lesson5.findViewById(R.id.lesson_name)).setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child(weekDay).child("6").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView)lesson6.findViewById(R.id.lesson_name)).setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child(weekDay).child("7").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView)lesson7.findViewById(R.id.lesson_name)).setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child(weekDay).child("8").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView)lesson8.findViewById(R.id.lesson_name)).setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child(weekDay).child("9").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView)lesson9.findViewById(R.id.lesson_name)).setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
