package com.example.user.homework.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.user.homework.GroupPasswordActivity;
import com.example.user.homework.R;
import com.example.user.homework.models.SearchGroupModel;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {

    private ArrayList<SearchGroupModel> groups;
    private Context context;
    private LayoutInflater inflater;

    public SearchAdapter(ArrayList<SearchGroupModel> groups, Context context) {
        this.groups = groups;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null){
            view = inflater.inflate(R.layout.group_view, parent, false);
        }
        if (view != null) {
            ((TextView) view.findViewById(R.id.txt_group_view_uid)).setText(groups.get(position).getUid());
            ((TextView) view.findViewById(R.id.txt_group_view_name)).setText(groups.get(position).getName());
        }
        assert view != null;
        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, GroupPasswordActivity.class);
            intent.putExtra("NAME", groups.get(position).getName());
            intent.putExtra("UID", groups.get(position).getUid());
            context.startActivity(intent);
        });
        return view;
    }
}
