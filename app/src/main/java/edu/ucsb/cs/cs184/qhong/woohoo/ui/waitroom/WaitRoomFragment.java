package edu.ucsb.cs.cs184.qhong.woohoo.ui.waitroom;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.ucsb.cs.cs184.qhong.woohoo.FindViewModel;
import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.SettingViewModel;

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
//
        TextView textCode = getActivity().findViewById(R.id.textCode);
        textCode.setText("Room ID:"+mViewModel.getCode().getValue());
        TextView textName = getActivity().findViewById(R.id.textName);
        textName.setText("Problem Set Name:"+mViewModel.getProbSetName().getValue());
        // TODO: Use the ViewModel
    }

}