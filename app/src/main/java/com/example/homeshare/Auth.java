package com.example.homeshare;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth {
    public static FirebaseAuth mAuth;
    public static User currentUser;

    public static void init(MainActivity mainActivity) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Auth.logout(mainActivity);
        } else {
            Database.readUser(currentUser.getUid(), new Callback<User>() {
                public void callback(User user) {
                    Auth.currentUser = user;
                    mainActivity.setProfileView(user);

                    if (Auth.currentUser.lightTheme) {
                        ThemeManager.activeTheme = ThemeManager.Theme.LIGHT;
                        ThemeManager.loadTheme(mainActivity.findViewById(android.R.id.content));
                    }
                }
            });
        }
    }

    public static void login(Activity act,
                             Button submit,
                             String email,
                             String password) {
        // Authenticate
        submit.setAlpha(0.5f);
        submit.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        // If success, go to discover
                        Database.readUser(task.getResult().getUser().getUid(), new Callback<User>() {
                            public void callback(User user) {
                                Auth.currentUser = user;
                                if (user.lightTheme) {
                                    ThemeManager.activeTheme = ThemeManager.Theme.LIGHT;
                                }
                                Intent intent = new Intent(act, MainActivity.class);
                                act.startActivity(intent);
                                act.finish();  // Closes Activity
                            }
                        });
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(act, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                        submit.setAlpha(1);
                        submit.setEnabled(true);
                    }
                }
            });
    }

    public static void logout(Activity act) {
        mAuth.signOut();

        Intent intent = new Intent(act, AuthActivity.class);
        act.startActivity(intent);
        act.finish();  // Closes Activity
    }

    public static void changePassword(Activity act,
                                      Button submit,
                                      String email) {
        submit.setAlpha(0.5f);
        submit.setEnabled(false);

        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(act, "Email sent.",
                                Toast.LENGTH_SHORT).show();
                        act.finish();
                    } else {
                        Toast.makeText(act, "Not a valid email.",
                                Toast.LENGTH_SHORT).show();

                        submit.setAlpha(1);
                        submit.setEnabled(true);
                    }
                }
            });
    }

    public static void createAccount(Activity act,
                                     Button submit,
                                     String name,
                                     String instagram,
                                     String email,
                                     String password,
                                     String bio,
                                     User.Gender gender,
                                     boolean alcoholic,
                                     boolean smoker,
                                     boolean social,
                                     boolean nightOwl) {
        submit.setAlpha(0.5f);
        submit.setEnabled(false);
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Update name, instagram, and bio in Firebase Realtime DB
                        Database.writeUser(new User(user.getUid(), name, bio, instagram, email, gender,
                                                    alcoholic, smoker, social, nightOwl));
                        login(act, submit, email, password);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(act, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        Toast.makeText(act, task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();

                        submit.setAlpha(1);
                        submit.setEnabled(true);
                    }
                }
            });

    }

    public static void deleteAccount() {

    }

    // Validation functions
    public static boolean validateName(String name) {
        return name.trim().equals(name) && name.length() > 4;
    }
    public static boolean validateInstagram(String ig) {
        return ig.trim().equals(ig) && ig.length() > 3 && !ig.contains("@");
    }
    public static boolean validateEmail(String email) {
        return email.trim().equals(email) && email.length() > 5 && email.contains("@") && !email.contains(" ");
    }
    public static boolean validatePassword(String pass) {
        return pass.trim().equals(pass) && pass.length() > 5 && !pass.contains(" ");
    }
    public static boolean validatePassword(String pass, String passConfirm) {
        return validatePassword(pass) && pass.equals(passConfirm);
    }
    public static boolean validateBio(String bio) {
        return bio.trim().length() > 3;
    }
}
