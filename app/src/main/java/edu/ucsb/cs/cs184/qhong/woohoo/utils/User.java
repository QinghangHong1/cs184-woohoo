package edu.ucsb.cs.cs184.qhong.woohoo.utils;

import java.util.ArrayList;

public class User {
    private String name;
    private String email;
    private String icon;


    public User(){
        name = "";
        email = "";
        icon = "";
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
}
