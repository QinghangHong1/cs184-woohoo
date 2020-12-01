package edu.ucsb.cs.cs184.qhong.woohoo.ui.waitroom;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsb.cs.cs184.qhong.woohoo.R;
//YZ2nd: wait room for students
public class StudentWaitingFragment extends Fragment {

    private StudentWaitingViewModel mViewModel;

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
        mViewModel = new ViewModelProvider(this).get(StudentWaitingViewModel.class);
        // TODO: Use the ViewModel
    }

}