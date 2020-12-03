package edu.ucsb.cs.cs184.qhong.woohoo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> mEmail;
    private FirebaseAuth mAuth;

    public MainViewModel() {
        mEmail = new MutableLiveData<>();
        mEmail.setValue("");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            mEmail.setValue(currentUser.getEmail());
        }
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