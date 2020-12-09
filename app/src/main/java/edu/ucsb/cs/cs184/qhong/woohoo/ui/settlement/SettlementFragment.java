package edu.ucsb.cs.cs184.qhong.woohoo.ui.settlement;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Player;

public class SettlementFragment extends Fragment {

    private QuizViewModel quizViewModel;

    public static SettlementFragment newInstance() {
        return new SettlementFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settlement_fragment, container, false);
    }
    String uid;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        quizViewModel = new ViewModelProvider(getActivity()).get(QuizViewModel.class);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference playersRef = firebaseDatabase.getReference("CurrentRoom").child("Room" + quizViewModel.getCode().getValue()).child("players");
        playersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<Player>> t = new GenericTypeIndicator<ArrayList<Player>>() {};
                ArrayList<Player> players = (ArrayList<Player>)snapshot.getValue(t);

                displayRanking(players);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

        Collections.sort(players);
        if (players.size() > 0){
            Player firstPlayer = players.get(0);
            TextView textView = (TextView)getActivity().findViewById(R.id.FirstPlayer);
            String displayText = String.format("No.1 %s %d", firstPlayer.getUid(), firstPlayer.getScore());
            textView.setText(displayText);
        }
        if (players.size() > 1){
            Player secondPlayer = players.get(1);
            TextView textView = (TextView)getActivity().findViewById(R.id.SecondPlayer);
            String displayText = String.format("No.2 %s %d", secondPlayer.getUid(), secondPlayer.getScore());
            textView.setText(displayText);
        }else {
            getActivity().findViewById(R.id.SecondPlayer).setVisibility(View.INVISIBLE);
        }
        if (players.size() > 2){
            Player thirdPlayer = players.get(2);
            TextView textView = (TextView)getActivity().findViewById(R.id.SecondPlayer);
            String displayText = String.format("No.3 %s %d", thirdPlayer.getUid(), thirdPlayer.getScore());
            textView.setText(displayText);
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