package edu.ucsb.cs.cs184.qhong.woohoo.ui.quiz;

import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.MotionEvent;
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
        final Button button1 = getActivity().findViewById(R.id.button1);
        final Button button2 = getActivity().findViewById(R.id.button2);
        final Button button3 = getActivity().findViewById(R.id.button3);
        final Button button4 = getActivity().findViewById(R.id.button4);

        button1.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        button1.setBackgroundColor(getResources().getColor(R.color.colorMyGray));
                        break;

                    case MotionEvent.ACTION_UP:
                        button1.setBackgroundColor(getResources().getColor(R.color.colorMyWhite));
                        button1.setTextColor(getResources().getColor(R.color.colorMyRed));
                        button2.setTextColor(getResources().getColor(R.color.colorMyBlack));
                        button3.setTextColor(getResources().getColor(R.color.colorMyBlack));
                        button4.setTextColor(getResources().getColor(R.color.colorMyBlack));
                        mViewModel.getmGame().getValue().setCurrChooseAnswIndex(0);
                        Log.e("answer", "current answer index change to 0 !");
                        break;
                }
                return true;
            }
        });

        button2.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        button2.setBackgroundColor(getResources().getColor(R.color.colorMyGray));
                        break;

                    case MotionEvent.ACTION_UP:
                        button2.setBackgroundColor(getResources().getColor(R.color.colorMyWhite));
                        button2.setTextColor(getResources().getColor(R.color.colorMyRed));
                        button1.setTextColor(getResources().getColor(R.color.colorMyBlack));
                        button3.setTextColor(getResources().getColor(R.color.colorMyBlack));
                        button4.setTextColor(getResources().getColor(R.color.colorMyBlack));
                        mViewModel.getmGame().getValue().setCurrChooseAnswIndex(1);
                        Log.e("answer", "current answer index change to 1 !");
                        break;
                }
                return true;
            }
        });

        button3.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        button3.setBackgroundColor(getResources().getColor(R.color.colorMyGray));
                        break;

                    case MotionEvent.ACTION_UP:
                        button3.setBackgroundColor(getResources().getColor(R.color.colorMyWhite));
                        button3.setTextColor(getResources().getColor(R.color.colorMyRed));
                        button2.setTextColor(getResources().getColor(R.color.colorMyBlack));
                        button1.setTextColor(getResources().getColor(R.color.colorMyBlack));
                        button4.setTextColor(getResources().getColor(R.color.colorMyBlack));
                        mViewModel.getmGame().getValue().setCurrChooseAnswIndex(2);
                        Log.e("answer", "current answer index change to 2 !");
                        break;
                }
                return true;
            }
        });

        button4.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        button4.setBackgroundColor(getResources().getColor(R.color.colorMyGray));
                        break;

                    case MotionEvent.ACTION_UP:
                        button4.setBackgroundColor(getResources().getColor(R.color.colorMyWhite));
                        button4.setTextColor(getResources().getColor(R.color.colorMyRed));
                        button2.setTextColor(getResources().getColor(R.color.colorMyBlack));
                        button1.setTextColor(getResources().getColor(R.color.colorMyBlack));
                        button3.setTextColor(getResources().getColor(R.color.colorMyBlack));
                        mViewModel.getmGame().getValue().setCurrChooseAnswIndex(3);
                        Log.e("answer", "current answer index change to 3 !");
                        break;
                }
                return true;
            }
        });
    }
}