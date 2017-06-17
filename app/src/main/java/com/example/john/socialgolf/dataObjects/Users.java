package com.example.john.socialgolf.dataObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 6/10/2017.
 */

public class Users {

    public String uid;
    public String name;
    public String email;
    public String picture;

    public  Users(){

    }

    public Users(String uid, String name, String email, String picture) {

        this.uid = uid;
        this.name = name;
        this.email = email;
        this.picture = picture;

    }

}
