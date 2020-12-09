package edu.ucsb.cs.cs184.qhong.woohoo.ui.quiz;

import androidx.lifecycle.ViewModelProvider;

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

import edu.ucsb.cs.cs184.qhong.woohoo.QuizViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.SettingViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Game;
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

        txtView = getActivity().findViewById(R.id.timeInQuiz2);
        countDownTimer = new CountDownTimer(5 * 1000, 1 * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtView.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                Game game = mViewModel.getmGame().getValue();
                // check if all questions are displayed
                // wrong check index function, need to change in Game.java
                if(!game.checkIndex()){
                    NavHostFragment.findNavController(QuizCorrectAnswerFragment.this)
                            .navigate(R.id.action_quizCorrectAnswerFragment_to_settlementFragment);
                    return;
                }

                // translate to correct answer page
                NavHostFragment.findNavController(QuizCorrectAnswerFragment.this)
                        .navigate(R.id.action_quizCorrectAnswerFragment_to_quizFragment);
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