package edu.ucsb.cs.cs184.qhong.woohoo.ui.quiz;

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
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import edu.ucsb.cs.cs184.qhong.woohoo.FindViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.MainActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.QuizActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.SettingActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.SettingViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.ui.setting.SettingFragment;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Game;
import edu.ucsb.cs.cs184.qhong.woohoo.QuizViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Problem;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.ProblemSet;

public class QuizFragment extends Fragment {

    private QuizViewModel mViewModel;
    private TextView timeView;
    private TextView quesIndexView;
    private TextView questionView;
    private Button ansButton;
    private ProgressDialog mDialog;
    private boolean fetchProbSet = false;

    public static QuizFragment newInstance() {
        return new QuizFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e("tag", "on create view");
        return inflater.inflate(R.layout.quiz_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(QuizViewModel.class);

        quesIndexView = getActivity().findViewById(R.id.questionIndex);
        Game game = mViewModel.getmGame().getValue();

        quesIndexView.setText("No."+game.getCurrentProblemIndex());

        final String problemSet_name = mViewModel.getmGame().getValue().getProblemSetName();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ProblemSet");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(problemSet_name)){
                    Log.e("tag", "in on data change in quiz fragment");
                    ProblemSet problemSet =
                            snapshot.child(problemSet_name).getValue(ProblemSet.class);
                    mViewModel.getmGame().getValue().setProblemSet(problemSet);

                    mViewModel.setFetchProblemSet(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final boolean[] ready = {false};

        if(!mViewModel.getFetchProblemSet()){
//            Log.e("Tag","Signed in");
            mDialog = ProgressDialog.show(getContext(),"Load problem set","Loading...",true);

            final Handler handler = new Handler();
            new Thread(){
                public void run(){
                        try{
                            while(!mViewModel.getFetchProblemSet()){
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
                                    // put the question and answers into the page
                                    Game game = mViewModel.getmGame().getValue();
                                    Problem problem = game.getProblem(game.getCurrentProblemIndex()-1);
                                    questionView = getActivity().findViewById(R.id.questions1);
                                    questionView.setText(problem.getQuestion());

                                    ansButton = getActivity().findViewById(R.id.button1);
                                    ansButton.setText(problem.getAnswer_choices().get(0));

                                    ansButton = getActivity().findViewById(R.id.button2);
                                    ansButton.setText(problem.getAnswer_choices().get(1));

                                    ansButton = getActivity().findViewById(R.id.button3);
                                    ansButton.setText(problem.getAnswer_choices().get(2));

                                    ansButton = getActivity().findViewById(R.id.button4);
                                    ansButton.setText(problem.getAnswer_choices().get(3));

                                    // get the timer
                                    timeView = getActivity().findViewById(R.id.timeInQuiz);
                                    int time = game.getTimePerQuestion();
                                    CountDownTimer countDownTimer = new CountDownTimer(time * 1000, 1 * 1000) {

                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            changeAnswerIndex(mViewModel);
                                            timeView.setText(millisUntilFinished / 1000 + "s");
                                        }

                                        @Override
                                        public void onFinish() {
                                            // translate to correct answer page
                                            NavHostFragment.findNavController(QuizFragment.this)
                                                    .navigate(R.id.action_quizFragment_to_quizCorrectAnswerFragment);
                                        }
                                    };

                                    // update the current problem index
                                    game.updateCurrentProblemIndex();
                                    countDownTimer.start();
                                }
                            });
                        }
                }
            }.start();

        }else{
            // put the question and answers into the page
        game = mViewModel.getmGame().getValue();
        Problem problem = game.getProblem(game.getCurrentProblemIndex()-1);
        questionView = getActivity().findViewById(R.id.questions1);
        questionView.setText(problem.getQuestion());

        ansButton = getActivity().findViewById(R.id.button1);
        ansButton.setText(problem.getAnswer_choices().get(0));

        ansButton = getActivity().findViewById(R.id.button2);
        ansButton.setText(problem.getAnswer_choices().get(1));

        ansButton = getActivity().findViewById(R.id.button3);
        ansButton.setText(problem.getAnswer_choices().get(2));

        ansButton = getActivity().findViewById(R.id.button4);
        ansButton.setText(problem.getAnswer_choices().get(3));

        // get the timer
        timeView = getActivity().findViewById(R.id.timeInQuiz);
        int time = game.getTimePerQuestion();
        CountDownTimer countDownTimer = new CountDownTimer(time * 1000, 1 * 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                changeAnswerIndex(mViewModel);
                timeView.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                // translate to correct answer page
                NavHostFragment.findNavController(QuizFragment.this)
                        .navigate(R.id.action_quizFragment_to_quizCorrectAnswerFragment);
            }
        };

        // update the current problem index
        game.updateCurrentProblemIndex();
        countDownTimer.start();
        }

//        Log.e("tag", "new thread finsh");

//        // put the question and answers into the page
//        game = mViewModel.getmGame().getValue();
//        Problem problem = game.getProblem(game.getCurrentProblemIndex()-1);
//        questionView = getActivity().findViewById(R.id.questions1);
//        questionView.setText(problem.getQuestion());
//
//        ansButton = getActivity().findViewById(R.id.button1);
//        ansButton.setText(problem.getAnswer_choices().get(0));
//
//        ansButton = getActivity().findViewById(R.id.button2);
//        ansButton.setText(problem.getAnswer_choices().get(1));
//
//        ansButton = getActivity().findViewById(R.id.button3);
//        ansButton.setText(problem.getAnswer_choices().get(2));
//
//        ansButton = getActivity().findViewById(R.id.button4);
//        ansButton.setText(problem.getAnswer_choices().get(3));
//
//        // get the timer
//        timeView = getActivity().findViewById(R.id.timeInQuiz);
//        int time = game.getTimePerQuestion();
//        CountDownTimer countDownTimer = new CountDownTimer(time * 1000, 1 * 1000) {
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//                changeAnswerIndex(mViewModel);
//                timeView.setText(millisUntilFinished / 1000 + "s");
//            }
//
//            @Override
//            public void onFinish() {
//                // translate to correct answer page
//                NavHostFragment.findNavController(QuizFragment.this)
//                        .navigate(R.id.action_quizFragment_to_quizCorrectAnswerFragment);
//            }
//        };
//
//        // update the current problem index
//        game.updateCurrentProblemIndex();
//        countDownTimer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Tag", "on Destroy");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("tag", "on create");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("tag", "on pause");
    }

    public void changeAnswerIndex(final QuizViewModel mViewModel){
        Button button1 = getActivity().findViewById(R.id.button1);
        Button button2 = getActivity().findViewById(R.id.button2);
        Button button3 = getActivity().findViewById(R.id.button3);
        Button button4 = getActivity().findViewById(R.id.button4);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getmGame().getValue().setCurrChooseAnswIndex(0);
                Log.e("answer", "current answer index change to 0 !");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getmGame().getValue().setCurrChooseAnswIndex(1);
                Log.e("answer", "current answer index change to 1 !");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getmGame().getValue().setCurrChooseAnswIndex(2);
                Log.e("answer", "current answer index change to 2 !");
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getmGame().getValue().setCurrChooseAnswIndex(3);
                Log.e("answer", "current answer index change to 3 !");
            }
        });
    }
}