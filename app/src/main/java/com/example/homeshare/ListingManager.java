package com.example.homeshare;

import android.view.View;
import android.widget.LinearLayout;


import com.google.android.material.chip.Chip;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ListingManager {
    public static ListingManager instance;
    public MainActivity mainActivity;
    private static Vector<Listing> currentlyDisplayedListings;
    public static Map<String, Invitation> inbox;


    public static ListingManager init(MainActivity mainActivity) {
        ListingManager.instance = new ListingManager(mainActivity, false, false,false,false, false, false, false, false);
        currentlyDisplayedListings = new Vector<Listing>();
        inbox = new HashMap<String, Invitation>();
        return ListingManager.instance;
    }

    public Vector<Listing> getCurrentlyDisplayedListings(){
        return currentlyDisplayedListings;
    }

    public ListingManager(MainActivity mainActivity,
                          boolean queryAlcoholic,
                          boolean querySmoker,
                          boolean querySocial,
                          boolean queryNightOwl,
                          boolean smallGroup,
                          boolean largeGroup,
                          boolean cheapRoom,
                          boolean largeRoom) {
        this.mainActivity = mainActivity;

        // Get Listings from Database (Temporary)
        // Idea is to retrieve a query from Firebase DB
        Database.updateListingCache(new Callback<Map<String, Listing>>() {
            public void callback(Map<String, Listing> listings) {
                ListingManager.this.updateViewsFromMap(listings,
                        queryAlcoholic,
                        querySmoker,
                        querySocial,
                        queryNightOwl,
                        smallGroup,
                        largeGroup,
                        cheapRoom,
                        largeRoom);
            }
        });

    }

    public void updateViewsFromMap(Map<String, Listing> listings,
                                   boolean queryAlcoholic,
                                   boolean querySmoker,
                                   boolean querySocial,
                                   boolean queryNightOwl,
                                   boolean smallGroup,
                                   boolean largeGroup,
                                   boolean cheapRoom,
                                   boolean largeRoom
                                   ) {
        // Reset listings
        ListingManager.this.mainActivity.resetViews(MainActivity.Tab.DISCOVER);
        currentlyDisplayedListings.clear();
        // Adds Listing to DiscoverView
        for (Listing listing : listings.values()) {
            boolean valid = true;
            User user = listing.postedBy;

            if (queryAlcoholic && !user.preferences.alcoholic) valid = false;
            else if (querySmoker && !user.preferences.smoker) valid = false;
            else if (querySocial && !user.preferences.social) valid = false;
            else if (queryNightOwl && !user.preferences.nightOwl) valid = false;

            else if (smallGroup && listing.maxRoommates > 6) valid = false;
            else if (largeGroup && listing.maxRoommates < 7) valid = false;
            else if (cheapRoom && listing.rent > 1000) valid = false;
            else if (largeRoom && listing.rent < 1000) valid = false;

            if (valid) {
                ListingManager.this.mainActivity.addListingView(listing);
                currentlyDisplayedListings.add(listing);
            }
        }


        // Reset inbox
        ListingManager.this.mainActivity.resetViews(MainActivity.Tab.INBOX);
        // Read inbox
        Database.readInbox(Auth.currentUser.uid, new Callback<ArrayList<Invitation>>() {
            public void callback(ArrayList<Invitation> invitations) {
                inbox.clear();
                int sentCount = 0;
                for (Invitation invitation : invitations) {
                    if (invitation.status != Invitation.Status.REJECTED) {
                        ListingManager.this.mainActivity.addInvitationView(invitation, true);
                        inbox.put(invitation.sender.email, invitation);

                        if (invitation.status == Invitation.Status.SENT) {
                            sentCount++;
                        }
                    }
                }

                Chip notificationChip = mainActivity.findViewById(R.id.NotificationChip);
                if (sentCount > 0) {
                    notificationChip.setText(String.valueOf(sentCount));
                    notificationChip.setVisibility(View.VISIBLE);
                } else {
                    notificationChip.setVisibility(View.GONE);
                }
            }
        });

        // Read outbox
        Database.readOutbox(Auth.currentUser.uid, new Callback<ArrayList<Invitation>>() {
            public void callback(ArrayList<Invitation> invitations) {
                for (Invitation invitation : invitations) {
                    ListingManager.this.mainActivity.addInvitationView(invitation, false);
                }
            }
        });
    }
}
