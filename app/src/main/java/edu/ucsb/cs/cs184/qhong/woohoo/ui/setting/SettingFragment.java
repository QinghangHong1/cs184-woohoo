package edu.ucsb.cs.cs184.qhong.woohoo.ui.setting;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.SettingViewModel;

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

        settingViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);

        final TextView timePerQuestion = getActivity().findViewById(R.id.inputTimePerQues);

        Button settingCreateButton = getActivity().findViewById(R.id.settingCreateButton);
        settingCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int time = Integer.parseInt(timePerQuestion.getText().toString());
                settingViewModel.setTimePerQuestion(time);
                settingViewModel.update();
                NavHostFragment.findNavController(SettingFragment.this)
                        .navigate(R.id.action_setting_to_waitRoomFragment);
            }
        });
        // TODO: Use the ViewModel
    }



}