package com.example.john.socialgolf.dataObjects;

import com.example.john.socialgolf.dataObjects.Friends;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TeeTimeItem {

    public String _key;
    public String teeTimeDate;
    public String teeTimeTime;
    public List<Friends> groupMembers;
    public String booked;
    public String course;
    public String owner;

    public TeeTimeItem(String teeTimeDate, List<Friends> groupMembers, String booked, String course, String _key, String owner) {
        this.teeTimeDate = teeTimeDate;

        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        this.teeTimeTime = localDateFormat.format(teeTimeDate);

        this.groupMembers = new ArrayList<Friends>();
        this.groupMembers = groupMembers;
        this.booked = booked;
        this.course = course;
        this._key = _key;
        this.owner = owner;

    }

    public TeeTimeItem(){

    }
}
