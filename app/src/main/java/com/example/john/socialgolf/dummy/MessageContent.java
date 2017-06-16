package com.example.john.socialgolf.dummy;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MessageContent {

    public static final List<MessageContent.MessageItem> ITEMS = new ArrayList<MessageItem>();

    public static void addItem(MessageItem item) {
        ITEMS.add(item);
    }
    static {
        addItem(new MessageContent.MessageItem("Phil Backers", "hey"));
        addItem(new MessageContent.MessageItem("Enis Guzelaydin", "sup"));
        addItem(new MessageItem("Mike Harrignton", "howdy"));
        addItem(new MessageItem("Matt Mitchell", "holla"));
        addItem(new MessageItem("Nick St. Onge", "whats up?"));
        addItem(new MessageItem("Vince Klein", "Sounds good"));
    }

    public static class MessageItem {

        public final String name;
        public final String lastMessage;

        public MessageItem(String name, String lastMessage) {

            this.name = name;
            this.lastMessage = lastMessage;

        }

        @Override
        public String toString() {return "(" + this.name + "," + this.lastMessage + ")";

        }
    }
}
