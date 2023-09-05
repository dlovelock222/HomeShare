package com.example.homeshare;

import org.junit.Test;

import static org.junit.Assert.*;

public class ListingTest {

    @Test
    public void validateAddressCorrectInput() {
        assertTrue(Listing.validateAddress("3705 Smallwood ct, Pleasanton, CA, 94566"));
    }

    @Test
    public void addressWithLeadingSpace(){
        assertFalse(Listing.validateAddress(" 3705 Smallwood ct, Pleasanton, CA, 94566"));
    }

    @Test
    public void addressTooShort(){
        assertFalse(Listing.validateAddress("3705"));
    }

    @Test
    public void validateRoommatesNonInteger() {
        assertFalse(Listing.validateRoommates("daniel"));
    }

    @Test
    public void validateRoommatesTooFew() {
        assertFalse(Listing.validateRoommates("1"));
    }

    @Test
    public void validateRoommatesTooMany() {
        assertFalse(Listing.validateRoommates("15"));
    }

    @Test
    public void validateRoommatesCorrectInput() {
        assertTrue(Listing.validateRoommates("7"));
    }

    @Test
    public void validateExpirationNotADate() {
        assertFalse(Listing.validateExpiration("joseph"));
    }

    @Test
    public void validateExpirationIncorrectDate() {
        assertFalse(Listing.validateExpiration("12-22-1999"));
    }

    @Test
    public void validateExpirationCorrectInput() {
        assertTrue(Listing.validateExpiration("12/22/2022"));
    }

    @Test
    public void validateDescriptionTooLong() {
        assertFalse(Listing.validateDescription("Lorem Ipsum is simply dummy text of the printing and" +
                " typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever" +
                " since the 1500s, when an unknown printer took a galley of type and scrambled it to" +
                " make a type specimen book. It has survived not only five centuries, but also the " +
                "leap into electronic typesetting, remaining essentially unchanged. It was popularised" +
                " in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, " +
                "and more recently with desktop publishing software like Aldus PageMaker including " +
                "versions of Lorem Ipsum."));
    }

    @Test
    public void validateDescriptionTooShort() {
        assertFalse(Listing.validateDescription("cozy"));
    }
    @Test
    public void validateDescriptionCorrectInput() {
        assertTrue(Listing.validateDescription("Great location, modern utilities"));
    }
}