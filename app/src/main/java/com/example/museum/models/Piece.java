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
        piece.imageUrl = jsonObject.getString("primaryImage");
        piece.title = jsonObject.getString("title");
        piece.artist = jsonObject.getString("artistDisplayName");
        piece.department = jsonObject.getString("department");
        piece.objectDate = jsonObject.getString("objectDate");
        piece.medium = jsonObject.getString("medium");
        piece.metUrl = jsonObject.getString("objectURL");
        return piece;
    }

    // TODO: fill with default values
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

    // format into my jsonObject
    public JSONObject getJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("primaryImage", imageUrl);
        jsonObject.put("title", title);
        jsonObject.put("artistDisplayName", artist);
        jsonObject.put("department", department);
        jsonObject.put("objectDate", objectDate);
        jsonObject.put("medium", medium);
        jsonObject.put("objectURL", metUrl);
        return jsonObject;
    }
}