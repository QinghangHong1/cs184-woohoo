package edu.ucsb.cs.cs184.qhong.woohoo.utils;

import java.util.ArrayList;

public class FriendGroup {
    private String groupName;
    private ArrayList<User> friends;

    public String getGroupName() {
        return groupName;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
