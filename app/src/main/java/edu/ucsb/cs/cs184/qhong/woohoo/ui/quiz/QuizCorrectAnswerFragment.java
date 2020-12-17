package edu.ucsb.cs.cs184.qhong.woohoo.ui.quiz;

import androidx.lifecycle.ViewModelProvider;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.qhong.woohoo.QuizViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.SettingViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Game;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Player;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Problem;

public class QuizCorrectAnswerFragment extends Fragment {

    private QuizViewModel mViewModel;
    private TextView txtView;
    CountDownTimer countDownTimer;

    public static QuizCorrectAnswerFragment newInstance() {
        return new QuizCorrectAnswerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.quiz_correct_answer_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(QuizViewModel.class);

        Game game = mViewModel.getmGame().getValue();

        // put the question and answers into the page
        Problem problem = game.getProblem(game.getCurrentProblemIndex()-2);
        TextView questionView = getActivity().findViewById(R.id.questions2);
        questionView.setText(problem.getQuestion());

        TextView ansView = getActivity().findViewById(R.id.correctAnswer);
        int index = problem.getTrue_answer_index();
        ansView.setText(problem.getAnswer_choices().get(index));

        int userAnswer = game.getCurrChooseAnswIndex();
        if(userAnswer != index){
            Toast.makeText(getContext(), "Sorry, luck does not stand on your side",
                    Toast.LENGTH_LONG).show();
            MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.buzzer_wrong_answer);
            mediaPlayer.start();
        }else{
            Toast.makeText(getContext(), "Good choice!",
                    Toast.LENGTH_LONG).show();
            game.addScore();
            MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.cheering);
            mediaPlayer.start();
        }

        txtView = getActivity().findViewById(R.id.timeInQuiz2);
        countDownTimer = new CountDownTimer(5 * 1000, 1 * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtView.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                final Game game = mViewModel.getmGame().getValue();
                // check if all questions are displayed
                // wrong check index function, need to change in Game.java
                // update the totoal score for the user in the database 12.12
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                final FirebaseUser currentUser = mAuth.getCurrentUser();
                final String uid = currentUser.getUid();
                final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference playersRef =
                            firebaseDatabase.getReference("CurrentRoom").child("Room" + mViewModel.getCode().getValue()).child("players");

                playersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // find the local player and set the player score in the firebase
                        GenericTypeIndicator<ArrayList<Player>> t = new GenericTypeIndicator<ArrayList<Player>>() {};
                        ArrayList<Player> players = (ArrayList<Player>)snapshot.getValue(t);

                        int userIndex = -1;
                        for (int i = 0; i < players.size(); i++){
                            Player temp = players.get(i);
                            if (temp.getUid().equals(uid)){
                                userIndex = i;
                                break;
                            }
                        }
                        int score = game.getScore();
                        firebaseDatabase.getReference("CurrentRoom").child("Room" + mViewModel.getCode().getValue()).child("players").child(String.valueOf(userIndex)).child("score").setValue(score);

                        if(!game.checkIndex()){
                            Log.e("tag", "to settlement");
                            firebaseDatabase.getReference("CurrentRoom").child("Room" + mViewModel.getCode().getValue()).child("players").child(String.valueOf(userIndex)).child("finished").setValue(true);
                            NavHostFragment.findNavController(QuizCorrectAnswerFragment.this)
                                    .navigate(R.id.action_quizCorrectAnswerFragment_to_settlementFragment);
                        }else{
                            Log.e("tag", "to quiz");
                            // translate to correct answer page
                            NavHostFragment.findNavController(QuizCorrectAnswerFragment.this)
                                    .navigate(R.id.action_quizCorrectAnswerFragment_to_quizFragment);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                if(!game.checkIndex()){
//                    NavHostFragment.findNavController(QuizCorrectAnswerFragment.this)
//                            .navigate(R.id.action_quizCorrectAnswerFragment_to_settlementFragment);
//                }else{
//                    Log.e("tag", "I have been there");
//                    // translate to correct answer page
//                    NavHostFragment.findNavController(QuizCorrectAnswerFragment.this)
//                            .navigate(R.id.action_quizCorrectAnswerFragment_to_quizFragment);
//                }
            }

        };
        countDownTimer.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}