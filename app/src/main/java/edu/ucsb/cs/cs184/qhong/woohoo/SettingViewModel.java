package edu.ucsb.cs.cs184.qhong.woohoo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.ucsb.cs.cs184.qhong.woohoo.utils.Game;

public class SettingViewModel extends ViewModel {

    private MutableLiveData<Game> mGame;
    private FirebaseAuth mAuth;

    public SettingViewModel() {
        mGame = new MutableLiveData<>(new Game());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    public MutableLiveData<Game> getmGame() {
        return mGame;
    }
    public void setTimePerQuestion(int time){
        mGame.getValue().setTimePerQuestion(time);
    }
    public void update(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CurrentRoom");
        DatabaseReference curRoom = myRef.child("Room"+ mGame.getValue().getRoomId());
        DatabaseReference problemSet = curRoom.child("ProblemSet");
        problemSet.setValue("0");
        DatabaseReference timePerQuestion = curRoom.child("TimePerQuestion");
        timePerQuestion.setValue(mGame.getValue().getTimePerQuestion());
        DatabaseReference player = curRoom.child("Player");
        player.setValue("Player0");


    }
}