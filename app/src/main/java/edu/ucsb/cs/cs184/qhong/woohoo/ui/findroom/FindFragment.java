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
import com.google.firebase.database.ValueEventListener;

import edu.ucsb.cs.cs184.qhong.woohoo.FindViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.ui.setting.SettingFragment;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Game;

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
        mViewModel = new ViewModelProvider(this).get(FindViewModel.class);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        curRoom = FirebaseDatabase.getInstance().getReference("CurrentRoom");;

        Button start = getActivity().findViewById(R.id.button_student_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText code = getActivity().findViewById(R.id.input_student_code);
//                mViewModel.findRoom(Integer.parseInt(code.getText().toString()));
//                mViewModel.setCode(Integer.parseInt(code.getText().toString()));
                curRoom.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("Room"+code.getText().toString())){
//                            mViewModel.setmGame(new Game(Integer.parseInt(code.getText().toString())));
//                            Log.e("Tag",""+mViewModel.getmGame().getValue().getRoomId());
                            NavHostFragment.findNavController(FindFragment.this)
                                    .navigate(R.id.action_findFragment_to_studentWaitingFragment);
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