package com.example.john.socialgolf.dummy;

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
public class GolfBuddiesContent {

    public static final List<GolfBuddiesContent.GolfBuddiesItem> ITEMS = new ArrayList<GolfBuddiesContent.GolfBuddiesItem>();

    public static void addItem(GolfBuddiesContent.GolfBuddiesItem item) {
        ITEMS.add(item);
    }
    static {
        addItem(new GolfBuddiesContent.GolfBuddiesItem("Phil Backers", 54));
        addItem(new GolfBuddiesContent.GolfBuddiesItem("Enis Guzelaydin", 26));
        addItem(new GolfBuddiesContent.GolfBuddiesItem("Mike Harrignton", 47));
        addItem(new GolfBuddiesContent.GolfBuddiesItem("Matt Mitchell", 43));
        addItem(new GolfBuddiesContent.GolfBuddiesItem("Nick St. Onge", 21));
        addItem(new GolfBuddiesContent.GolfBuddiesItem("Vince Klein", 3));
    }

    public static class GolfBuddiesItem {

        public final String name;
        public final Integer roundsPlayed;

        public GolfBuddiesItem(String name, Integer roundsPlayed) {

            this.name = name;
            this.roundsPlayed = roundsPlayed;

        }

        @Override
        public String toString() {return "(" + this.name + "," + this.roundsPlayed + ")";

        }
    }
}
