package com.example.homeshare;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;

// Activity to Create Listing
public class ListingActivity extends AppCompatActivity {
    private Listing listing;
    private EditText invitationDescription;
    private boolean restartMainOnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enlarged_listingview);

        // Retrieve Attributes from previous Intent
        listing = Listing.active;
        this.restartMainOnClose = (boolean) getIntent().getExtras().get("restartMainOnClose");

        // Set XML Attributes
        TextView textViewRent = (TextView) findViewById(R.id.RentValue);
        textViewRent.setText("$" + listing.rent);

        TextView textView = (TextView) findViewById(R.id.address);
        textView.setText(listing.address);
        TextView textView2 = (TextView) findViewById(R.id.description);
        textView2.setText(listing.description);
        com.google.android.material.chip.Chip textView3 = (com.google.android.material.chip.Chip) findViewById(R.id.maxRoommates);
        textView3.setText(listing.roommates.size() + " / " + listing.maxRoommates);
        TextView textView4 = (TextView) findViewById(R.id.Owner);
        textView4.setText(listing.postedBy.getAnnotatedName());
        ImageView iv = (ImageView) findViewById(R.id.genderIcon);
        if (listing.postedBy.gender == User.Gender.MALE) {
            iv.setImageResource(R.drawable.ic_icons8_male_120__xxxhdpi_);
            iv.setColorFilter(ThemeManager.getThemeColor(R.color.accent));
        } else if (listing.postedBy.gender == User.Gender.FEMALE) {
            iv.setImageResource(R.drawable.ic_icons8_female_120__xxxhdpi_);
            iv.setColorFilter(ThemeManager.getThemeColor(R.color.accent_secondary));
        } else {
            iv.setImageResource(R.drawable.ic_icons8_neuter_120__xxxhdpi_);
            iv.setColorFilter(ThemeManager.getThemeColor(R.color.accent_tertiary));
        }

        // If roommates
        if (listing.roommates.size() > 1) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.RoommateLayout);
            for (User roommate : listing.roommates) {
                if (!roommate.uid.equals(listing.postedBy.uid)) {
                    TextView roommateObj = new TextView(this);
                    roommateObj.setPadding(0, 0, 0, 30);
                    roommateObj.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                    roommateObj.setTextColor(ThemeManager.getThemeColor(R.color.text_primary));
                    roommateObj.setTypeface(null, Typeface.BOLD);
                    roommateObj.setTextSize(15);
                    roommateObj.setText(roommate.getAnnotatedName());

                    layout.addView(roommateObj);
                }
            }
            layout.setVisibility(View.VISIBLE);
        }
        // If max roommates
        if (listing.roommates.size() >= listing.maxRoommates) {
            findViewById(R.id.RequestSend).setVisibility(View.GONE);
            TextView requestStatus = (TextView) findViewById(R.id.RequestStatus);
            requestStatus.setText("Max Capacity");
            requestStatus.setVisibility(View.VISIBLE);
        }
        // If you are the poster
        if (listing.postedBy.uid.equals(Auth.currentUser.uid)) {
            findViewById(R.id.deleteListing).setVisibility(View.VISIBLE);
            findViewById(R.id.RequestToJoin).setVisibility(View.GONE);
        }
        // If you have already submitted
        for (Invitation invitation : listing.invitations) {
            if (invitation.sender.uid.equals(Auth.currentUser.uid)) {
                findViewById(R.id.RequestSend).setVisibility(View.GONE);
                TextView requestStatus = (TextView) findViewById(R.id.RequestStatus);
                if (invitation.status == Invitation.Status.ACCEPTED) {
                    requestStatus.setText("Request Accepted");
                    requestStatus.setTextColor(ThemeManager.getThemeColor(R.color.accent));
                } else if (invitation.status == Invitation.Status.REJECTED) {
                    requestStatus.setText("Request Rejected");
                    requestStatus.setTextColor(ThemeManager.getThemeColor(R.color.accent_secondary));
                }
                requestStatus.setVisibility(View.VISIBLE);

                break;
            }
        }

        // Set validate
        this.invitationDescription = (EditText) findViewById(R.id.ListingDescription);
        this.invitationDescription.addTextChangedListener(
                new TextWatcher() {
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    public void afterTextChanged(Editable editable) {validate();}
                }
        );

        ThemeManager.loadTheme(findViewById(android.R.id.content));
    }

    // If Button is Clicked go back to Main Activity.
    public void backToMain(View view) {
        if (this.restartMainOnClose) {
            Intent intent = new Intent(ListingActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            this.finish();
        }
    }

    // To Do:
    public void RequestJoin(View view) throws ParseException {
        // To Fill when Profiles is done (Daniel); So we can perform backend (Me); and Notification Service (Allen)
        String listingID = this.listing.listingID;
        User user = Auth.currentUser;
        String invitationID = "I" + user.uid;
        Invitation.Status status = Invitation.Status.SENT;
        String description = this.invitationDescription.getText().toString();

        Invitation invitation = new Invitation(listing, invitationID, status, description, user);
        Database.writeInvitation(invitation);

        // Exit activity
        Toast.makeText(this, "Invitation sent.",
                Toast.LENGTH_SHORT).show();
        // Restart main activity so invitations are updated.
        Intent intent = new Intent(ListingActivity.this, MainActivity.class);
        startActivity(intent);
        ListingActivity.this.finish();
    }

    // Delete Listing
    public void deleteListing(View v) {
        // Delete from DB
        Database.deleteListing(this.listing.listingID, new Callback() {
            public void callback(Object o) {
                // Restart main activity so that listings are updated
                Intent intent = new Intent(ListingActivity.this, MainActivity.class);
                startActivity(intent);
                ListingActivity.this.finish();
            }
        });
    }

    public void validate() {
        Button invitationSubmit = (Button) findViewById(R.id.InvitationSubmit);
        String description = ((EditText) findViewById(R.id.ListingDescription)).getText().toString();

        boolean valid = (description.length() > 3 && description.length() <= 100);
        invitationSubmit.setEnabled(valid);
        if (valid) {
            invitationSubmit.setAlpha(1);
        } else {
            invitationSubmit.setAlpha(0.5f);
        }

        // Update label
        ImageView validLabel = (ImageView) findViewById(R.id.ListingDescriptionValid);
        if (valid) {
            validLabel.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_24));
            validLabel.setColorFilter(ThemeManager.getThemeColor(R.color.accent));
        } else {
            validLabel.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_forbidden_24));
            validLabel.setColorFilter(ThemeManager.getThemeColor(R.color.accent_secondary));
        }
        // Show if not empty
        if (description.isEmpty()) {
            validLabel.setVisibility(View.GONE);
        } else {
            validLabel.setVisibility(View.VISIBLE);
        }
    }
}

