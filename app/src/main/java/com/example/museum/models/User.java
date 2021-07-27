package com.example.museum.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {

    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_IMAGE = "image";

    public String getFirstName() { return getString(KEY_FIRST_NAME); }
    public void setFirstName(String firstName) { put(KEY_FIRST_NAME, firstName); }

}
