package com.example.homeshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static MainActivity instance;
    public Tab currentTab = Tab.DISCOVER;
    public Map<Tab, ImageButton> tabButtons;
    private Map<Tab, ScrollView> tabViews;
    private Map<Tab, LinearLayout> tabLayouts;

    public ListingManager listingManager;
    private int numListings;
    private int numInbox;

    enum Tab {
        DISCOVER,
        INBOX,
        PROFILE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        Auth.init(MainActivity.this);

        if (Auth.mAuth.getCurrentUser() != null) {
            this.initViews();
            this.setOnClickListeners();
            this.listingManager = ListingManager.init(MainActivity.this);
        }

        ThemeManager.loadTheme(findViewById(android.R.id.content));
    }

    // Helper Function
    protected void initViews() {
        this.tabButtons = new HashMap<Tab, ImageButton>();
        this.tabButtons.put(Tab.DISCOVER, (ImageButton) findViewById(R.id.DiscoverTab));
        this.tabButtons.put(Tab.INBOX, (ImageButton) findViewById(R.id.InboxTab));
        this.tabButtons.put(Tab.PROFILE, (ImageButton) findViewById(R.id.ProfileTab));

        this.tabViews = new HashMap<Tab, ScrollView>();
        this.tabViews.put(Tab.DISCOVER, (ScrollView) findViewById(R.id.DiscoverView));
        this.tabViews.put(Tab.INBOX, (ScrollView) findViewById(R.id.InboxView));
        this.tabViews.put(Tab.PROFILE, (ScrollView) findViewById(R.id.ProfileView));

        this.tabLayouts = new HashMap<Tab, LinearLayout>();
        this.tabLayouts.put(Tab.DISCOVER, (LinearLayout) findViewById(R.id.DiscoverLayout));
        this.tabLayouts.put(Tab.INBOX, (LinearLayout) findViewById(R.id.InboxLayout));
        this.tabLayouts.put(Tab.PROFILE, (LinearLayout) findViewById(R.id.ProfileLayout));
    }

    // Helper Function
    protected void setOnClickListeners() {
        for (Tab tab : Tab.values()) {
            this.tabButtons.get(tab).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        MainActivity.this.setCurrentTab(tab);
                    }
                }
            );
        }
        findViewById(R.id.AuthLogout).setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View view) {
                    Auth.logout(MainActivity.instance);
                }
            }
        );
        ((Switch) findViewById(R.id.ThemeSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ViewGroup parent = (ViewGroup) findViewById(android.R.id.content);
                if (isChecked) {
                    ThemeManager.setThemeParent(ThemeManager.Theme.DARK, parent);
                    Auth.currentUser.lightTheme = false;
                    Database.writeUser(Auth.currentUser);
                } else {
                    ThemeManager.setThemeParent(ThemeManager.Theme.LIGHT, parent);
                    Auth.currentUser.lightTheme = true;
                    Database.writeUser(Auth.currentUser);
                }
            }
        });
    }

    // Helper Function
    public void setCurrentTab(Tab newTab) {
        if (newTab == this.currentTab)
            return;

        // Reset highlight currentTab
        this.tabButtons.get(this.currentTab).setColorFilter(ThemeManager.getThemeColor(R.color.text_secondary));
        this.tabButtons.get(newTab).setColorFilter(ThemeManager.getThemeColor(R.color.accent_tertiary));

        // Set visibility of views
        this.tabViews.get(this.currentTab).setVisibility(View.GONE);
        this.tabViews.get(newTab).setVisibility(View.VISIBLE);

        this.currentTab = newTab;
    }

    // Resets DiscoverView and InboxView
    public void resetViews(Tab tab) {
        if (tab == Tab.DISCOVER) {
            LinearLayout discoverLayout = this.tabLayouts.get(Tab.DISCOVER);
            discoverLayout.removeAllViews();
            this.numListings = 0;
        } else if (tab == Tab.INBOX) {
            LinearLayout inboxLayout = findViewById(R.id.InboxLinearView);
            LinearLayout outboxLayout = findViewById(R.id.OutboxLinearView);
            inboxLayout.removeAllViews();
            outboxLayout.removeAllViews();
        }
    }

    // Helper Function
    private void addLinearLayout(LinearLayout parent) {
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setDividerDrawable(getResources().getDrawable(R.drawable.divider_horizontal));
        layout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        parent.addView(layout);
    }

    // Adds Listing in Discover View
    public void addListingView(Listing listing) {
        if (numListings++ % 2 == 0) {  // Needs another horizontal LinearLayout
            this.addLinearLayout(this.tabLayouts.get(Tab.DISCOVER));
        }
        LinearLayout discoverLayout = this.tabLayouts.get(Tab.DISCOVER);
        LinearLayout finalRow = (LinearLayout) discoverLayout.getChildAt(discoverLayout.getChildCount() - 1);
        finalRow.addView(listing.updateView(MainActivity.this));
        listing.view.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
    }

    // Adds Invitation in Inbox View
    public void addInvitationView(Invitation invitation, boolean inbox) {
        if (inbox) {
            LinearLayout inboxLayout = findViewById(R.id.InboxLinearView);
            inboxLayout.addView(invitation.updateView(MainActivity.this, true));
        } else {
            LinearLayout outboxLayout = findViewById(R.id.OutboxLinearView);
            outboxLayout.addView(invitation.updateView(MainActivity.this, false));
        }
    }

    public void setProfileView(User user){
        // Null pointer catch
        if (user == null)
            return;

        TextView name = (TextView) findViewById(R.id.EditProfileName);
        TextView email = (TextView) findViewById(R.id.Email);
        TextView ig = (TextView) findViewById(R.id.EditProfileInstagram);
        ImageView gender = (ImageView) findViewById(R.id.EditProfileGender);
        TextView bio = (TextView) findViewById(R.id.Biography);

        name.setText(user.name);
        email.setText(user.email);
        ig.setText(user.instagram);
        bio.setText(user.bio);

        if (user.gender == User.Gender.MALE) {
            gender.setImageResource(R.drawable.ic_icons8_male_120__xxxhdpi_);
            gender.setColorFilter(ThemeManager.getThemeColor(R.color.accent));
        } else if (user.gender == User.Gender.FEMALE) {
            gender.setImageResource(R.drawable.ic_icons8_female_120__xxxhdpi_);
            gender.setColorFilter(ThemeManager.getThemeColor(R.color.accent_secondary));
        } else if (user.gender == User.Gender.OTHER) {
            gender.setImageResource(R.drawable.ic_icons8_neuter_120__xxxhdpi_);
            gender.setColorFilter(ThemeManager.getThemeColor(R.color.accent_tertiary));
        }

        Chip alcoholicChip = (Chip) findViewById(R.id.EditProfileAlcoholic);
        Chip smokerChip = (Chip) findViewById(R.id.EditProfileSmoker);
        Chip socialChip = (Chip) findViewById(R.id.EditProfileSocial);
        Chip nightOwlChip = (Chip) findViewById(R.id.EditProfileNightOwl);
        alcoholicChip.setChecked(user.preferences.alcoholic);
        smokerChip.setChecked(user.preferences.smoker);
        socialChip.setChecked(user.preferences.social);
        nightOwlChip.setChecked(user.preferences.nightOwl);

        ((Switch) findViewById(R.id.ThemeSwitch)).setChecked(!user.lightTheme);
    }

    // Create Listing Intent -> CreateListing.class
    public void CreateListing(View view) {
        Intent intent = new Intent(this, CreateListingActivity.class);
        startActivity(intent);
    }

    // Open Listing Intent -> ViewListing.class
    public void OpenListing(boolean restartMainOnClose) {
        Intent intent = new Intent(this, ListingActivity.class);
        intent.putExtra("restartMainOnClose", restartMainOnClose);
        startActivity(intent);
    }

    // Edit Profile Intent -> New View to Edit Profile
    // To-Do
    public void EditProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
    //filters out the listings that are invalid based on user preferences
    public void userQueriedUpdateListings(View view) {
        Chip alcChip = findViewById(R.id.AlcoholChip);
        Chip smokeChip = findViewById(R.id.SmokerChip);
        Chip socialChip = findViewById(R.id.SocialChip);
        Chip nightOwlChip = findViewById(R.id.NightOwlChip);

        Chip smallHouseholdChip = findViewById(R.id.SmallHouseholdChip);
        Chip BigHouseholdChip = findViewById(R.id.LargeHouseholdChip);
        Chip cheapRentChip = findViewById(R.id.CheapRent);
        Chip expensiveRentChip = findViewById(R.id.PriceyRent);

        this.listingManager.updateViewsFromMap(Database.readListingCache(),
                alcChip.isChecked(),
                smokeChip.isChecked(),
                socialChip.isChecked(),
                nightOwlChip.isChecked(),
                smallHouseholdChip.isChecked(),
                BigHouseholdChip.isChecked(),
                cheapRentChip.isChecked(),
                expensiveRentChip.isChecked());
    }
}