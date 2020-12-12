package edu.ucsb.cs.cs184.qhong.woohoo.ui.friend;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.qhong.woohoo.LoginActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.MainViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.FriendGroup;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.MyAdapter;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.User;

public class FriendFragment extends Fragment {

    private MainViewModel mViewModel;

    private ArrayList<FriendGroup> groups;
    private MyAdapter mAdapter;
    private ExpandableListView expandableListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(MainViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_friend, container, false);
        expandableListView = root.findViewById(R.id.expandableView);
//        initList();
        groups = mViewModel.getmFriends().getValue();
        initListener();
        mAdapter = new MyAdapter(groups,getContext());
        expandableListView.setAdapter(mAdapter);
        mViewModel.getmFriends().observe(getViewLifecycleOwner(), new Observer<ArrayList<FriendGroup>>() {
            @Override
            public void onChanged(ArrayList<FriendGroup> friendGroups) {
                groups = friendGroups;
                mAdapter = new MyAdapter(groups,getContext());
                expandableListView.setAdapter(mAdapter);
            }
        });

        final Button addButton = root.findViewById(R.id.searchUser);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                LinearLayout searchBar = root.findViewById(R.id.userSearchBar);
                searchBar.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.GONE);
                SearchView userSearch = root.findViewById(R.id.userSearchText);
                userSearch.setIconified(false);
                userSearch.setSubmitButtonEnabled(true);
                userSearch.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        LinearLayout searchBar = root.findViewById(R.id.userSearchBar);
                        LinearLayout findUser = root.findViewById(R.id.findUser);
                        searchBar.setVisibility(View.GONE);
                        findUser.setVisibility(View.GONE);
                        addButton.setVisibility(View.VISIBLE);
                        return false;
                    }
                });
                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                        return false;
                    }
                });
                userSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(final String s) {

                        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean t = true;
                                for (final DataSnapshot sshot : snapshot.getChildren()) {
                                    String temp = sshot.child("name").getValue(String.class);
                                    if (temp.equals(s)) {
                                        LinearLayout findUser = root.findViewById(R.id.findUser);
                                        TextView findUserName = root.findViewById(R.id.findUserName);
                                        TextView findUserEmail = root.findViewById(R.id.findUserEmail);
                                        Button findUserAdd = root.findViewById(R.id.addFindUser);
                                        findUserAdd.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                User u = new User();
                                                u.setName(s);
                                                u.setEmail(sshot.child("email").getValue().toString());
                                                if(groups.size()==0){
                                                    FriendGroup temp = new FriendGroup();
                                                    temp.setGroupName("Default Group");
                                                    ArrayList<User> list = new ArrayList<>();
                                                    temp.setFriends(list);
                                                    groups.add(temp);
                                                }
                                                groups.get(0).getFriends().add(u);
                                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                                DatabaseReference curUser = myRef.child(currentUser.getUid()).child("friend")
                                                        .child("Default Group").child(sshot.getKey());
                                                curUser.child("name").setValue(s);
                                                curUser.child("email").setValue(sshot.child("email").getValue());
                                                curUser.child("icon").setValue("Default profile photo");

                                            }
                                        });
                                        findUserName.setText(s);
                                        findUserEmail.setText(sshot.child("email").getValue(String.class));
                                        findUser.setVisibility(View.VISIBLE);
                                        Toast.makeText(getContext(), "Find User: " + sshot.getKey(),
                                                Toast.LENGTH_LONG).show();
                                        t = false;
                                        break;
                                    }
                                }
                                if (t) {
                                    Toast.makeText(getContext(), "User not found!",
                                            Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                });
            }});
        return root;
    }
//    public void initList(){
//        for(int i = 0;i < groupNames.length;i++){
//            FriendGroup temp = new FriendGroup();
//            temp.setGroupName(groupNames[i]);
//            ArrayList<User> list = new ArrayList<>();
//            for(int j = 0;j < friendNames[i].length;j++){
//                User u = new User();
//                u.setName(friendNames[i][j]);
//                list.add(u);
//            }
//            temp.setFriends(list);
//            groups.add(temp);
//        }
//    }
    public void initListener(){
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Toast.makeText(getContext(), "老子没写呢！！", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                Toast.makeText(getContext(), "默认好友", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}