package com.example.user.homework;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LessonsListAdapter extends BaseAdapter {

    private ArrayList<Lesson> lessons;


    private String day = "";
    private Context context;
    private LayoutInflater layoutInflater;

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = layoutInflater.inflate(R.layout.table_row, viewGroup, false);
        }
        ((TextView) view.findViewById(R.id.txt_lesson_number)).setText(lessons.get(i).getNumber().toString());
        ((TextView) view.findViewById(R.id.txt_lesson)).setText(lessons.get(i).getLesson());
        ((TextView) view.findViewById(R.id.txt_task)).setText(lessons.get(i).getHomework());
        final int x = i;
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(context, AdminAuthActivity.class);
                intent.putExtra("Lesson", lessons.get(x).getLesson());
                intent.putExtra("Lesson number", lessons.get(x).getNumber());
                intent.putExtra("Day", day);
                intent.putExtra("Next", 3);
                Log.e("DAY_ADAPTER", day);
                context.startActivity(intent);
                return false;
            }
        });
        return view;
    }
}
