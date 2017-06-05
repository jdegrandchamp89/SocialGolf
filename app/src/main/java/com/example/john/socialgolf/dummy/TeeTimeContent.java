package com.example.john.socialgolf.dummy;

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
public class TeeTimeContent {

    public static final List<TeeTimeItem> ITEMS = new ArrayList<TeeTimeItem>();

    public static void addItem(TeeTimeItem item) {
        ITEMS.add(item);
    }
    static {
        addItem(new TeeTimeItem(new Date(), 4, "X", "Sylvan Glen"));
        addItem(new TeeTimeItem(new Date(), 2, " ", "Sanctuary Lake"));
        addItem(new TeeTimeItem(new Date(), 3, "X", "The Mines"));
        addItem(new TeeTimeItem(new Date(), 4, " ", "The Meadows"));
        addItem(new TeeTimeItem(new Date(), 4, "X", "Sylvan Glen"));
        addItem(new TeeTimeItem(new Date(), 2, " ", "Sanctuary Lake"));
        addItem(new TeeTimeItem(new Date(), 3, "X", "The Mines"));
        addItem(new TeeTimeItem(new Date(), 4, " ", "The Meadows"));
        addItem(new TeeTimeItem(new Date(), 4, "X", "Sylvan Glen"));
        addItem(new TeeTimeItem(new Date(), 2, " ", "Sanctuary Lake"));
        addItem(new TeeTimeItem(new Date(), 3, "X", "The Mines"));
        addItem(new TeeTimeItem(new Date(), 4, " ", "The Meadows"));
    }

    public static class TeeTimeItem {

        public final Date teeTimeDate;
        public final String teeTimeTime;
        public final Integer groupMembers;
        public final String booked;
        public final String course;

        public TeeTimeItem(Date teeTimeDate, Integer groupMembers, String booked, String course) {
            this.teeTimeDate = teeTimeDate;

            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
            this.teeTimeTime = localDateFormat.format(teeTimeDate);

            this.groupMembers = groupMembers;
            this.booked = booked;
            this.course = course;

        }

        @Override
        public String toString() {return "(" + this.teeTimeDate + "," + this.teeTimeTime + ")";

        }
    }
}
