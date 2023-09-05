package com.example.homeshare;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Activity to Create Listing
public class CreateListingActivity extends AppCompatActivity {
    EditText address;
    EditText description;
    EditText roommates;
    EditText expiration;
    EditText rent;
    Button submit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_create);

        // Init views
        address = (EditText) findViewById(R.id.ListingAddress);
        rent = (EditText) findViewById(R.id.ListingPrice);
        roommates = (EditText) findViewById(R.id.ListingRoommates);
        expiration = (EditText) findViewById(R.id.ListingExpiration);
        description = (EditText) findViewById(R.id.ListingDescription);
        submit = (Button) findViewById(R.id.ListingCreateSubmit);

        ImageView addressValid = (ImageView) findViewById(R.id.ListingAddressValid);
        ImageView rentValid = (ImageView) findViewById(R.id.ListingRent);
        ImageView roommatesValid = (ImageView) findViewById(R.id.ListingRoommatesValid);
        ImageView expirationValid = (ImageView) findViewById(R.id.ListingExpirationValid);
        ImageView descriptionValid = (ImageView) findViewById(R.id.ListingDescriptionValid);

        address.addTextChangedListener(new Validate(address, addressValid, Field.ADDRESS));
        rent.addTextChangedListener(new Validate(rent, rentValid, Field.RENT));
        roommates.addTextChangedListener(new Validate(roommates, roommatesValid, Field.ROOMMATE));
        expiration.addTextChangedListener(new Validate(expiration, expirationValid, Field.EXPIRATION));
        description.addTextChangedListener(new Validate(description, descriptionValid, Field.DESCRIPTION));

        ThemeManager.loadTheme(findViewById(android.R.id.content));
    }

    private void validate() {
        boolean valid = Listing.validateAddress(address.getText().toString()) &&
                Listing.validateRent(rent.getText().toString()) &&
                Listing.validateRoommates(roommates.getText().toString()) &&
                Listing.validateExpiration(expiration.getText().toString()) &&
                Listing.validateDescription(description.getText().toString());

        submit.setEnabled(valid);
        if (valid) {
            submit.setAlpha(1);
        } else {
            submit.setAlpha(0.5f);
        }
    }

    // If Button is Clicked go back to Main Activity.
    public void backToMain(View view) {
        this.finish();
    }

    // If Button is Clicked Send New Creation to Firebase DB and return to Main Activity
    public void Create(View view) {
        String address = ((EditText) findViewById(R.id.ListingAddress)).getText().toString();
        int rentValue = Integer.parseInt(((EditText) findViewById(R.id.ListingPrice)).getText().toString());
        int maxRoommates = Integer.parseInt(((EditText) findViewById(R.id.ListingRoommates)).getText().toString());
        Date expiration = Listing.parseDate(((EditText) findViewById(R.id.ListingExpiration)).getText().toString());
        String description = ((EditText) findViewById(R.id.ListingDescription)).getText().toString();

        // Add List to Listing Manager and Main Discover View
        String uid = Auth.currentUser.uid;
        String listingID = "L" + uid + "_" + address.length() + "_" + ((int) (Math.random() * 1000));
        Listing current = new Listing(listingID, Auth.currentUser, address, rentValue, maxRoommates, expiration, description);
        current.roommates.add(Auth.currentUser);
        Database.writeListing(current);

        // Restart Home Screen
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    enum Field {
        ADDRESS,
        RENT,
        ROOMMATE,
        EXPIRATION,
        DESCRIPTION
    }

    private class Validate implements TextWatcher {
        public EditText textField;
        public ImageView validLabel;
        public Field field;

        public Validate(EditText textField, ImageView validLabel, Field field) {
            this.textField = textField;
            this.validLabel = validLabel;
            this.field = field;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = textField.getText().toString();
            boolean valid = true;

            if (text.isEmpty()) {
                validLabel.setVisibility(View.GONE);
                valid = false;
            } else if (
                    (field == Field.ADDRESS && Listing.validateAddress(text)) ||
                    (field == Field.RENT && Listing.validateRent(text)) ||
                    (field == Field.ROOMMATE && Listing.validateRoommates(text)) ||
                    (field == Field.EXPIRATION && Listing.validateExpiration(text)) ||
                    (field == Field.DESCRIPTION && Listing.validateDescription(text))) {
                validLabel.setImageResource(R.drawable.ic_baseline_check_24);
                validLabel.setColorFilter(ThemeManager.getThemeColor(R.color.accent));
                validLabel.setVisibility(View.VISIBLE);
            } else {
                validLabel.setImageResource(R.drawable.ic_baseline_forbidden_24);
                validLabel.setColorFilter(ThemeManager.getThemeColor(R.color.accent_secondary));
                validLabel.setVisibility(View.VISIBLE);
                valid = false;
            }

            if (valid) {
                CreateListingActivity.this.validate();
            }
        }
    }
}

