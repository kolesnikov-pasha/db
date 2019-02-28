package com.example.user.homework;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import java.util.Map;
import java.util.TreeMap;

class GroupView{
    String name, uid;

    public GroupView() {
    }

    public GroupView(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "GroupView{" +
                "name='" + name + '\'' +
                ", id='" + uid + '\'' +
                '}';
    }
}

class SearchEngine{

    private static class Pair implements Comparable<Pair>{
        String s, t;

        Pair(String s, String t) {
            this.s = s;
            this.t = t;
        }


        @Override
        public int compareTo(Pair o) {
            if (s.equals(o.s)) {
                return t.compareTo(o.t);
            }
            else {
                return s.compareTo(o.s);
            }
        }
    }

    static class PairForSort implements Comparable<PairForSort>{

        String s;
        String uid;
        Double v;

        @Override
        public int compareTo(PairForSort o) {
            return v.compareTo(o.v);
        }

        PairForSort(String s, String uid, Double v) {
            this.s = s;
            this.uid = uid;
            this.v = v;
        }
    }

    private static Map<Pair, Integer> res = new TreeMap<>();

    static int levensteinDelta(String s, String t){
        if (res.containsKey(new Pair(s, t))){
            return res.get(new Pair(s, t));
        }
        if (s.equals(t)){
            return 0;
        }
        if (s.length() == 0 || t.length() == 0){
            return s.length() + t.length();
        }
        int result =  Math.min(Math.min(levensteinDelta(s.substring(0, s.length()-1), t) + 1, levensteinDelta(s, t.substring(0, t.length()-1)) + 1),
                levensteinDelta(s.substring(0, s.length()-1), t.substring(0, t.length()-1)) + (s.charAt(s.length() - 1) == t.charAt(t.length() - 1) ? 0 : 1));
        res.put(new Pair(s, t), result);
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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupPasswordActivity.class);
                intent.putExtra("NAME", groups.get(position).name);
                intent.putExtra("UID", groups.get(position).uid);
                context.startActivity(intent);
            }
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


        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
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
                    sort.sort(SearchEngine.PairForSort::compareTo);
                    for (SearchEngine.PairForSort pair : sort) {
                        adapterList.add(new GroupView(pair.s, pair.uid));
                    }
                    listView.setAdapter(new SearchAdapter(adapterList, SearchActivity.this.getApplicationContext()));
                } else {
                    listView.setAdapter(new SearchAdapter(list, SearchActivity.this.getApplicationContext()));
                }
            }
        });

    }
}
