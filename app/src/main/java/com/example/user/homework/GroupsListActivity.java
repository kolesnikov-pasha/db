package com.example.user.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.example.user.homework.adapters.GroupsAdapter;
import com.example.user.homework.models.GroupModel;
import com.example.user.homework.utils.UiUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GroupsListActivity extends AppCompatActivity {
    ListView listView;
    ImageButton btnBurger;
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    TextView txtName;
    Set<GroupModel> groupModels = new TreeSet<>();

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
            List<String> groupsArray = user.getGroups();
            if (groupsArray == null) {
                return;
            }
            for (String uid : groupsArray) {
                DatabaseReference groupReference = FirebaseDatabase.getInstance().getReference().child(uid);
                groupReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        groupModels.add(dataSnapshot.getValue(GroupModel.class));
                        listView.setAdapter(new GroupsAdapter(groupModels));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        UiUtils.say(getApplicationContext(), R.string.check_internet_connection);
                    }
                });
            }
        });
    }
}
