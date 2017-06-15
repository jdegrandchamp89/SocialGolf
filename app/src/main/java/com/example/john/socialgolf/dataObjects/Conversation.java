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

//    public String get_key() {
//        return _key;
//    }
//
//    public void set_key(String _key) {
//        this._key = _key;
//    }
//
//    public String getOwner() {
//        return owner;
//    }
//
//    public void setOwner(String owner) {
//        this.owner = owner;
//    }
//
//    public String getLastMessage() {
//        return lastMessage;
//    }
//
//    public void setLastMessage(String lastMessage) {
//        this.lastMessage = lastMessage;
//    }
//
//    public List<Friends> getGroupMembers(){
//        return groupMembers;
//    }
//
//    public void setGroupMembers(List<Friends> groupMembers) {
//        this.groupMembers = groupMembers;
//    }

}
