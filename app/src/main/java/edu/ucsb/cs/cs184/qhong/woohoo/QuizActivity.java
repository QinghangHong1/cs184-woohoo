package edu.ucsb.cs.cs184.qhong.woohoo;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import edu.ucsb.cs.cs184.qhong.woohoo.utils.Game;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.ProblemSet;

public class QuizActivity extends AppCompatActivity {

    private QuizViewModel quizViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retrieve the data from wait room fragment or student waiting fragment
        // room id, problem set name, and time per question
        Intent i = getIntent();
        Bundle data = i.getExtras();

        int room_id = data.getInt("room_id");
        final String problemSet_name = data.getString("problemSet_name");
        int time_per_ques = data.getInt("time_per_ques");

        // put all data into the quiz view model
        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        quizViewModel.setCode(room_id);
        quizViewModel.getmGame().getValue().setRoomId(room_id);
        quizViewModel.getmGame().getValue().setProblemSetName(problemSet_name);
        quizViewModel.getmGame().getValue().setTimePerQuestion(time_per_ques);


        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
}