package com.example.homeshare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    public String uid;
    public String name;
    public String bio;
    public String instagram;
    public String email;
    public Gender gender;
    public Preferences preferences;
    public boolean lightTheme;

    public User(String uid,
                String name,
                String bio,
                String instagram,
                String email,
                Gender gender,
                boolean alcoholic,
                boolean smoker,
                boolean social,
                boolean nightOwl) {
        this.uid = uid;
        this.name = name;
        this.bio = bio;
        this.instagram = instagram;
        this.email = email;
        this.gender = gender;

        // Set list properties
        this.preferences = new Preferences();

        // Set preferences
        this.preferences.alcoholic = alcoholic;
        this.preferences.smoker = smoker;
        this.preferences.social = social;
        this.preferences.nightOwl = nightOwl;
    }

    public String getAnnotatedName() {
        if (this.uid.equals(Auth.currentUser.uid)) {
            return this.name + " (You)";
        } else {
            return this.name;
        }
    }

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    public class Preferences{
        boolean alcoholic;
        boolean smoker;
        boolean social;
        boolean nightOwl;
        boolean smallGroup;
        boolean largeGroup;
        boolean cheapRoom;
        boolean expensiveRoom;

        public List<Integer> getResourceList() {
            List<Integer> resourceList = new ArrayList<Integer>();
            if (alcoholic)
                resourceList.add(R.drawable.ic_round_local_drink_24);
            if (smoker)
                resourceList.add(R.drawable.ic_baseline_smoking_rooms_24);
            if (social)
                resourceList.add(R.drawable.ic_baseline_sports_kabaddi_24);
            if (nightOwl)
                resourceList.add(R.drawable.ic_baseline_nights_stay_24);

            return resourceList;
        }
    }
}
