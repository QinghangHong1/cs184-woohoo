package edu.ucsb.cs.cs184.qhong.woohoo;

import android.renderscript.Sampler;

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

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> mEmail;
    private FirebaseAuth mAuth;
    private MutableLiveData<String> mNickname;

    public MainViewModel() {
        mEmail = new MutableLiveData<>();
        mEmail.setValue("");
        mNickname = new MutableLiveData<>();
        mNickname.setValue("");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            mEmail.setValue(currentUser.getEmail());
        }
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