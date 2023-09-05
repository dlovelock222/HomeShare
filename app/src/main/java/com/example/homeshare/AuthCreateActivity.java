package com.example.homeshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.chip.Chip;

import java.util.HashMap;
import java.util.Map;

public class AuthCreateActivity extends AppCompatActivity {

    public static AuthCreateActivity instance;
    private User.Gender gender;
    private boolean alcoholic;
    private boolean smoker;
    private boolean social;
    private boolean nightOwl;
    private Map<TextField, EditText> textFields;

    enum TextField {
        NAME,
        INSTAGRAM,
        EMAIL,
        PASSWORD,
        PASSWORD_CONFIRM,
        BIO
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_create);
        instance = this;

        // Set ClickListener
        ClickListener returnAuth = new ClickListener();
        findViewById(R.id.EditProfileMale).setOnClickListener(returnAuth);
        findViewById(R.id.EditProfileFemale).setOnClickListener(returnAuth);
        findViewById(R.id.EditProfileOther).setOnClickListener(returnAuth);
        findViewById(R.id.EditProfileAlcoholicOld).setOnClickListener(returnAuth);
        findViewById(R.id.EditProfileSmoker).setOnClickListener(returnAuth);
        findViewById(R.id.EditProfileSocial).setOnClickListener(returnAuth);
        findViewById(R.id.EditProfileNightOwl).setOnClickListener(returnAuth);
        findViewById(R.id.AuthBack).setOnClickListener(returnAuth);
        findViewById(R.id.AuthBack2).setOnClickListener(returnAuth);
        findViewById(R.id.AuthBack3).setOnClickListener(returnAuth);
        findViewById(R.id.ListingCreateSubmit).setOnClickListener(returnAuth);
        findViewById(R.id.AuthSubmit2).setOnClickListener(returnAuth);
        findViewById(R.id.AuthSubmit3).setOnClickListener(returnAuth);

        // Add TextFields
        textFields = new HashMap<TextField, EditText>();
        textFields.put(TextField.NAME, (EditText) findViewById(R.id.AuthResetEmail));
        textFields.put(TextField.INSTAGRAM, (EditText) findViewById(R.id.AuthCreateInstagram));
        textFields.put(TextField.EMAIL, (EditText) findViewById(R.id.AuthCreateEmail));
        textFields.put(TextField.PASSWORD, (EditText) findViewById(R.id.AuthEmail));
        textFields.put(TextField.PASSWORD_CONFIRM, (EditText) findViewById(R.id.AuthCreatePasswordConfirm));
        textFields.put(TextField.BIO, (EditText) findViewById(R.id.AuthCreateBio));
        for (TextField tf : TextField.values()) {
            textFields.get(tf).addTextChangedListener(new Validation(tf));
        }
        // PASSWORD should trigger PASSWORD_CONFIRM TextWatcher
        textFields.get(TextField.PASSWORD).addTextChangedListener(new Validation(TextField.PASSWORD_CONFIRM));
    }

    private class ClickListener implements View.OnClickListener {
        public void onClick(View view) {
            int resourceID = view.getId();

            if (resourceID == R.id.AuthBack) {
                // Close current activity
                AuthCreateActivity.this.finish();
            } else if (resourceID == R.id.AuthBack2) {
                // Hide Page two
                findViewById(R.id.AuthCreatePage2).setVisibility(View.GONE);
                // Show Page one
                findViewById(R.id.AuthCreatePage1).setVisibility(View.VISIBLE);
            } else if (resourceID == R.id.AuthBack3) {
                // Hide Page two
                findViewById(R.id.AuthCreatePage3).setVisibility(View.GONE);
                // Show Page one
                findViewById(R.id.AuthCreatePage2).setVisibility(View.VISIBLE);
            } else if (resourceID == R.id.ListingCreateSubmit) {
                // Hide Page one
                findViewById(R.id.AuthCreatePage1).setVisibility(View.GONE);
                // Show Page two
                findViewById(R.id.AuthCreatePage2).setVisibility(View.VISIBLE);
            } else if (resourceID == R.id.AuthSubmit2) {
                // Hide Page one
                findViewById(R.id.AuthCreatePage2).setVisibility(View.GONE);
                // Show Page two
                findViewById(R.id.AuthCreatePage3).setVisibility(View.VISIBLE);
            } else if (resourceID == R.id.AuthSubmit3) {
                // Create Account
                Auth.createAccount(AuthCreateActivity.instance,
                                    findViewById(resourceID),
                                    getText(TextField.NAME),
                                    getText(TextField.INSTAGRAM),
                                    getText(TextField.EMAIL),
                                    getText(TextField.PASSWORD),
                                    getText(TextField.BIO),
                                    gender,
                                    alcoholic,
                                    smoker,
                                    social,
                                    nightOwl);
                AuthActivity.instance.finish();
            } else if (resourceID == R.id.EditProfileMale) {
                ((Chip) findViewById(R.id.EditProfileFemale)).setChecked(false);
                ((Chip) findViewById(R.id.EditProfileOther)).setChecked(false);

                if (gender == User.Gender.MALE) { gender = null; }
                else { gender = User.Gender.MALE; }
                page1Validate(null);
            } else if (resourceID == R.id.EditProfileFemale) {
                ((Chip) findViewById(R.id.EditProfileMale)).setChecked(false);
                ((Chip) findViewById(R.id.EditProfileOther)).setChecked(false);

                if (gender == User.Gender.FEMALE) { gender = null; }
                else { gender = User.Gender.FEMALE; }
                page1Validate(null);
            } else if (resourceID == R.id.EditProfileOther) {
                ((Chip) findViewById(R.id.EditProfileMale)).setChecked(false);
                ((Chip) findViewById(R.id.EditProfileFemale)).setChecked(false);

                if (gender == User.Gender.OTHER) { gender = null; }
                else { gender = User.Gender.OTHER; }
                page1Validate(null);
            } else if (resourceID == R.id.EditProfileAlcoholicOld) {
                AuthCreateActivity.this.alcoholic = !AuthCreateActivity.this.alcoholic;
            } else if (resourceID == R.id.EditProfileSmoker) {
                AuthCreateActivity.this.smoker = !AuthCreateActivity.this.smoker;
            } else if (resourceID == R.id.EditProfileSocial) {
                AuthCreateActivity.this.social = !AuthCreateActivity.this.social;
            } else if (resourceID == R.id.EditProfileNightOwl) {
                AuthCreateActivity.this.nightOwl = !AuthCreateActivity.this.nightOwl;
            }
        }
    }

    private String getText(TextField tf) {
        return textFields.get(tf).getText().toString();
    }

    private boolean page1Validate(TextField tf) {
        Button authSubmit = (Button) findViewById(R.id.ListingCreateSubmit);
        String name = this.getText(TextField.NAME);
        String ig = this.getText(TextField.INSTAGRAM);

        boolean valid = (Auth.validateName(name) &&
                         Auth.validateInstagram(ig) &&
                         gender != null);
        authSubmit.setEnabled(valid);
        if (valid) {
            authSubmit.setAlpha(1);
        } else {
            authSubmit.setAlpha(0.5f);
        }

        if (tf == TextField.NAME)
            return Auth.validateName(name);
        else if (tf == TextField.INSTAGRAM)
            return Auth.validateInstagram(ig);
        return false;
    }

    private boolean page2Validate(TextField tf) {
        Button authSubmit = (Button) findViewById(R.id.AuthSubmit2);
        String email = this.getText(TextField.EMAIL);
        String pass = this.getText(TextField.PASSWORD);
        String passConfirm = this.getText(TextField.PASSWORD_CONFIRM);

        boolean valid = (Auth.validateEmail(email) && Auth.validatePassword(pass, passConfirm));
        authSubmit.setEnabled(valid);
        if (valid) {
            authSubmit.setAlpha(1);
        } else {
            authSubmit.setAlpha(0.5f);
        }

        if (tf == TextField.EMAIL)
            return Auth.validateEmail(email);
        else if (tf == TextField.PASSWORD)
            return Auth.validatePassword(pass);
        else if (tf == TextField.PASSWORD_CONFIRM)
            return Auth.validatePassword(pass, passConfirm);
        return false;
    }

    private boolean page3Validate(TextField tf) {
        Button authSubmit = (Button) findViewById(R.id.AuthSubmit3);
        String bio = this.getText(TextField.BIO);

        boolean valid = Auth.validateBio(bio);
        authSubmit.setEnabled(valid);
        if (valid) {
            authSubmit.setAlpha(1);
        } else {
            authSubmit.setAlpha(0.5f);
        }

        if (tf == TextField.BIO)
            return Auth.validateBio(bio);
        return false;
    }

    private class Validation implements TextWatcher {
        private TextField tf;
        private ImageView validLabel;
        public Validation(TextField tf) {
            this.tf = tf;

            switch (this.tf) {
                case NAME:
                    this.validLabel = findViewById(R.id.AuthCreateNameValid);
                    break;
                case INSTAGRAM:
                    this.validLabel = findViewById(R.id.AuthCreateInstagramValid);
                    break;
                case EMAIL:
                    this.validLabel = findViewById(R.id.AuthCreateEmailValid);
                    break;
                case PASSWORD:
                    this.validLabel = findViewById(R.id.AuthCreatePasswordValid);
                    break;
                case PASSWORD_CONFIRM:
                    this.validLabel = findViewById(R.id.AuthCreatePasswordConfirmValid);
                    break;
                case BIO:
                default:
                    this.validLabel = findViewById(R.id.AuthCreateBioValid);
            }
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            this.validLabel.setVisibility(View.GONE);
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void afterTextChanged(Editable editable) {
            boolean valid;

            switch (this.tf) {
                case NAME:
                case INSTAGRAM:
                    valid = page1Validate(this.tf);
                    break;
                case EMAIL:
                case PASSWORD:
                case PASSWORD_CONFIRM:
                    valid = page2Validate(this.tf);
                    break;
                case BIO:
                default:
                    valid = page3Validate(this.tf);
            }

            // Update label
            if (valid) {
                this.validLabel.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_24));
                this.validLabel.setColorFilter(ThemeManager.getThemeColor(R.color.accent));
            } else {
                this.validLabel.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_forbidden_24));
                this.validLabel.setColorFilter(ThemeManager.getThemeColor(R.color.accent_secondary));
            }

            // Show if not empty
            if (getText(this.tf).isEmpty()) {
                this.validLabel.setVisibility(View.GONE);
            } else {
                this.validLabel.setVisibility(View.VISIBLE);
            }
        }
    }
}