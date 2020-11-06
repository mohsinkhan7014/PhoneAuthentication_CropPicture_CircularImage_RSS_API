package com.example.haqdarshak_assignment.rssfeed.module;

import java.util.List;

public class RSSOBJECT {

        public String status;
        public Feed feed;
        public List<Item> items;
        public RSSOBJECT(String status, Feed feed, List<Item> items)
        {
            this.status=status;
            this.feed=feed;
            this.items=items;
        }

        public String getStatus() {
            return status;
        }

        public Feed getFeed() {
            return feed;
        }

        public List<Item> getItems() {
            return items;
        }

    public class Enclosure{
        public String link;
    }


}
