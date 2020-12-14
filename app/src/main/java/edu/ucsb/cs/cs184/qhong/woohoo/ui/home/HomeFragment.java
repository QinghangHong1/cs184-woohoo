package edu.ucsb.cs.cs184.qhong.woohoo.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.qhong.woohoo.FindActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.SettingActivity;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.Player;

//YZ2nd:HomeFragment
//GOOD Quiz, create Room button, find room button.
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        final Button createRoom = root.findViewById(R.id.button_create);
        final Button findRoom = root.findViewById(R.id.button_find);

        homeViewModel.getmText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        homeViewModel.getButtonCreateText().observe(getViewLifecycleOwner(),new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                createRoom.setText(s);
            }
        });
        homeViewModel.getButtonFindText().observe(getViewLifecycleOwner(),new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                findRoom.setText(s);
            }
        });
        createRoom.setOnClickListener(new LocationCheckedListener());  //这一行是在将button和监听器捆绑
        findRoom.setOnClickListener(new LocationCheckedListener2());  //这一行是在将button和监听器捆绑
        ArrayList<Player> test = new ArrayList<>();


        return root;
    }

//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        view.findViewById(R.id.button_create).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(HomeFragment.this)
//                        .navigate(R.id.action_homeFragment_to_setting);
//            }
//        });
//    }
//
    class LocationCheckedListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), SettingActivity.class);  //从前者跳到后者，特别注意的是，在fragment中，用getActivity()来获取当前的activity
            getActivity().startActivity(intent);
        }
    }

    class LocationCheckedListener2 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), FindActivity.class);  //从前者跳到后者，特别注意的是，在fragment中，用getActivity()来获取当前的activity
            getActivity().startActivity(intent);
        }
    }



}