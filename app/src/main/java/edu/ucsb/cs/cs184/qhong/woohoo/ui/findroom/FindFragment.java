package edu.ucsb.cs.cs184.qhong.woohoo.ui.findroom;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.qhong.woohoo.FindViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.ui.setting.SettingFragment;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Game;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Player;

//YZ2nd: FindRoom Fragment
public class FindFragment extends Fragment {

    private FindViewModel mViewModel;
    private FirebaseAuth mAuth;
    private DatabaseReference curRoom;
    public static FindFragment newInstance() {
        return new FindFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.find_fragment, container, false);
    }

//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        view.findViewById(R.id.button_student_start).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FindFragment.this)
//                        .navigate(R.id.action_findFragment_to_studentWaitingFragment);
//            }
//        });
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(FindViewModel.class);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        curRoom = FirebaseDatabase.getInstance().getReference("CurrentRoom");;

        Button start = getActivity().findViewById(R.id.button_student_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText code = getActivity().findViewById(R.id.input_student_code);
//                mViewModel.findRoom(Integer.parseInt(code.getText().toString()));
//                mViewModel.setCode(Integer.parseInt(code.getText().toString())); 被移动
                curRoom.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("Room"+code.getText().toString())){
//                            mViewModel.setmGame(new Game(Integer.parseInt(code.getText().toString())));
//                            Log.e("Tag",""+mViewModel.getmGame().getValue().getRoomId());
                            mViewModel.setCode(Integer.parseInt(code.getText().toString()));
                            // QH 12/09 add current player to player list of the joined room
                            if (currentUser != null){
                                String uid = currentUser.getUid();
                                GenericTypeIndicator<ArrayList<Player>> t = new GenericTypeIndicator<ArrayList<Player>>() {};
                                ArrayList<Player> players = (ArrayList<Player>)snapshot.child("Room" + code.getText().toString()).child("players").getValue(t);
                                players.add(new Player(uid, 0));
                                curRoom.child("Room" + code.getText().toString()).child("players").setValue(players);
                                NavHostFragment.findNavController(FindFragment.this)
                                        .navigate(R.id.action_findFragment_to_studentWaitingFragment);
                            }else {
                                Toast.makeText(getContext(), "Sign in first!",
                                        Toast.LENGTH_LONG).show();
                            }

                        }else{
                            Toast.makeText(getContext(), "Invalid Code!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

}