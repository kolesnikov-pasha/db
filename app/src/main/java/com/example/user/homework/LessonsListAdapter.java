package com.example.user.homework;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LessonsListAdapter extends BaseAdapter {

    private ArrayList<Lesson> lessons;


    private String day = "", groupId = "";
    private Context context;
    private LayoutInflater layoutInflater;
    private boolean isAdmin = false;

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setDay(String day) {
        this.day = day;
    }

    LessonsListAdapter(ArrayList<Lesson> lessons, Context context) {
        this.lessons = lessons;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lessons.size();
    }

    @Override
    public Object getItem(int i) {
        return lessons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lessons.get(i).getNumber();
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        if (view == null){
            view = layoutInflater.inflate(R.layout.table_row, viewGroup, false);
        }
        ((TextView) view.findViewById(R.id.txt_lesson_number)).setText(lessons.get(i).getNumber().toString());
        ((TextView) view.findViewById(R.id.txt_lesson)).setText(lessons.get(i).getLesson());
        ((TextView) view.findViewById(R.id.txt_task)).setText(lessons.get(i).getHomework());
        final int x = i;
        view.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdmin) {
                    Intent intent = new Intent(context, EditHometaskActivity.class);
                    intent.putExtra("Lesson", lessons.get(x).getLesson());
                    intent.putExtra("Lesson number", lessons.get(x).getNumber());
                    intent.putExtra("Day", day);
                    intent.putExtra("GROUPID", groupId);
                    Log.e("DAY_ADAPTER", day);
                    context.startActivity(intent);
                }
                else {
                    Toast.makeText(context, "Вы не администратор", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
