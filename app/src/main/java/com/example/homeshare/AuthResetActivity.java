package com.example.homeshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AuthResetActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_reset);

        // Init Listeners
        findViewById(R.id.AuthBack).setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View view) {
                    AuthResetActivity.this.finish();
                }
            }
        );
        findViewById(R.id.ListingCreateSubmit).setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View view) {
                    Auth.changePassword(AuthResetActivity.this,
                                        findViewById(R.id.ListingCreateSubmit),
                                        ((EditText) findViewById(R.id.AuthResetEmail)).getText().toString());
                }
            }
        );

        ((EditText) findViewById(R.id.AuthResetEmail)).addTextChangedListener(
            new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                public void afterTextChanged(Editable editable) {validate();}
            }
        );
    }

    public void validate() {
        Button authSubmit = (Button) findViewById(R.id.ListingCreateSubmit);
        String email = ((EditText) findViewById(R.id.AuthResetEmail)).getText().toString();

        boolean valid = (Auth.validateEmail(email));
        authSubmit.setEnabled(valid);
        if (valid) {
            authSubmit.setAlpha(1);
        } else {
            authSubmit.setAlpha(0.5f);
        }
    }
}