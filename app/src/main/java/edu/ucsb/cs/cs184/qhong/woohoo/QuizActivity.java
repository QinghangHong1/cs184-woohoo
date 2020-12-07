package edu.ucsb.cs.cs184.qhong.woohoo;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import edu.ucsb.cs.cs184.qhong.woohoo.utils.Game;

public class QuizActivity extends AppCompatActivity {

    private QuizViewModel quizViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Intent i = getIntent();
//        Game game = (Game)i.getSerializableExtra("mGame");
//        quizViewModel.setmGame(game);
//        quizViewModel.setCode(game.getRoomId());
    }
}