package edu.ucsb.cs.cs184.qhong.woohoo.ui.quiz;

import androidx.lifecycle.ViewModelProvider;

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
import android.widget.TextView;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import edu.ucsb.cs.cs184.qhong.woohoo.FindViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.QuizActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.SettingViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.ui.setting.SettingFragment;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Game;
import edu.ucsb.cs.cs184.qhong.woohoo.QuizViewModel;

public class QuizFragment extends Fragment {

    private QuizViewModel mViewModel;
    private TextView timeView;
    private TextView quesIndexView;

    public static QuizFragment newInstance() {
        return new QuizFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.quiz_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(QuizViewModel.class);

        // check if the current question index excess the size of questions,
        // if so, translate to the settlement page
        // otherwise, update the question index
        quesIndexView = getActivity().findViewById(R.id.questionIndex);
        Game game = mViewModel.getmGame().getValue();

        Log.e("problem index", "problem index is: "+game.getCurrentProblemIndex());
        quesIndexView.setText(game.getCurrentProblemIndex()+"");
        game.updateCurrentProblemIndex();

        // get the timer
        timeView = getActivity().findViewById(R.id.timeInQuiz);
//        int time = game.getTimePerQuestion();
        int time = 5;
        Log.e("time", "in quiz fragment, time is:"+time);
        Log.e("time",
                "in quiz fragment, room id is :"+mViewModel.getCode().getValue());
        CountDownTimer countDownTimer = new CountDownTimer(time * 1000, 1 * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeView.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                // translate to correct answer page
                NavHostFragment.findNavController(QuizFragment.this)
                        .navigate(R.id.action_quizFragment_to_quizCorrectAnswerFragment);
            }
        };
        countDownTimer.start();

    }
}