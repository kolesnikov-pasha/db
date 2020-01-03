package com.example.user.homework;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

class Group implements Comparable<Group>{

    private String name = "", id = "", password = "";
    private ArrayList<String> admin = new ArrayList<>();

    public ArrayList<String> getAdmin() {
        return admin;
    }

    public void setAdmin(ArrayList<String> admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Group() {}

    public Group(String name, String id, String password, ArrayList<String> admin) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.admin = admin;
    }

    @Override
    public int compareTo(@NonNull Group o) {
        return id.compareTo(o.id);
    }
}

class GroupsAdapter extends BaseAdapter {
    private List<Group> groups = new ArrayList<>();

    GroupsAdapter(Collection<Group> groups) {
        if (groups != null) {
            this.groups = new ArrayList<>(groups);
        }
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
    public View getView(final int position, View view, ViewGroup parent) {
        final Context context = parent.getContext();
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.groups_list_item, parent, false);
        }
        ((TextView) view.findViewById(R.id.txt_item_name)).setText(groups.get(position).getName());
        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, GroupViewActivity.class);
            intent.putExtra("GROUPID", groups.get(position).getId());
            context.startActivity(intent);
        });
        return view;
    }
}

public class GroupsListActivity extends AppCompatActivity {
    ListView listView;
    ImageButton btnBurger;
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    TextView txtName;
    Set<Group> groups = new TreeSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_list);
        listView = findViewById(R.id.groups_list);
        btnBurger = findViewById(R.id.btn_burger_groups);
        mDrawerLayout = findViewById(R.id.groups_list_layout);
        navigationView = findViewById(R.id.groups_menu);
        txtName = navigationView.getHeaderView(0).findViewById(R.id.txt_name);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.nav_find_groups:{
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    break;
                }
                case R.id.nav_my_groups:{
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
                case R.id.nav_settings:{
                    startActivity(new Intent(getApplicationContext(), AccountSettingsActivity.class));
                    break;
                }
                case R.id.nav_add_group:{
                    startActivity(new Intent(getApplicationContext(), CreateGroupActivity.class));
                    break;
                }
            }
            return false;
        });
        btnBurger.setOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));
        ((HomeworkApplication) getApplication()).addUserListener(user -> {
            if (txtName != null) {
                txtName.setText(user.getName() + " " + user.getSurname());
            }
            Log.i("KEK", user.toString());
            List<String> groupsArray = user.getGroups();
            if (groupsArray == null) {
                return;
            }
            for (String uid : groupsArray) {
                DatabaseReference groupReference = FirebaseDatabase.getInstance().getReference().child(uid);
                groupReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        groups.add(dataSnapshot.getValue(Group.class));
                        listView.setAdapter(new GroupsAdapter(groups));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });
    }
}
