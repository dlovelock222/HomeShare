package com.example.homeshare;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Database {
    private static DatabaseReference mDatabase;
    private static Map<String, User> userCache;
    private static Map<String, Listing> listingCache;

    static {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userCache = new HashMap<String, User>();
        listingCache = new HashMap<String, Listing>();
    }

    public static void writeUser(User user) {
        DatabaseReference userObj = mDatabase.child("users").child(user.uid);
        userObj.child("name").setValue(user.name);
        userObj.child("email").setValue(user.email);
        userObj.child("ig").setValue(user.instagram);
        userObj.child("bio").setValue(user.bio);
        if (user.gender == User.Gender.MALE) {
            userObj.child("gender").setValue("male");
        } else if (user.gender == User.Gender.FEMALE) {
            userObj.child("gender").setValue("female");
        } else {
            userObj.child("gender").setValue("other");
        }
        userObj.child("alcoholic").setValue(user.preferences.alcoholic);
        userObj.child("smoker").setValue(user.preferences.smoker);
        userObj.child("social").setValue(user.preferences.social);
        userObj.child("nightOwl").setValue(user.preferences.nightOwl);
        userObj.child("lightTheme").setValue(user.lightTheme);

        if (userCache.containsKey(user.uid)) {
            User cachedUser = userCache.get(user.uid);
            cachedUser.name = user.name;
            cachedUser.email = user.email;
            cachedUser.instagram = user.instagram;
            cachedUser.bio = user.bio;
            cachedUser.gender = user.gender;
            cachedUser.preferences.alcoholic = user.preferences.alcoholic;
            cachedUser.preferences.smoker = user.preferences.smoker;
            cachedUser.preferences.social = user.preferences.social;
            cachedUser.preferences.nightOwl = user.preferences.nightOwl;
            cachedUser.lightTheme = user.lightTheme;
        }
    }

    public static void writeListing(Listing listing) {
        DatabaseReference listingObj = mDatabase.child("listings").child(listing.listingID);
        listingObj.child("address").setValue(listing.address);
        listingObj.child("description").setValue(listing.description);
        listingObj.child("expiration").setValue(Listing.dateString(listing.expiration));
        listingObj.child("postedBy").setValue(listing.postedBy.uid);
        listingObj.child("maxRoommates").setValue(listing.maxRoommates);
        listingObj.child("rent").setValue(listing.rent);
        for (User u : listing.roommates) {
            listingObj.child("roommates").child(u.uid).setValue(true);
        }

        listingCache.put(listing.listingID, listing);
    }

    public static void deleteListing(String listingID, Callback cb) {
        mDatabase.child("listings").child(listingID).removeValue().addOnCompleteListener(
            new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listingCache.remove(listingID);
                    cb.callback(null);
                }
            }
            }
        );
    }

    public static void writeInvitation(Invitation invitation) {
        DatabaseReference invObj = mDatabase.child("listings").child(invitation.listing.listingID).child("invitations");
        invObj.child(invitation.invitationID).child("status").setValue(invitation.status.toString());
        invObj.child(invitation.invitationID).child("message").setValue(invitation.message);
        invObj.child(invitation.invitationID).child("sender").setValue(invitation.sender.uid);
    }

    public static void writeRoommate(Listing listing, User user) {
        DatabaseReference listingObj = mDatabase.child("listings").child(listing.listingID);
        listingObj.child("roommates").child(user.uid).setValue(true);

        listingCache.get(listing.listingID).roommates.add(user);
    }

    public static void deleteRoommate(Listing listing, User user) {
        DatabaseReference listingObj = mDatabase.child("listings").child(listing.listingID);
        listingObj.child("roommates").child(user.uid).setValue(true);

        listingCache.get(listing.listingID).roommates.remove(user);
    }

    public static void readUser(String userID, Callback<User> cb) {
        // Check if user is in userCache
        if (Database.userCache.containsKey(userID)) {
            cb.callback(Database.userCache.get(userID));
        } else {
            // Update userCache
            updateUserCache(new Callback() {
            public void callback(Object o) {
                cb.callback(Database.userCache.get(userID));
            }
            });
        }
    }

    public static Map<String, Listing> readListingCache() {
        // WARNING: this may return outdated listings
        return Database.listingCache;
    }

    public static void readListing(String listingID, Callback<Listing> cb) {
        // Check if listing is in listingCache
        if (Database.listingCache.containsKey(listingID)) {
            cb.callback(Database.listingCache.get(listingID));
        } else {
            // Update listingCache
            updateListingCache(new Callback() {
            public void callback(Object o) {
                cb.callback(Database.listingCache.get(listingID));
            }
            });
        }
    }

    public static void readInbox(String userID, Callback<ArrayList<Invitation>> cb) {
        Callback initInbox = new Callback() {
        public void callback(Object o) {
            ArrayList<Invitation> inbox = new ArrayList<Invitation>();
            for (String listingID : listingCache.keySet()) {
                if (listingCache.get(listingID).postedBy.uid.equals(userID)) {
                    // Add corresponding invitations to inbox
                    for (Invitation invitation : listingCache.get(listingID).invitations) {
                        inbox.add(invitation);
                    }
                }
            }
            cb.callback(inbox);
        }
        };

        // Update listings if empty
        if (listingCache.isEmpty()) {
            updateListingCache(initInbox);
        } else {
            initInbox.callback(null);
        }
    }

    public static void readOutbox(String userID, Callback<ArrayList<Invitation>> cb) {
        Callback initOutbox = new Callback() {
            public void callback(Object o) {
                ArrayList<Invitation> outbox = new ArrayList<Invitation>();
                for (String listingID : listingCache.keySet()) {
                    for (Invitation invitation : listingCache.get(listingID).invitations) {
                        if (invitation.sender.uid.equals(userID)) {
                            outbox.add(invitation);
                        }
                    }
                }
                cb.callback(outbox);
            }
        };

        // Update listings if empty
        if (listingCache.isEmpty()) {
            updateListingCache(initOutbox);
        } else {
            initOutbox.callback(null);
        }
    }

    public static void updateUserCache(Callback<Map<String, User>> cb) {
        mDatabase.child("users").get().addOnCompleteListener(
        new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Map<String, Object>> users = (Map<String, Map<String, Object>>) task.getResult().getValue();
                    for (String userID : users.keySet()) {
                        Map<String, Object> userObj = users.get(userID);

                        String name = String.valueOf(userObj.get("name"));
                        String bio = String.valueOf(userObj.get("bio"));
                        String ig = String.valueOf(userObj.get("ig"));
                        String email = String.valueOf(userObj.get("email"));
                        String genderString = String.valueOf(userObj.get("gender"));
                        User.Gender gender = User.Gender.MALE;

                        boolean alcoholic = userObj.containsKey("alcoholic") && (boolean) userObj.get("alcoholic");
                        boolean smoker = userObj.containsKey("smoker") && (boolean) (boolean) userObj.get("smoker");
                        boolean social = userObj.containsKey("social") && (boolean) (boolean) userObj.get("social");
                        boolean nightOwl = userObj.containsKey("nightOwl") && (boolean) (boolean) userObj.get("nightOwl");

                        if (genderString.equals("female")) {
                            gender = User.Gender.FEMALE;
                        } else if (genderString.equals("other")) {
                            gender = User.Gender.OTHER;
                        }

                        User user = new User(userID, name, bio, ig, email, gender,
                                alcoholic, smoker, social, nightOwl);
                        user.lightTheme = userObj.containsKey("lightTheme") && (boolean) userObj.get("lightTheme");
                        Database.userCache.put(userID, user);
                    }
                    cb.callback(userCache);
                }
            }
        }
        );
    }

    public static void updateListingCache(Callback<Map<String, Listing>> cb) {
        // First update user cache
        Database.updateUserCache(new Callback() {
            public void callback(Object o) {
                // Read the listings from database
                mDatabase.child("listings").get().addOnCompleteListener(
                        new OnCompleteListener<DataSnapshot>() {
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Map<String, Object> listings = (Map<String, Object>) task.getResult().getValue();

                                    // No listings
                                    if (listings == null) {
                                        listingCache.clear();
                                        cb.callback(listingCache);
                                        return;
                                    }

                                    for (String listingID : listings.keySet()) {
                                        Map<String, Object> listingObj = (Map<String, Object>) listings.get(listingID);
                                        String address = String.valueOf(listingObj.get("address"));
                                        Date expiration = Listing.parseDate(String.valueOf(listingObj.get("expiration")));
                                        String description = String.valueOf(listingObj.get("description"));
                                        int maxRoommates = ((Long) listingObj.get("maxRoommates")).intValue();
                                        int rent = 1000;
                                        if (listingObj.get("rent") != null) {
                                            rent = ((Long) listingObj.get("rent")).intValue();
                                        }

                                        // Owner
                                        String postedByID = String.valueOf(listingObj.get("postedBy"));
                                        User postedBy = Database.userCache.get(postedByID);

                                        // Create listing
                                        Listing listing = new Listing(listingID, postedBy, address, rent, maxRoommates, expiration, description);

                                        Set<String> roommateIDs = ((Map<String, Object>) listingObj.get("roommates")).keySet();
                                        for (String roommateID : roommateIDs) {
                                            listing.roommates.add(Database.userCache.get(roommateID));
                                        }

                                        // Invitations
                                        if (listingObj.containsKey("invitations")) {
                                            Map<String, Map<String, Object>> invitationObjs = ((Map<String, Map<String, Object>>) listingObj.get("invitations"));
                                            for (String invitationID : invitationObjs.keySet()) {
                                                String statusObj = String.valueOf(invitationObjs.get(invitationID).get("status"));
                                                Invitation.Status status = Invitation.Status.valueOf(statusObj);
                                                String message = String.valueOf(invitationObjs.get(invitationID).get("message"));
                                                String sender = String.valueOf(invitationObjs.get(invitationID).get("sender"));

                                                listing.invitations.add(new Invitation(listing, invitationID, status, message, Database.userCache.get(sender)));
                                            }
                                        }
                                        listingCache.put(listingID, listing);
                                    }
                                    cb.callback(listingCache);
                                }
                            }
                        });
            }
        });
    }
}
