package edu.ucsb.cs.cs184.qhong.woohoo.ui.settlement;

import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

import edu.ucsb.cs.cs184.qhong.woohoo.MainActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.QuizActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.QuizViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.SettingViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.ui.quiz.QuizFragment;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Game;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Player;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Problem;

public class SettlementFragment extends Fragment {

    private QuizViewModel quizViewModel;
    private boolean allFinished = false;
    public static SettlementFragment newInstance() {
        return new SettlementFragment();
    }
    private ProgressDialog mDialog;
    private ArrayList<Player> mPlayers;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settlement_fragment, container, false);
    }
    String uid;
    FirebaseDatabase firebaseDatabase;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        quizViewModel = new ViewModelProvider(getActivity()).get(QuizViewModel.class);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference playersRef = firebaseDatabase.getReference("CurrentRoom").child("Room" + quizViewModel.getCode().getValue()).child("players");
        playersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    GenericTypeIndicator<ArrayList<Player>> t = new GenericTypeIndicator<ArrayList<Player>>() {
                    };
                    ArrayList<Player> players = (ArrayList<Player>) snapshot.getValue(t);

                    for (int i = 0; i < players.size(); i++) {
                        if (!players.get(i).isFinished()) {
                            return;
                        }
                    }
                    allFinished = true;
                    mPlayers = players;
//                if (allFinished) {
//                    displayRanking(players);
//                }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(!allFinished){
//            Log.e("Tag","Signed in");
            mDialog = ProgressDialog.show(getContext(),"Waiting for other players to finish","Loading...",true);

            final Handler handler = new Handler();
            new Thread(){
                public void run(){
                    try{
                        while(!allFinished){
                            sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally{
                        mDialog.dismiss();
                        Log.e("Tag","finish fetching problem set");

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                               displayRanking(mPlayers);
                            }
                        });
                    }
                }
            }.start();

        }

        final boolean[] isHost = new boolean[1];
        DatabaseReference curRoomHostRef = firebaseDatabase.getReference("CurrentRoom").child("Room" + quizViewModel.getCode().getValue()).child("host_uid");
        curRoomHostRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String host_uid = snapshot.getValue(String.class);
                if (host_uid.equals(currentUser.getUid())){
                    isHost[0] = true;
                }else{
                    isHost[0] = false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FloatingActionButton fab = getActivity().findViewById(R.id.buttonBackToHome);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHost[0]) {
                    FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                    DatabaseReference curRoomRef = firebaseDatabase1.getReference("CurrentRoom").child("Room" + quizViewModel.getCode().getValue());
                    curRoomRef.removeValue();
                }

                // Use the start button in wait room to translate to the quiz activity
                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void displayRanking(ArrayList<Player> players){
        int totalQuestion = 0;

        Collections.sort(players, Collections.<Player>reverseOrder());;
        if (players.size() > 0){
            final Player firstPlayer = players.get(0);
            final TextView textView = (TextView)getActivity().findViewById(R.id.FirstPlayer);
            DatabaseReference  playerRef = firebaseDatabase.getReference("Users").child(firstPlayer.getUid());
            playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String nickname = snapshot.child("name").getValue(String.class);
                    String displayText = String.format("No.1:      %s         %d", nickname, firstPlayer.getScore());
                    textView.setText(displayText);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        if (players.size() > 1){
            final Player secondPlayer = players.get(1);
            final TextView textView = (TextView)getActivity().findViewById(R.id.SecondPlayer);
            DatabaseReference  playerRef = firebaseDatabase.getReference("Users").child(secondPlayer.getUid());
            playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String nickname = snapshot.child("name").getValue(String.class);
                    String displayText = String.format("No.2:      %s         %d", nickname, secondPlayer.getScore());
                    textView.setText(displayText);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            getActivity().findViewById(R.id.SecondPlayer).setVisibility(View.INVISIBLE);
        }
        if (players.size() > 2){
            final Player thirdPlayer = players.get(2);
            final TextView textView = (TextView)getActivity().findViewById(R.id.ThirdPlayer);
            DatabaseReference  playerRef = firebaseDatabase.getReference("Users").child(thirdPlayer.getUid());
            playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String nickname = snapshot.child("name").getValue(String.class);
                    String displayText = String.format("No.3:      %s         %d", nickname, thirdPlayer.getScore());
                    textView.setText(displayText);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            getActivity().findViewById(R.id.ThirdPlayer).setVisibility(View.INVISIBLE);
        }

        // display current user's score
        TextView currentUserText = getActivity().findViewById(R.id.UserName);
        for (int i = 0; i < players.size(); i++){
            Player temp = players.get(i);
            if (temp.getUid().equals(uid)){
                String displayText = String.format("You rank No.%d with %d correct answers", i + 1, temp.getScore());
                currentUserText.setText(displayText);
            }
        }

    }

}