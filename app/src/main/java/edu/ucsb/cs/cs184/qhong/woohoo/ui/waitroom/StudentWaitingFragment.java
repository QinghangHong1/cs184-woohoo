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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.qhong.woohoo.FindViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.QuizActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.ui.setting.SettingFragment;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Player;

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
        final boolean[] check = {true};
        curRoom.addValueEventListener(new ValueEventListener() {
            final Intent intent=new Intent(getActivity(), QuizActivity.class);
            Bundle data = new Bundle();
            @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Qinghang Hong 12/08 transfer room id to next activity
                    // Haochen 12/9 add problem set name, and time per questions data when
                    // transfer to other activities
                    if(snapshot.hasChild("ProblemSet")){
                        data.putString("problemSet_name", (String) snapshot.child("ProblemSet").getValue());
                    }

                    if(snapshot.hasChild("TimePerQuestion")){
                        Integer timePerQuestion =
                                ((Long)snapshot.child("TimePerQuestion").getValue()).intValue();
                        data.putInt("time_per_ques", timePerQuestion);
                    }
                    if(snapshot.hasChild("players")){
                        GenericTypeIndicator<ArrayList<Player>> t = new GenericTypeIndicator<ArrayList<Player>>() {};
                        ArrayList<Player> players = (ArrayList<Player>)snapshot.child("players").getValue(t);
                        TextView numPlayerText = (TextView) getActivity().findViewById(R.id.numberOfPlayersText);
                        numPlayerText.setText(players.size() + " players in the room");
                    }
                    if(snapshot.hasChild("start")){
                        data.putInt("room_id", mViewModel.getCode().getValue());
                        intent.putExtras(data);
                        Log.e("tag", "on intent in student waiting fragment");
                        if(check[0]){
                            startActivity(intent);
                            check[0] = false;
                        }
                    }
//                    else{
//                        Toast.makeText(getContext(), "Quiz is going to start!",
//                                Toast.LENGTH_LONG).show();
//                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}