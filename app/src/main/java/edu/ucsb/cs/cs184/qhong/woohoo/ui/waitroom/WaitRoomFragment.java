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

public class WaitRoomFragment extends Fragment {

    private WaitRoomViewModel mViewModel;

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
        mViewModel = new ViewModelProvider(this).get(WaitRoomViewModel.class);
        // TODO: Use the ViewModel
    }

}