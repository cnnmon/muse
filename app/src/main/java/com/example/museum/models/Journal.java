package com.example.museum.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Journal")
public class Journal extends ParseObject {

    public static final String KEY_TITLE = "title";
    public static final String KEY_COVER = "cover";
    public static final String KEY_CONTENTS = "contents";
    public static final String KEY_AUTHOR = "author";

    public String getTitle() { return getString(KEY_TITLE); }
    public void setTitle(String title) { put(KEY_TITLE, title); }
    public ParseObject getCover() { return getParseObject(KEY_COVER); }
    public void setCover(Piece cover) { put(KEY_COVER, cover); }
    public String getContents() { return getString(KEY_CONTENTS); }
    public void setContents(String contents) { put(KEY_CONTENTS, contents); }
    public ParseUser getUser() {
        return getParseUser(KEY_AUTHOR);
    }
    public void setUser(ParseUser parseUser) {
        put(KEY_AUTHOR, parseUser);
    }
}
