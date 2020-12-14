package edu.ucsb.cs.cs184.qhong.woohoo.ui.waitroom;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.qhong.woohoo.FindViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.MainActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.QuizActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.SettingActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.SettingViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Player;

//YZ2nd: wait room for teachers
public class WaitRoomFragment extends Fragment {

    private SettingViewModel mViewModel;

    public static WaitRoomFragment newInstance() {
        return new WaitRoomFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.waitroom_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(SettingViewModel.class);

        TextView textCode = getActivity().findViewById(R.id.textCode);
        textCode.setText("Room ID:"+mViewModel.getCode().getValue());
        TextView textName = getActivity().findViewById(R.id.textName);
        textName.setText("Problem Set Name:"+mViewModel.getProbSetName().getValue());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CurrentRoom");
        DatabaseReference playerReference = databaseReference.child("Room" + mViewModel.getCode().getValue());
        playerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("players")){
                    GenericTypeIndicator<ArrayList<Player>> t = new GenericTypeIndicator<ArrayList<Player>>() {};
                    ArrayList<Player> players = (ArrayList<Player>)snapshot.child("players").getValue(t);
                    TextView numPlayerText = (TextView) getActivity().findViewById(R.id.numberOfPlayersText1);
                    numPlayerText.setText(players.size() + " players in the room ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button fab = getActivity().findViewById(R.id.button_start);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("CurrentRoom");
                DatabaseReference curRoom = myRef.child("Room"+ mViewModel.getCode().getValue());
                Log.e("time",
                        "in data change, room id is :"+"Room"+ mViewModel.getCode().getValue());
                DatabaseReference start = curRoom.child("start");
                start.setValue(1);

                // Use the start button in wait room to translate to the quiz activity
                // Qinghang 12/08 transfer room id to next activity
                // Haochen 12/9 add problem set name, and time per questions data when
                // transfer to other activities
                Intent intent=new Intent(getActivity(), QuizActivity.class);

                Bundle data = new Bundle();
                data.putInt("room_id", mViewModel.getCode().getValue());
                data.putString("problemSet_name", mViewModel.getProbSetName().getValue());
                data.putInt("time_per_ques", mViewModel.getmGame().getValue().getTimePerQuestion());
                intent.putExtras(data);

                startActivity(intent);
            }
        });
        // TODO: Use the ViewModel
    }

}