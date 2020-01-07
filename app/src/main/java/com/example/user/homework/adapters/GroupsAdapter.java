package com.example.user.homework.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.user.homework.GroupViewActivity;
import com.example.user.homework.R;
import com.example.user.homework.models.GroupModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GroupsAdapter extends BaseAdapter {
    private List<GroupModel> groupModels = new ArrayList<>();

    public GroupsAdapter(Collection<GroupModel> groupModels) {
        if (groupModels != null) {
            this.groupModels = new ArrayList<>(groupModels);
        }
    }

    @Override
    public int getCount() {
        return groupModels.size();
    }

    @Override
    public Object getItem(int position) {
        return groupModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final Context context = parent.getContext();
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.groups_list_item, parent, false);
        }
        ((TextView) view.findViewById(R.id.txt_item_name)).setText(groupModels.get(position).getName());
        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, GroupViewActivity.class);
            intent.putExtra("GROUPID", groupModels.get(position).getId());
            context.startActivity(intent);
        });
        return view;
    }
}
