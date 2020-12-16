package edu.ucsb.cs.cs184.qhong.woohoo.ui.friend;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.qhong.woohoo.LoginActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.MainActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.MainViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.FriendGroup;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.MyAdapter;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.User;
import q.rorbin.badgeview.QBadgeView;

public class FriendFragment extends Fragment {

    private MainViewModel mViewModel;

    private ArrayList<FriendGroup> groups;
    private MyAdapter mAdapter;
    private ExpandableListView expandableListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(getActivity()).get(MainViewModel.class);

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

        Button friendRequests = root.findViewById(R.id.friendRequests);
        new QBadgeView(getContext()).bindTarget(friendRequests).setBadgeNumber(mViewModel.getFriendsPendingList().getValue().size());
        friendRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("")
                        .setMessage("Do you want to accept all friend requests?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ArrayList<User> lists = mViewModel.getFriendsPendingList().getValue();
                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                for(int j=0;j<lists.size();j++){
                                    //set currentUser
                                    DatabaseReference curUser = myRef.child(currentUser.getUid()).child("friend")
                                            .child("Default Group").child(lists.get(j).getUid());
                                    curUser.child("name").setValue(lists.get(j).getName());
                                    curUser.child("email").setValue(lists.get(j).getEmail());
                                    curUser.child("icon").setValue(lists.get(j).getIcon());
                                    //set targetUser
                                    DatabaseReference targetUser = myRef.child(lists.get(j).getUid()).child("friend")
                                            .child("Default Group").child(currentUser.getUid());
                                    User tem = mViewModel.getUser().getValue();
                                    targetUser.child("name").setValue(tem.getName());
                                    targetUser.child("email").setValue(tem.getEmail());
                                    targetUser.child("icon").setValue(tem.getIcon());

                                }
                                myRef.child(currentUser.getUid()).child("pending friends").removeValue();
                                mViewModel.setFriendsPendingList(new ArrayList<User>(0));
                                refresh();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                alertDialog.show();
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

                                                NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                                                TextView nickname = navigationView.getHeaderView(0).findViewById(R.id.nickname);
                                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                                DatabaseReference te = myRef.child(sshot.getKey()).child("pending friends")
                                                        .child(currentUser.getUid());
                                                te.child("name").setValue(nickname.getText().toString());
                                                te.child("email").setValue(currentUser.getEmail());
                                                te.child("icon").setValue("Default profile photo");
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
    public void refresh(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(FriendFragment.this).attach(FriendFragment.this).commit();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mViewModel.updateFriendsList(currentUser);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public void initListener(){
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, final int ii, final int i1, long l) {

                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("")
                        .setMessage("Do you want to delete this user?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String uid = groups.get(ii).getFriends().get(i1).getUid();
                                groups.get(ii).getFriends().remove(i1);
                                mViewModel.deleteFriend(uid);
                                refresh();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                alertDialog.show();

                return false;
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return false;
            }
        });
    }
}