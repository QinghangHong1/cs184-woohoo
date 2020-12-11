package edu.ucsb.cs.cs184.qhong.woohoo.ui.friend;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.qhong.woohoo.R;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.FriendGroup;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.MyAdapter;
import edu.ucsb.cs.cs184.qhong.woohoo.utils.User;

public class FriendFragment extends Fragment {

    private FriendViewModel friendViewModel;

    private String[] groupNames = new String[]{"女朋友", "宠物", "基友", "小弟"};
    private String[][] friendNames = new String[][]{{"苍井空", "波多野结衣", "小泽玛莉亚", "龙泽罗拉"},
            {"草泥马", "雅蠛蝶", "法克鱿"},
            {"小张", "小杨", "小洪", "小李", },
            {"奥巴驴", "小学僧"}};
    private ArrayList<FriendGroup> groups = new ArrayList<>();
    private MyAdapter mAdapter;
    private ExpandableListView expandableListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        friendViewModel =
                ViewModelProviders.of(this).get(FriendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_friend, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        expandableListView = getActivity().findViewById(R.id.expandableView);
        initList();
        initListener();
        mAdapter = new MyAdapter(groups,getContext());
        expandableListView.setAdapter(mAdapter);

        final Button addButton = getActivity().findViewById(R.id.searchUser);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                LinearLayout searchBar = getActivity().findViewById(R.id.userSearchBar);
                searchBar.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.GONE);
                SearchView userSearch = getActivity().findViewById(R.id.userSearchText);
                userSearch.setIconified(false);
                userSearch.setSubmitButtonEnabled(true);
                userSearch.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        LinearLayout searchBar = getActivity().findViewById(R.id.userSearchBar);
                        searchBar.setVisibility(View.GONE);
                        addButton.setVisibility(View.VISIBLE);
                        return false;
                    }
                });
                userSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        Toast.makeText(getContext(), "老子没写呢！！", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                });
            }});
    }

    public void initList(){
        for(int i = 0;i < groupNames.length;i++){
            FriendGroup temp = new FriendGroup();
            temp.setGroupName(groupNames[i]);
            ArrayList<User> list = new ArrayList<>();
            for(int j = 0;j < friendNames[i].length;j++){
                User u = new User();
                u.setName(friendNames[i][j]);
                list.add(u);
            }
            temp.setFriends(list);
            groups.add(temp);
        }
    }
    public void initListener(){
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Toast.makeText(getContext(), "老子没写呢！！", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                Toast.makeText(getContext(), "默认好友", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}