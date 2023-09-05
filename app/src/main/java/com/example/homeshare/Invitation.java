package com.example.homeshare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class Invitation {
    public static Invitation active;

    public Listing listing;
    public String invitationID;
    public Status status;
    public String message;
    public User sender;

    public ConstraintLayout view;

    enum Status {
        SENT,
        ACCEPTED,
        REJECTED
    }

    public Invitation(Listing listing, String invitationID, Status status, String message, User sender) {
        this.listing = listing;
        this.invitationID = invitationID;
        this.status = status;
        this.message = message;
        this.sender = sender;
    }

    public void accept(View view) {
        this.status = Status.ACCEPTED;
        Database.writeInvitation(this);
        Database.writeRoommate(listing, sender);
        // Open detailed view and restart listing
//        Toast.makeText(MainActivity.instance, "Invitation accepted.",
//                Toast.LENGTH_SHORT).show();
        Listing.active = this.listing;
        MainActivity.instance.OpenListing(true);
    }

    public void reject(View view) {
        this.status = Status.REJECTED;
        Database.writeInvitation(this);
        // Open detailed view and restart listing

        Toast.makeText(MainActivity.instance, "Invitation rejected.",
                Toast.LENGTH_SHORT).show();
        Listing.active = this.listing;
        MainActivity.instance.OpenListing(true);
    }

    public View updateView(MainActivity mainActivity, boolean incoming) {
        LayoutInflater inflater = mainActivity.getLayoutInflater();
        this.view = (ConstraintLayout) inflater.inflate(R.layout.view_invitation, null);
        LinearLayout layoutRoot = (LinearLayout) this.view.getChildAt(2);

        // Get children
        ImageView genderIcon = (ImageView) layoutRoot.getChildAt(0);
        LinearLayout layout = (LinearLayout) layoutRoot.getChildAt(1);
        TextView sender = (TextView) layout.getChildAt(0);
        TextView message = (TextView) layout.getChildAt(1);
        Chip rejectButton = (Chip) this.view.findViewById(R.id.Reject);
        Chip acceptButton = (Chip) this.view.findViewById(R.id.Accept);
        TextView statusView = (TextView) this.view.findViewById(R.id.InvitationStatus);

        // Update fields
        User otherUser;
        if (incoming) {
            otherUser = this.sender;
            sender.setText("From: " + otherUser.name);
            acceptButton.setTag("Accept" + otherUser.email + listing.address);
            rejectButton.setTag("Decline" + otherUser.email + listing.address);
        } else {
            otherUser = this.listing.postedBy;
            sender.setText("To: " + otherUser.name);
        }
        message.setText(this.message);
        if (otherUser.gender == User.Gender.FEMALE) {
            genderIcon.setImageResource(R.drawable.ic_icons8_female_120__xxxhdpi_);
            genderIcon.setColorFilter(ThemeManager.getThemeColor(R.color.accent_secondary));
        } else if (otherUser.gender == User.Gender.OTHER) {
            genderIcon.setImageResource(R.drawable.ic_icons8_neuter_120__xxxhdpi_);
            genderIcon.setColorFilter(ThemeManager.getThemeColor(R.color.accent_tertiary));
        }

        if (incoming) {
            if (this.status == Status.SENT) {
                acceptButton.setOnClickListener(Invitation.this::accept);
                rejectButton.setOnClickListener(Invitation.this::reject);
            } else if (this.status == Status.ACCEPTED) {
                // Hide action buttons
                rejectButton.setVisibility(View.GONE);
                acceptButton.setVisibility(View.GONE);

                // Show Status
                statusView.setText("Status: Accepted");
                statusView.setTextColor(ThemeManager.getThemeColor(R.color.accent));
                statusView.setVisibility(View.VISIBLE);
            }

            // Set gap on the right
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.FILL_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 100, 0);
            this.view.setLayoutParams(params);
        } else {
            // Change bubble direction
            this.view.findViewById(R.id.ReceiverTriangle).setVisibility(View.GONE);
            this.view.findViewById(R.id.SenderTriangle).setVisibility(View.VISIBLE);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.FILL_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(100, 0, 0, 0);
            this.view.setLayoutParams(params);

            // Hide action buttons
            rejectButton.setVisibility(View.GONE);
            acceptButton.setVisibility(View.GONE);

            // Update status
            if (this.status == Status.ACCEPTED) {
                statusView.setText("Status: Accepted");
                statusView.setTextColor(ThemeManager.getThemeColor(R.color.accent));
            } else if (this.status == Status.REJECTED) {
                statusView.setText("Status: Rejected");
                statusView.setTextColor(ThemeManager.getThemeColor(R.color.accent_secondary));
            }
            statusView.setVisibility(View.VISIBLE);
        }

        ThemeManager.loadTheme(this.view);

        return this.view;
    }
}
