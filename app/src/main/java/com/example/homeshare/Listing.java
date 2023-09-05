package com.example.homeshare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Listing {
    public static Listing active;
    private static SimpleDateFormat dateFormat;

    public String listingID;
    public User postedBy;
    public String address;
    public List<Invitation> invitations;
    public List<User> roommates;
    public int maxRoommates;
    public Date expiration;
    public String description;
    public LinearLayout view;

    public int rent;

    static {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    public static Date parseDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String dateString(Date date) {
        return dateFormat.format(date);
    }

    public Listing(String listingID,
                   User postedBy,
                   String address,
                   int rent,
                   int maxRoommates,
                   Date expiration,
                   String description) {
        // Set arg properties
        this.listingID = listingID;
        this.postedBy = postedBy;
        this.address = address;
        this.rent = rent;
        this.maxRoommates = maxRoommates;
        this.expiration = expiration;
        this.description = description;

        // Set list properties
        this.invitations = new ArrayList<Invitation>();
        this.roommates = new ArrayList<User>();
    }

    public View updateView(MainActivity mainActivity) {
        LayoutInflater inflater = mainActivity.getLayoutInflater();
        this.view = (LinearLayout) inflater.inflate(R.layout.view_listing, null);

        // Get children
        TextView address = (TextView) this.view.getChildAt(0);
        Chip rent = (Chip) this.view.getChildAt(1);
        Chip roommates = (Chip) this.view.getChildAt(2);
        LinearLayout postedBy = (LinearLayout) this.view.getChildAt(3);
        TextView posterBio = (TextView) this.view.getChildAt(4);
        ChipGroup preferences = (ChipGroup) this.view.getChildAt(5);
        Chip submit = (Chip) this.view.getChildAt(6);
        TextView description = (TextView) this.view.getChildAt(7);
        // Get grandchildren
        ImageView genderIcon = (ImageView) postedBy.getChildAt(0);
        TextView posterName = (TextView) postedBy.getChildAt(1);

        // Update fields
        address.setText(this.address);
        String rentValue = String.valueOf(this.rent);
        rent.setText("$"+ rentValue);
        roommates.setText(this.roommates.size() + " / " + this.maxRoommates);
        posterBio.setText(this.postedBy.bio);
        posterName.setText(this.postedBy.getAnnotatedName());
        description.setText(this.description);
        if (this.postedBy.gender == User.Gender.FEMALE) {
            genderIcon.setImageResource(R.drawable.ic_icons8_female_120__xxxhdpi_);
            genderIcon.setColorFilter(ThemeManager.getThemeColor(R.color.accent_secondary));
            submit.setChipBackgroundColorResource(R.color.accent_secondary);
        } else if (this.postedBy.gender == User.Gender.OTHER) {
            genderIcon.setImageResource(R.drawable.ic_icons8_neuter_120__xxxhdpi_);
            genderIcon.setColorFilter(ThemeManager.getThemeColor(R.color.accent_tertiary));
            submit.setChipBackgroundColorResource(R.color.accent_tertiary);
        }
        for (int resourceID : this.postedBy.preferences.getResourceList()) {
            Chip filter = (Chip) inflater.inflate(R.layout.view_listing_filter, null);
            filter.setChipIconResource(resourceID);
            preferences.addView(filter);
        }

        ThemeManager.loadTheme(this.view);

        // Set Tag to Reference
        this.view.setTag((String)this.address);

        // Set an OnClickListener for expanding feature
        this.view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Listing.active = Listing.this;
                mainActivity.OpenListing(false);
            }
        });

        return this.view;
    }

    // Validation functions
    public static boolean validateAddress(String address) {
        return address.indexOf(' ') > 1 && address.length() > 5;
    }
    public static boolean validateRent(String rent) {
        try {
            int rentValue = Integer.parseInt(rent);
            return rentValue > 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean validateRoommates(String roommates) {
        try {
            int roommatesValue = Integer.parseInt(roommates);
            return roommatesValue > 1 && roommatesValue < 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean validateExpiration(String expiration) {
        try {
            return dateFormat.parse(expiration).after(new Date());
        } catch (ParseException e) {
            return false;
        }
    }
    public static boolean validateDescription(String description) {
        return description.length() > 5 && description.length() < 100;
    }
}
