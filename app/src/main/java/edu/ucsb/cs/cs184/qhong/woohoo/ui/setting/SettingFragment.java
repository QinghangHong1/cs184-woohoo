package edu.ucsb.cs.cs184.qhong.woohoo.ui.setting;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.ucsb.cs.cs184.qhong.woohoo.FindViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.MainActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.SettingViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.ui.findroom.FindFragment;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.ProblemSet;

//YZ2nd: Setting Fragment, quiz game room setting
public class SettingFragment extends Fragment {

    private SettingViewModel settingViewModel;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.setting_fragment, container, false);



    }

//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        view.findViewById(R.id.settingCreateButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(SettingFragment.this)
//                        .navigate(R.id.action_setting_to_waitRoomFragment);
//            }
//        });
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        settingViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
//        final TextView timePerQuestion = getActivity().findViewById(R.id.inputTimePerQues);
//
//        Button settingCreateButton = getActivity().findViewById(R.id.settingCreateButton);
//        settingCreateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int time = Integer.parseInt(timePerQuestion.getText().toString());
//                settingViewModel.setTimePerQuestion(time);
//                settingViewModel.update();
//                NavHostFragment.findNavController(SettingFragment.this)
//                        .navigate(R.id.action_setting_to_waitRoomFragment);
//            }
//        });
        //1.Jiajun original code in meeting
        //2.Yiwei adjusted it on 12.4-12.6
        //3.Haochen adjust the function name update => initialize and add functionality of
        // obtaining the problem set from the firebase
        settingViewModel = new ViewModelProvider(getActivity()).get(SettingViewModel.class);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("ProblemSet");
        final List<String> list;
        list = new ArrayList<String>();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot n : snapshot.getChildren()) {
                    list.add(n.getKey().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) getActivity().findViewById(R.id.spinner);
        sp.setAdapter(adapter);

//        final TextView tvResult = (TextView) getActivity().findViewById(R.id.textView3);
//
//        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                //获取Spinner控件的适配器
//////                Log.e("here",
//////                        "it is :"+adapter.getItem(position));
//////                tvResult.setText(adapter.getItem(position));
////                String selected = list.get(position);
//                String selected = parent.getItemAtPosition(position).toString();
//                tvResult.setText(selected);
////                parent.setVisibility(View.VISIBLE);
//
//                Log.e("this",parent.getItemAtPosition(position).toString());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Log.e("this", "");
//            }
//        });


        Button settingCreateButton = getActivity().findViewById(R.id.settingCreateButton);
        settingCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    final EditText ProblemSetName = getActivity().findViewById(R.id.input2);
                    final EditText timePerQuestion = getActivity().findViewById(R.id.inputTimePerQues);
                    private String temp = ProblemSetName.getText().toString();

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("" + ProblemSetName.getText().toString())) {
                            int time = Integer.parseInt(timePerQuestion.getText().toString());
                            settingViewModel.setTimePerQuestion(time);
                            settingViewModel.setProbSetName(ProblemSetName.getText().toString());

                            // check if the user has already log in before create room
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser == null) {
                                Toast.makeText(getContext(), "You need to log in before create a " +
                                                "room",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                settingViewModel.initialize();

                                NavHostFragment.findNavController(SettingFragment.this)
                                        .navigate(R.id.action_setting_to_waitRoomFragment);
                            }

                        } else {
                            Toast.makeText(getContext(), "The problem set is not exist, please enter correct problem set name or upload your problem set!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        FloatingActionButton fab = getActivity().findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}