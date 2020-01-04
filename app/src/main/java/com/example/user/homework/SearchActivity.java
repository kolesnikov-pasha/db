package com.example.user.homework;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import com.example.user.homework.adapters.SearchAdapter;
import com.example.user.homework.models.SearchGroupModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SearchActivity extends AppCompatActivity {
    private static class SearchEngine{
        static class PairForSort implements Comparable<PairForSort>{
            private String s;
            private String uid;
            private Double v;
            @Override
            public int compareTo(@NonNull PairForSort o) {
                return v.compareTo(o.v);
            }

            public PairForSort(String s, String uid, Double v) {
                this.s = s;
                this.uid = uid;
                this.v = v;
            }

            public String getS() {
                return s;
            }

            public String getUid() {
                return uid;
            }
        }

        private static Map<SearchGroupModel, Integer> res = new TreeMap<>();

        static int levensteinDelta(String s, String t){
            SearchGroupModel searchGroupModel = new SearchGroupModel(s, t);
            if (res.containsKey(searchGroupModel)){
                return res.get(searchGroupModel);
            }
            if (s.equals(t)){
                return 0;
            }
            if (s.length() == 0 || t.length() == 0){
                return s.length() + t.length();
            }
            int result =  Math.min(Math.min(levensteinDelta(s.substring(0, s.length()-1), t) + 1, levensteinDelta(s, t.substring(0, t.length()-1)) + 1),
                levensteinDelta(s.substring(0, s.length()-1), t.substring(0, t.length()-1)) + (s.charAt(s.length() - 1) == t.charAt(t.length() - 1) ? 0 : 1));
            res.put(searchGroupModel, result);
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

    ListView listView;
    EditText edtSearch;
    ImageButton button;
    ArrayList<SearchGroupModel> list = new ArrayList<>();
    ArrayList<SearchGroupModel> adapterList = new ArrayList<>();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("groups");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        button = findViewById(R.id.btn_search);
        listView = findViewById(R.id.groups_search_list);
        edtSearch = findViewById(R.id.edt_search);
        listView.setAdapter(new SearchAdapter(list, getApplicationContext()));
        ((HomeworkApplication) getApplication()).addGroupListener(groups -> {
            for (final String uid: groups) {
                if (uid == null) {
                    continue;
                }
                System.out.println(uid);
                DatabaseReference toGroup = FirebaseDatabase.getInstance().getReference();
                toGroup.child(uid).child("Name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String name = dataSnapshot.getValue(String.class);
                        assert name != null;
                        SearchGroupModel group = new SearchGroupModel(name, uid);
                        list.add(group);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });

        button.setOnClickListener(v -> {
            if (String.valueOf(edtSearch.getText()).isEmpty()) {
                listView.setAdapter(new SearchAdapter(list, SearchActivity.this.getApplicationContext()));
                return;
            }
            final String search = edtSearch.getText().toString();
            final List<SearchEngine.PairForSort> sort = new ArrayList<>();
            adapterList.clear();
            for (int i = 0; i < list.size(); i++) {
                int value = SearchEngine.levensteinDelta(list.get(i).getName(), search);
                if (value <= 2) {
                    sort.add(new SearchEngine.PairForSort(list.get(i).getName(), list.get(i).getUid(),
                        (double) value));
                } else if (SearchEngine.maxCommonSubstring(list.get(i).getName(), search) == search.length()) {
                    sort.add(new SearchEngine.PairForSort(list.get(i).getName(), list.get(i).getUid(),
                        (double) value));
                }
            }
            final SearchEngine.PairForSort[] sorted = sort.toArray(new SearchEngine.PairForSort[]{});
            if (sorted == null) {
                return;
            }
            Arrays.sort(sorted);
            for (SearchEngine.PairForSort pair : sorted) {
                adapterList.add(new SearchGroupModel(pair.getS(), pair.getUid()));
            }
            listView.setAdapter(new SearchAdapter(adapterList, SearchActivity.this.getApplicationContext()));
        });

    }
}
