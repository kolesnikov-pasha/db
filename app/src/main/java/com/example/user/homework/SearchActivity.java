package com.example.user.homework;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

class GroupView implements Comparable<GroupView>{

    String name, uid;

    public GroupView() {
    }

    GroupView(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "GroupView{" +
                "name='" + name + '\'' +
                ", id='" + uid + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull GroupView o) {
        if (name.equals(o.name)) {
            return uid.compareTo(o.uid);
        }
        else {
            return name.compareTo(o.name);
        }
    }
}

class SearchEngine{

    static class PairForSort implements Comparable<PairForSort>{

        String s;
        String uid;
        Double v;

        @Override
        public int compareTo(@NonNull PairForSort o) {
            return v.compareTo(o.v);
        }

        PairForSort(String s, String uid, Double v) {
            this.s = s;
            this.uid = uid;
            this.v = v;
        }
    }

    private static Map<GroupView, Integer> res = new TreeMap<>();

    static int levensteinDelta(String s, String t){
        GroupView groupView = new GroupView(s, t);
        if (res.containsKey(groupView)){
            return res.get(groupView);
        }
        if (s.equals(t)){
            return 0;
        }
        if (s.length() == 0 || t.length() == 0){
            return s.length() + t.length();
        }
        int result =  Math.min(Math.min(levensteinDelta(s.substring(0, s.length()-1), t) + 1, levensteinDelta(s, t.substring(0, t.length()-1)) + 1),
                levensteinDelta(s.substring(0, s.length()-1), t.substring(0, t.length()-1)) + (s.charAt(s.length() - 1) == t.charAt(t.length() - 1) ? 0 : 1));
        res.put(groupView, result);
        return result;
    }

    static int maxCommonSubstring(String s, String t) {
        int[][] dp = new int[s.length() + 1][t.length() + 1];
        for (int i = 0; i <= s.length(); i++) {
            for (int j = 0; j <= t.length(); j++) {
                if (i == 0 || j == 0) dp[i][j] = 0;
                else {
                    if (s.charAt(i - 1) == t.charAt(j - 1)) {
                        dp[i][j] = 1 + dp[i - 1][j - 1];
                    } else {
                        dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                    }
                }
            }
        }
        return dp[s.length()][t.length()];
    }

}

class SearchAdapter extends BaseAdapter {

    private ArrayList<GroupView> groups;
    private Context context;
    private LayoutInflater inflater;

    SearchAdapter(ArrayList<GroupView> groups, Context context) {
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
            ((TextView) view.findViewById(R.id.txt_group_view_uid)).setText(groups.get(position).uid);
            ((TextView) view.findViewById(R.id.txt_group_view_name)).setText(groups.get(position).name);
        }
        assert view != null;
        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, GroupPasswordActivity.class);
            intent.putExtra("NAME", groups.get(position).name);
            intent.putExtra("UID", groups.get(position).uid);
            context.startActivity(intent);
        });
        return view;
    }
}

public class SearchActivity extends AppCompatActivity {

    ListView listView;
    EditText edtSearch;
    ImageButton button;
    ArrayList<GroupView> list = new ArrayList<>();
    ArrayList<GroupView> adapterList = new ArrayList<>();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("groups");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        button = findViewById(R.id.btn_search);
        listView = findViewById(R.id.groups_search_list);
        edtSearch = findViewById(R.id.edt_search);
        listView.setAdapter(new SearchAdapter(list, getApplicationContext()));
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String uid = dataSnapshot.getValue(String.class);
                final String[] name = {""};
                assert uid != null;
                System.out.println(uid);
                DatabaseReference toGroup = FirebaseDatabase.getInstance().getReference();
                toGroup.child(uid).child("Name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        name[0] = dataSnapshot.getValue(String.class);
                        assert name[0] != null;
                        GroupView group = new GroupView(name[0], uid);
                        list.add(group);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        name[0] = dataSnapshot.getValue(String.class);
                        assert name[0] != null;
                        GroupView group = new GroupView(name[0], uid);
                        list.add(group);
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String uid = dataSnapshot.getValue(String.class);
                final String[] name = {""};
                assert uid != null;
                System.out.println(uid);
                DatabaseReference toGroup = FirebaseDatabase.getInstance().getReference();
                toGroup.child(uid).child("Name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        name[0] = dataSnapshot.getValue(String.class);
                        assert name[0] != null;
                        GroupView group = new GroupView(name[0], uid);
                        list.add(group);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        name[0] = dataSnapshot.getValue(String.class);
                        assert name[0] != null;
                        GroupView group = new GroupView(name[0], uid);
                        list.add(group);
                    }
                });
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


        button.setOnClickListener(v -> {
            if (!String.valueOf(edtSearch.getText()).isEmpty()) {
                String search = String.valueOf(edtSearch.getText());
                ArrayList<SearchEngine.PairForSort> sort = new ArrayList<>();
                adapterList.clear();
                for (int i = 0; i < list.size(); i++) {
                    int value = SearchEngine.levensteinDelta(list.get(i).name, search);
                    if (value <= 2) {
                        sort.add(new SearchEngine.PairForSort(list.get(i).name, list.get(i).uid,
                                (double) value));
                    } else if (SearchEngine.maxCommonSubstring(list.get(i).name, search) == search.length()) {
                        sort.add(new SearchEngine.PairForSort(list.get(i).name, list.get(i).uid,
                                (double) value));
                    }
                }
                SearchEngine.PairForSort sorted[] = (SearchEngine.PairForSort[]) sort.toArray();
                assert sorted != null;
                Arrays.sort(sorted);
                for (SearchEngine.PairForSort pair : sorted) {
                    adapterList.add(new GroupView(pair.s, pair.uid));
                }
                listView.setAdapter(new SearchAdapter(adapterList, SearchActivity.this.getApplicationContext()));
            } else {
                listView.setAdapter(new SearchAdapter(list, SearchActivity.this.getApplicationContext()));
            }
        });

    }
}
