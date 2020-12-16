package edu.ucsb.cs.cs184.qhong.woohoo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.ucsb.cs.cs184.qhong.woohoo.utils.Game;

public class QuizViewModel extends ViewModel {
    private MutableLiveData<Game> mGame;
    private FirebaseAuth mAuth;
    private DatabaseReference curRoom;
    private MutableLiveData<Integer> code;
    private boolean fetchProblemSet = false;

    public QuizViewModel() {
        mGame = new MutableLiveData<>(new Game());
        code = new MutableLiveData<>(mGame.getValue().getRoomId());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        curRoom = FirebaseDatabase.getInstance().getReference("CurrentRoom");
    }

    public QuizViewModel(Game existedGame) {
        mGame = new MutableLiveData<>(existedGame);
        code = new MutableLiveData<>(existedGame.getRoomId());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        curRoom = FirebaseDatabase.getInstance().getReference("CurrentRoom");
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
    public boolean getFetchProblemSet(){return fetchProblemSet;}
    public void setFetchProblemSet(boolean hello){fetchProblemSet = hello;}
}
