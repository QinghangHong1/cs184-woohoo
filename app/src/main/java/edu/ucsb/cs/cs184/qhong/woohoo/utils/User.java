package edu.ucsb.cs.cs184.qhong.woohoo.utils;

import java.util.ArrayList;

public class User {
    private String uid;
    private String name;
    private String email;
    private String icon;
    private ArrayList<FriendGroup> friends;


    public User(){
        name = "";
        email = "";
        icon = "";
        uid = "";
        friends = new ArrayList<>();
    }

    public ArrayList<FriendGroup> getFriends() {
        return friends;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFriends(ArrayList<FriendGroup> friends) {
        this.friends = friends;
    }
}
