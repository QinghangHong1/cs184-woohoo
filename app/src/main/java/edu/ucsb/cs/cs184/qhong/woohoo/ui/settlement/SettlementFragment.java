package edu.ucsb.cs.cs184.qhong.woohoo.ui.settlement;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import edu.ucsb.cs.cs184.qhong.woohoo.MainActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.QuizActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.R;

public class SettlementFragment extends Fragment {

    private SettlementViewModel mViewModel;

    public static SettlementFragment newInstance() {
        return new SettlementFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settlement_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettlementViewModel.class);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

//        FloatingActionButton fab = getActivity().findViewById(R.id.buttonBackToHome);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Use the start button in wait room to translate to the quiz activity
//                Intent intent=new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }

}