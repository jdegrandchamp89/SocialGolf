package com.example.john.socialgolf.dataObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJDY on 6/15/2017.
 */

public class Messages {

    public String message;
    public String timestamp;
    public String sender;

    public Messages(){

    }

    public Messages(String message, String timestamp, String sender){
        this.message = message;
        this.timestamp = timestamp;
        this.sender = sender;
    }
}
