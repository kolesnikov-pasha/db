package com.example.user.homework.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.user.homework.EditHometaskActivity;
import com.example.user.homework.R;
import com.example.user.homework.models.LessonModel;
import java.util.ArrayList;

public class LessonsListAdapter extends BaseAdapter {
    private ArrayList<LessonModel> lessons;
    private String day = "";
    private String groupId = "";
    private Context context;
    private LayoutInflater layoutInflater;
    private boolean isAdmin = false;

    public void setAdmin() {
        isAdmin = true;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public ArrayList<LessonModel> getLessons() {
        return lessons;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LessonsListAdapter(ArrayList<LessonModel> lessons, Context context) {
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
        view.findViewById(R.id.btn_edit).setOnClickListener(v -> {
            if (isAdmin) {
                Intent intent = new Intent(context, EditHometaskActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("LessonModel", lessons.get(x).getLesson());
                intent.putExtra("LessonModel number", lessons.get(x).getNumber());
                intent.putExtra("Day", day);
                intent.putExtra("GROUPID", groupId);
                context.startActivity(intent);
            }
            else {
                Toast.makeText(context, "Вы не администратор", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
