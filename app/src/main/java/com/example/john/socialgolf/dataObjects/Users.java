package com.example.john.socialgolf.dataObjects;

import java.util.ArrayList;

/**
 * Created by John on 6/10/2017.
 */

public class Users {

    public String uid;
    public String name;
    public String email;
    public String picture;
    public String aboutMe;
    public ArrayList<String> notificationTokens;

    public  Users(){

    }

    public Users(String uid, String name, String email, String picture, String aboutMe, ArrayList<String> notificationTokens) {

        this.uid = uid;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.aboutMe = aboutMe;
        this.notificationTokens = notificationTokens;

    }

}
