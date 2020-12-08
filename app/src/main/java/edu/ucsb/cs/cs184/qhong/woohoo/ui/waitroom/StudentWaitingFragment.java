package edu.ucsb.cs.cs184.qhong.woohoo.ui.waitroom;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.ucsb.cs.cs184.qhong.woohoo.FindViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.QuizActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.ui.setting.SettingFragment;

//YZ2nd: wait room for students
public class StudentWaitingFragment extends Fragment {

    private FindViewModel mViewModel;

    public static StudentWaitingFragment newInstance() {
        return new StudentWaitingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.student_waiting_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(FindViewModel.class);
//
        TextView textCode = getActivity().findViewById(R.id.textCode);
        textCode.setText(""+mViewModel.getCode().getValue());
        // TODO: Use the ViewModel

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CurrentRoom");
        DatabaseReference curRoom = myRef.child("Room"+ mViewModel.getCode().getValue());


//        curRoom.addListenerForSingleValueEvent(new ValueEventListener() {
        curRoom.addValueEventListener(new ValueEventListener() {

            @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild("start")){
                        Intent intent=new Intent(getActivity(), QuizActivity.class);
                        // Qinghang Hong 12/08 transfer room id to next activity
                        intent.putExtra("room_id", mViewModel.getCode().getValue());
                        startActivity(intent);
                    }else{
                        Toast.makeText(getContext(), "Quiz is going to start!",
                                Toast.LENGTH_LONG).show();
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}