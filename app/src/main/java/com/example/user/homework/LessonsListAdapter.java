package com.example.user.homework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by olpyh on 18.02.2018.
 */

public class LessonsListAdapter extends BaseAdapter {
    String[] lessons = new String[10];
    Context context;
    LayoutInflater layoutInflater;

    LessonsListAdapter (String[] lessons, Context context){
        this.context = context;
        this.lessons = lessons;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return lessons[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view != null) {
            layoutInflater.inflate(R.layout.choose_lesson_item, viewGroup, false);
        }
        ((TextView) view.findViewById(R.id.lesson_number)).setText(i);
        if (!lessons[i].isEmpty()) ((TextView) view.findViewById(R.id.lesson_name)).setText(lessons[i]);
        else {
            ((TextView) view.findViewById(R.id.lesson_name)).setText("Нет урока");
            view.findViewById(R.id.lesson_chosen).setClickable(false);
            view.findViewById(R.id.lesson_chosen).setVisibility(View.INVISIBLE);
        }
        return view;
    }
}
