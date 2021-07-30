package com.example.museum.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

@ParseClassName("Journal")
public class Journal extends ParseObject {

    public static final String KEY_TITLE = "title";
    public static final String KEY_COVER = "cover";
    public static final String KEY_CONTENTS = "contents";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_IMAGE = "image";

    public String getTitle() { return getString(KEY_TITLE); }
    public void setTitle(String title) { put(KEY_TITLE, title); }

    public Cover getCover() {
        JSONObject jsonObject = getJSONObject(KEY_COVER);
        try {
            return Cover.fromJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setCover(JSONObject cover) { put(KEY_COVER, cover); }
    public String getContent() { return getString(KEY_CONTENTS); }
    public void setContent(String contents) { put(KEY_CONTENTS, contents); }
    public ParseUser getUser() {
        return getParseUser(KEY_AUTHOR);
    }
    public void setUser(ParseUser parseUser) {
        put(KEY_AUTHOR, parseUser);
    }
    public ParseFile getImage() { return getParseFile(KEY_IMAGE); }
    public void setImage(ParseFile image) { put(KEY_IMAGE, image); }

    public String getSimpleDate() { return getSimpleDate(getCreatedAt()); }
    public static String getSimpleDateCurrent() {
        return getSimpleDate(new Date());
    }

    public static String getSimpleDate(Date date) {
        Format f = new SimpleDateFormat("MM/dd/yy");
        return f.format(date);
    }
}
