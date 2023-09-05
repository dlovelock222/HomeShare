package com.example.homeshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AuthActivity extends AppCompatActivity {

    public static AuthActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        instance = this;

        // Set OnClicks
        ClickListener clickListener = new ClickListener();
        int[] resourceIDs = { R.id.ListingCreateSubmit, R.id.AuthReset, R.id.AuthSignup };
        for (int resourceID : resourceIDs) {
            findViewById(resourceID).setOnClickListener(clickListener);
        }

        // Set TextListeners
        TextWatcher textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void afterTextChanged(Editable editable) {validate();}
        };
        ((EditText) findViewById(R.id.AuthEmail)).addTextChangedListener(textWatcher);
        ((EditText) findViewById(R.id.AuthPassword)).addTextChangedListener(textWatcher);

        ThemeManager.setThemeParent(ThemeManager.Theme.DARK,
                findViewById(android.R.id.content));
    }

    private class ClickListener implements View.OnClickListener {
        public void onClick(View view) {
            int resourceID = view.getId();

            if (resourceID == R.id.ListingCreateSubmit) {
                // Attempt login with Auth
                Auth.login(AuthActivity.this,
                        findViewById(resourceID),
                        ((EditText) findViewById(R.id.AuthEmail)).getText().toString(),
                        ((EditText) findViewById(R.id.AuthPassword)).getText().toString());
            } else if (resourceID == R.id.AuthReset) {
                // Send intent for reset email
                Intent intent = new Intent(AuthActivity.this, AuthResetActivity.class);
                AuthActivity.this.startActivity(intent);
            } else if (resourceID == R.id.AuthSignup) {
                // Send intent for account creation
                Intent intent = new Intent(AuthActivity.this, AuthCreateActivity.class);
                AuthActivity.this.startActivity(intent);
            }
        }
    }

    private void validate() {
        Button authSubmit = (Button) findViewById(R.id.ListingCreateSubmit);
        String email = ((EditText) findViewById(R.id.AuthEmail)).getText().toString();
        String password = ((EditText) findViewById(R.id.AuthPassword)).getText().toString();

        boolean valid = (Auth.validateEmail(email) && Auth.validatePassword(password));
        authSubmit.setEnabled(valid);
        if (valid) {
            authSubmit.setAlpha(1);
        } else {
            authSubmit.setAlpha(0.5f);
        }
    }
}