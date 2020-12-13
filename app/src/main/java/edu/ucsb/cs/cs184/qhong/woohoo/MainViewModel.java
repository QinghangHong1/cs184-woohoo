package edu.ucsb.cs.cs184.qhong.woohoo;

import android.renderscript.Sampler;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.qhong.woohoo.utils.FriendGroup;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.User;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> mEmail;
    private MutableLiveData<String> mNickname;
    private MutableLiveData<ArrayList<FriendGroup>> mFriends;
    private FirebaseAuth mAuth;
    private MutableLiveData<ArrayList<User>> friendsPendingList;

    public boolean fetched = true;

    private MutableLiveData<User> user;

    public MainViewModel() {
        mEmail = new MutableLiveData<>();
        mEmail.setValue("");
        mNickname = new MutableLiveData<>();
        mNickname.setValue("");
        user = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
        mFriends = new MutableLiveData<>();
        friendsPendingList = new MutableLiveData<>();
        initFriendList();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            updateFriendsList(currentUser);
            mEmail.setValue(currentUser.getEmail());
            //fetchAllUsers();
        }


    }
    public void fetchCurUsers(final String UID){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot shot : snapshot.getChildren()) {
                    if (UID.equals(shot.getKey())){
                        User tempUser = new User();
                        tempUser.setUid(shot.getKey());
                        tempUser.setName(shot.child("name").getValue(String.class));
                        tempUser.setEmail(shot.child("email").getValue(String.class));
                        tempUser.setIcon("Default profile photo");
                        ArrayList<FriendGroup> tempGroups = new ArrayList<>();
                        for (DataSnapshot s : shot.child("friend").getChildren()) {
                            FriendGroup temp = new FriendGroup();
                            temp.setGroupName(s.getKey());
                            ArrayList<User> tempList = new ArrayList<>();
                            for (DataSnapshot sUser : s.getChildren()) {
                                User t = new User();
                                t.setUid(sUser.getKey());
                                t.setName(sUser.child("name").getValue(String.class));
                                t.setEmail(sUser.child("email").getValue(String.class));
                                t.setIcon("Default profile photo");
                                tempList.add(t);
                            }
                            temp.setFriends(tempList);
                            tempGroups.add(temp);

                        }
                        tempUser.setFriends(tempGroups);
                        user.setValue(tempUser);
                    }
                }
                fetched = false;
                Log.e("Tag","Fetch success");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteFriend(String UID){
        FirebaseUser currentUser = mAuth.getCurrentUser();
       DatabaseReference tt = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid())
               .child("friend").child(UID);
       tt.removeValue();
    }
    public void initFriendList(){
        ArrayList<FriendGroup> groups = new ArrayList<>();
        FriendGroup temp = new FriendGroup();
        temp.setGroupName("Default Group");
        ArrayList<User> list = new ArrayList<>();
        temp.setFriends(list);
        groups.add(temp);
        mFriends.setValue(groups);
    }

    public void updateFriendsList(FirebaseUser currentUser){
        FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("friend")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<FriendGroup> groups = new ArrayList<>();
                        for (DataSnapshot shot : snapshot.getChildren()) {
                            FriendGroup temp = new FriendGroup();
                            temp.setGroupName(shot.getKey());
                            ArrayList<User> list = new ArrayList<>();
                            for(DataSnapshot s : shot.getChildren()){
                                User u = new User();
                                u.setName(s.child("name").getValue(String.class));
                                u.setEmail(s.child("email").getValue(String.class));
                                list.add(u);
                            }
                            temp.setFriends(list);
                            groups.add(temp);
                        }
                        if(groups.size()!=0){
                            mFriends.setValue(groups);
                        }
                        fetched = false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public MutableLiveData<ArrayList<User>> getFriendsPendingList() {
        return friendsPendingList;
    }

    public void setFriendsPendingList(ArrayList<User> list) {
        friendsPendingList.setValue(list);
        Log.e("tag",""+list.size());
    }

    public MutableLiveData<ArrayList<FriendGroup>> getmFriends() {
        return mFriends;
    }

    public MutableLiveData<String> getmNickname() {
        return mNickname;
    }

    public void setmNickname(String nickname) {
        mNickname.setValue(nickname);
    }

    public MutableLiveData<String> getmEmail() {
        return mEmail;
    }
    public boolean isSignedIn(){return mEmail.getValue()!="";}
    public void update(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            mEmail.setValue(currentUser.getEmail());
        }
    }
    public void signOut(){
        mEmail.setValue("");
        mAuth.signOut();
    }
}