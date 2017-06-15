package com.example.john.socialgolf.dataObjects;

import org.parceler.Parcel;

/**
 * Created by John on 6/11/2017.
 */
@Parcel
public class Friends {

    public String uid;

    public Friends(){

    }

    public Friends(String uid){
        this.uid = uid;
    }
}
