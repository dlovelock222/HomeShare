package com.example.homeshare;

import android.app.Activity;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class AuthTest {
    @Test
    public void emailCorrectInput(){
        assertTrue(Auth.validateEmail("lovelock@usc.edu"));
    }

    @Test
    public void instagramCorrectInput(){
        assertTrue(Auth.validateInstagram("daniel"));
    }
}