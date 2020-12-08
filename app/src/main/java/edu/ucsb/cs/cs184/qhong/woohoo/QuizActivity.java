package edu.ucsb.cs.cs184.qhong.woohoo;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;

import edu.ucsb.cs.cs184.qhong.woohoo.utils.Game;

public class QuizActivity extends AppCompatActivity {

    private QuizViewModel quizViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int room_id = getIntent().getIntExtra("room_id", 100000000);
        Log.d("room id", String.valueOf(room_id));
        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        quizViewModel.setCode(room_id);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Intent i = getIntent();
//        Game game = (Game)i.getSerializableExtra("mGame");
//        quizViewModel.setmGame(game);
//        quizViewModel.setCode(game.getRoomId());
    }
}