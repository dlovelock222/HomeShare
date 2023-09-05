package com.example.homeshare;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;

public class EditProfileActivity extends AppCompatActivity {
    TextView name;
    TextView ig;
    TextView bio;
    Chip male;
    Chip female;
    Chip other;
    Chip alcoholicChip;
    Chip smokerChip;
    Chip socialChip;
    Chip nightOwlChip;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile_view);
        name = (TextView) findViewById(R.id.EditProfileName);
        ig = (TextView) findViewById(R.id.EditProfileInstagram);
        bio = (TextView) findViewById(R.id.ListingDescription);
        male = (Chip) findViewById(R.id.EditProfileMale);
        female = (Chip) findViewById(R.id.EditProfileFemale);
        other = (Chip) findViewById(R.id.EditProfileOther);
        alcoholicChip = (Chip) findViewById(R.id.EditProfileAlcoholic);
        smokerChip = (Chip) findViewById(R.id.EditProfileSmoker);
        socialChip = (Chip) findViewById(R.id.EditProfileSocial);
        nightOwlChip = (Chip) findViewById(R.id.EditProfileNightOwl);
        submit = (Button) findViewById(R.id.EditProfileSubmit);

        initFields();
        validate();

        ThemeManager.loadTheme(findViewById(android.R.id.content));
    }

    private void initFields() {
        User user = Auth.currentUser;

        if (user != null) {
            TextWatcher watcher = new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                public void afterTextChanged(Editable editable) {
                    validate();
                }
            };
            name.addTextChangedListener(watcher);
            ig.addTextChangedListener(watcher);
            bio.addTextChangedListener(watcher);
            View.OnClickListener listener = new View.OnClickListener() {
                public void onClick(View view) {
                    int resourceID = view.getId();
                    if (resourceID == R.id.EditProfileMale) {
                        female.setChecked(false);
                        other.setChecked(false);
                    } else if (resourceID == R.id.EditProfileFemale) {
                        male.setChecked(false);
                        other.setChecked(false);
                    } else if (resourceID == R.id.EditProfileOther) {
                        male.setChecked(false);
                        female.setChecked(false);
                    }

                    validate();
                }
            };
            male.setOnClickListener(listener);
            female.setOnClickListener(listener);
            other.setOnClickListener(listener);

            name.setText(user.name);
            ig.setText(user.instagram);
            bio.setText(user.bio);

            if (user.gender == User.Gender.MALE) {
                male.setChecked(true);
            } else if (user.gender == User.Gender.FEMALE) {
                female.setChecked(true);
            } else if (user.gender == User.Gender.OTHER) {
                other.setChecked(true);
            }

            if (user.preferences.alcoholic) {
                alcoholicChip.setChecked(true);
            }
            if (user.preferences.smoker) {
                smokerChip.setChecked(true);
            }
            if (user.preferences.social) {
                socialChip.setChecked(true);
            }
            if (user.preferences.nightOwl) {
                nightOwlChip.setChecked(true);
            }
        }
    }

    private void validate() {
        boolean valid = true;
        ImageView nameValid = (ImageView) findViewById(R.id.EditProfileNameValid);
        ImageView igValid = (ImageView) findViewById(R.id.EditProfileInstagramValid);
        ImageView bioValid = (ImageView) findViewById(R.id.EditProfileBioValid);
        if (Auth.validateName(name.getText().toString())) {
            nameValid.setImageResource(R.drawable.ic_baseline_check_24);
            nameValid.setColorFilter(ThemeManager.getThemeColor(R.color.accent));
        } else {
            nameValid.setImageResource(R.drawable.ic_baseline_forbidden_24);
            nameValid.setColorFilter(ThemeManager.getThemeColor(R.color.accent_secondary));
            valid = false;
        }

        if (Auth.validateInstagram(ig.getText().toString())) {
            igValid.setImageResource(R.drawable.ic_baseline_check_24);
            igValid.setColorFilter(ThemeManager.getThemeColor(R.color.accent));
        } else {
            igValid.setImageResource(R.drawable.ic_baseline_forbidden_24);
            igValid.setColorFilter(ThemeManager.getThemeColor(R.color.accent_secondary));
            valid = false;
        }

        if (Auth.validateBio(bio.getText().toString())) {
            bioValid.setImageResource(R.drawable.ic_baseline_check_24);
            bioValid.setColorFilter(ThemeManager.getThemeColor(R.color.accent));
        } else {
            bioValid.setImageResource(R.drawable.ic_baseline_forbidden_24);
            bioValid.setColorFilter(ThemeManager.getThemeColor(R.color.accent_secondary));
            valid = false;
        }

        valid = valid & (male.isChecked() || female.isChecked() || other.isChecked());

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

    public void submitChanges(View view) {
        // Update user
        User user = Auth.currentUser;

        if (user != null) {
            user.name = name.getText().toString();
            user.instagram = ig.getText().toString();
            user.bio = bio.getText().toString();
            if (male.isChecked()) {
                user.gender = User.Gender.MALE;
            } else if (female.isChecked()) {
                user.gender = User.Gender.FEMALE;
            } else {
                user.gender = User.Gender.OTHER;
            }

            user.preferences.alcoholic = alcoholicChip.isChecked();
            user.preferences.smoker = smokerChip.isChecked();
            user.preferences.social = socialChip.isChecked();
            user.preferences.nightOwl = nightOwlChip.isChecked();

            Database.writeUser(user);
            MainActivity.instance.listingManager.updateViewsFromMap(Database.readListingCache(),
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false);
            MainActivity.instance.setProfileView(user);
            Toast.makeText(this, "Profile Saved.", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }
}
