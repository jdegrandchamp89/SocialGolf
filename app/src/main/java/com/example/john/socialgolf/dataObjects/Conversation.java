package com.example.john.socialgolf.dataObjects;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJDY on 6/15/2017.
 */

@Parcel
public class Conversation {

    public String _key;
    public String owner;
    public String lastMessage;
    public List<Friends> groupMembers;

    public Conversation(){

    }

    public Conversation(String _key, String owner, String lastMessage, List<Friends> groupMembers){
        this._key = _key;
        this.owner = owner;
        this.lastMessage = lastMessage;

        this.groupMembers = new ArrayList<Friends>();
        this.groupMembers = groupMembers;
    }
    
}
