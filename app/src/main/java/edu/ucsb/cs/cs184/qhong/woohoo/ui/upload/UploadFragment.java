package edu.ucsb.cs.cs184.qhong.woohoo.ui.upload;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Problem;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.ProblemSet;

public class UploadFragment extends Fragment {

    private UploadViewModel uploadViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        uploadViewModel =
                ViewModelProviders.of(this).get(UploadViewModel.class);
        View root = inflater.inflate(R.layout.fragment_upload, container, false);
//        final TextView textView = root.findViewById(R.id.text_slideshow);
//        uploadViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // handle problem set name
        uploadViewModel = ViewModelProviders.of(this).get(UploadViewModel.class);
        Button problem_set_name_confirm_button = getActivity().findViewById(R.id.problem_set_name_confirm_button);
        problem_set_name_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText problem_set_name_text = (EditText)getActivity().findViewById(R.id.problem_set_name);
                String problem_set_name = problem_set_name_text.getText().toString();
                uploadViewModel.getProblemSet().getValue().setName(problem_set_name);
                v.setVisibility(View.GONE);
                problem_set_name_text.setFocusable(false);
                getActivity().findViewById(R.id.question_input_section).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.finish).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.question_next).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.true_answer_button_group).setVisibility(View.VISIBLE);
            }
        });

        // handle questions next button

        Button question_next = getActivity().findViewById(R.id.question_next);
        question_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProblems();
            }
        });

        // handle finish button
        Button finish = getActivity().findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!addProblems()){
                    return;
                }
                ProblemSet problemSet = uploadViewModel.getProblemSet().getValue();

                String name = problemSet.getName();
                DatabaseReference problemSetRef = FirebaseDatabase.getInstance().getReference("ProblemSet");
                DatabaseReference curSet = problemSetRef.child(name);
                curSet.setValue(problemSet);
//                for (int i = 0; i < problemSet.getNumProblems(); i++){
//                    Problem p = problemSet.getProblems().get(i);
//                    DatabaseReference currentProblemRef = curSet.child(String.valueOf(i));
//                    DatabaseReference currentQuestionRef = currentProblemRef.child("question");
//                    currentQuestionRef.setValue(p.getQuestion());
//                    currentProblemRef.child("answers").setValue(p.getAnswer_choices());
//                    currentProblemRef.child("true_answer_index").setValue(p.getTrue_answer_index());
//
//                }

                EditText problem_set_name_text = (EditText)getActivity().findViewById(R.id.problem_set_name);
                problem_set_name_text.getText().clear();
                problem_set_name_text.setFocusableInTouchMode(true);
                getActivity().findViewById(R.id.problem_set_name_confirm_button).setVisibility(View.VISIBLE);
                uploadViewModel.clearProblemSet();
                getActivity().findViewById(R.id.question_input_section).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.finish).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.question_next).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.true_answer_button_group).setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Upload problem set successfully", Toast.LENGTH_SHORT).show();
            }
        });



    }
    private boolean addProblems(){
        Problem p = new Problem();
        LinearLayout question_input_section = getActivity().findViewById(R.id.question_input_section);
        for(int i = 0; i < question_input_section.getChildCount(); i++){
            EditText text = (EditText) question_input_section.getChildAt(i);
            if(text.getText().toString().equals("")){
                Toast.makeText(getContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        EditText question_edit_text = (EditText) question_input_section.getChildAt(0);
        String question_text = question_edit_text.getText().toString();
        p.setQuestion(question_text);
        question_edit_text.getText().clear();
        RadioGroup radioGroup = (RadioGroup) getActivity().findViewById(R.id.true_answer_button_group);
        int true_answer_index;
        if (radioGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getContext(), "Please select a right answer", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton selectedRadioButton = (RadioButton)getActivity().findViewById(selectedId);
            true_answer_index = Integer.parseInt(selectedRadioButton.getTag().toString());
        }
        p.setTrue_answer_index(true_answer_index);
        for(int i = 1; i < question_input_section.getChildCount(); i++){
            EditText current_edit_text = (EditText)question_input_section.getChildAt(i);
            String current_choice = current_edit_text.getText().toString();
            current_edit_text.getText().clear();
            p.getAnswer_choices().add(current_choice);
        }
        uploadViewModel.getProblemSet().getValue().getProblems().add(p);
        return true;
    }
}