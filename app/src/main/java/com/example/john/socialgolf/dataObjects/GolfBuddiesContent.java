package com.example.john.socialgolf.dataObjects;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class GolfBuddiesContent {

    public static final List<GolfBuddiesContent.GolfBuddiesItem> ITEMS = new ArrayList<GolfBuddiesContent.GolfBuddiesItem>();
    private static final String TAG = "ListFriends";

    public static void addItem(GolfBuddiesContent.GolfBuddiesItem item) {
        ITEMS.add(item);
    }

    public static class GolfBuddiesItem {

        public final String name;
        public final Uri picture;
        public final String email;

        public GolfBuddiesItem(String name, Uri picture, String email) {

            this.name = name;
            this.picture = picture;
            this.email = email;

        }

        @Override
        public String toString() {return "(" + this.name + "," + this.email + ")";

        }
    }
}
