package edu.ucsb.cs.cs184.qhong.woohoo.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.qhong.woohoo.R;

public class MyAdapter extends BaseExpandableListAdapter {
    private ArrayList<FriendGroup> groups;
    private Context mContext;

    public MyAdapter(ArrayList<FriendGroup> groups, Context context){
        this.groups = groups;
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return groups.get(i).getFriends().size();
    }

    @Override
    public Object getGroup(int i) {
        return groups.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return groups.get(i).getFriends().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    class ViewHolder{
        private TextView name;
        private ImageView icon;

        public ViewHolder(View view){
            name = view.findViewById(R.id.userName);
            icon = view.findViewById(R.id.userIcon);
        }
    }
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if(view == null){
            view = View.inflate(mContext,R.layout.group_view,null);
        }
        TextView groupName = view.findViewById(R.id.friendGroup);
        groupName.setText(groups.get(i).getGroupName());
        if(b){
            groupName.setTextColor(Color.GREEN);
        }else{
            groupName.setTextColor(Color.BLACK);
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view = View.inflate(mContext, R.layout.child_view, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        User user = groups.get(i).getFriends().get(i1);
        holder.name.setText(user.getName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
