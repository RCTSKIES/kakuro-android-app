package com.example.authentication.Helpers;

public class StringHelper {
    public static String encodeEmail(String email) {
        return email.replace(".", ","); // Encode dots as commas
    }

    public static String decodeEmail(String encodedEmail) {
        return encodedEmail.replace(",", "."); // Decode back to normal email
    }
}
