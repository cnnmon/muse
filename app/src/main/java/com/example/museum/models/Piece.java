package com.example.museum.models;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Piece extends JSONObject {

    private static final String KEY_IMAGE = "primaryImage";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ARTIST = "artistDisplayName";
    private static final String KEY_DEPARTMENT = "department";
    private static final String KEY_DATE = "objectDate";
    private static final String KEY_MEDIUM = "medium";
    private static final String KEY_URL = "objectURL";

    public String imageUrl;
    public String title;
    public String artist;
    public String department;
    public String objectDate;
    public String medium;
    public String metUrl;

    public void Piece() {}

    // Piece object from MET-given jsonObject
    public static Piece fromJson(JSONObject jsonObject) throws JSONException {
        Piece piece = new Piece();
        piece.imageUrl = jsonObject.getString(KEY_IMAGE);
        piece.title = jsonObject.getString(KEY_TITLE);
        piece.artist = jsonObject.getString(KEY_ARTIST);
        piece.department = jsonObject.getString(KEY_DEPARTMENT);
        piece.objectDate = jsonObject.getString(KEY_DATE);
        piece.medium = jsonObject.getString(KEY_MEDIUM);
        piece.metUrl = jsonObject.getString(KEY_URL);
        return piece;
    }

    public static Piece emptyPiece() {
        Piece piece = new Piece();
        return piece;
    }

    public static List<Piece> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i += 1) {
            pieces.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return pieces;
    }

    @Override
    public @NotNull String toString() {
        return "\nPiece:" +
                "title='" + title + '\'' +
                "artist='" + artist + '\'' +
                "";
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() { return artist; }

    public String getMedium() { return medium; }

    public String getDepartment() { return department; }

    public String getObjectDate() { return objectDate; }

    // format into my jsonObject
    public JSONObject getJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_IMAGE, imageUrl);
        jsonObject.put(KEY_TITLE, title);
        jsonObject.put(KEY_ARTIST, artist);
        jsonObject.put(KEY_DEPARTMENT, department);
        jsonObject.put(KEY_DATE, objectDate);
        jsonObject.put(KEY_MEDIUM, medium);
        jsonObject.put(KEY_URL, metUrl);
        return jsonObject;
    }
}