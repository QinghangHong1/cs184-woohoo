package edu.ucsb.cs.cs184.qhong.woohoo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.ucsb.cs.cs184.qhong.woohoo.ui.findroom.FindFragment;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Game;

public class FindViewModel extends ViewModel {

    private MutableLiveData<Game> mGame;
    private FirebaseAuth mAuth;
    private DatabaseReference curRoom;
    private MutableLiveData<Integer> code;

    public FindViewModel() {
        mGame = new MutableLiveData<>();
        code = new MutableLiveData<>();
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
}