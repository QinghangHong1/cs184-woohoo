package edu.ucsb.cs.cs184.qhong.woohoo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.qhong.woohoo.ui.findroom.FindFragment;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Game;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Player;

public class SettingViewModel extends ViewModel {

    private MutableLiveData<Game> mGame;
    private FirebaseAuth mAuth;
    private MutableLiveData<Integer> code;

    private MutableLiveData<String> PSetName;
    public void setProbSetName(String setName){ this.PSetName.setValue(setName);}
    public MutableLiveData<String> getProbSetName(){ return PSetName;}

    public SettingViewModel() {
        mGame = new MutableLiveData<>(new Game());
        code = new MutableLiveData<>();
        PSetName = new MutableLiveData<>();
        PSetName.setValue("");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public MutableLiveData<Game> getmGame() {
        return mGame;
    }
    public void setmGame(Game game){
        mGame.setValue(game);
    }
    public void setCode(int code){
        this.code.setValue(code);
    }
    public MutableLiveData<Integer> getCode(){return code;}


    public void setTimePerQuestion(int time){
        mGame.getValue().setTimePerQuestion(time);
    }


    public void initialize(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CurrentRoom");

        //new code-YZ
        int generatedCode = mGame.getValue().getRoomId();
        setCode(generatedCode);
        DatabaseReference curRoom = myRef.child("Room"+ generatedCode);

        DatabaseReference problemSet = curRoom.child("ProblemSet");
        problemSet.setValue(getProbSetName().getValue());

        DatabaseReference timePerQuestion = curRoom.child("TimePerQuestion");
        timePerQuestion.setValue(mGame.getValue().getTimePerQuestion());

        // get the user id and create player list
        DatabaseReference player = curRoom.child("players");
        ArrayList<Player> players = new ArrayList<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        // QH add host
        curRoom.child("host_uid").setValue(uid);
        players.add(new Player(uid, 0));
        player.setValue(players);

        // add current quiz index(int)
        DatabaseReference quizIndex = curRoom.child("QuizIndex");
        quizIndex.setValue(0);

        // add start value(int)
//        DatabaseReference start = curRoom.child("start");
//        start.setValue(0);
    }


}