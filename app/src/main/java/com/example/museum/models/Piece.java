package com.example.museum.models;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
import java.util.ArrayList;
import java.util.List;

@Parcel
public class Piece {

    public int id;
    public String imageURL;
    public String title;
    public String artist;
    public String department;
    public String objectDate;
    public String medium;
    public String metURL;
    public List<String> tags;

    public void Piece() {}

    public static Piece fromJson(JSONObject jsonObject) throws JSONException {
        Piece piece = new Piece();
        piece.id = jsonObject.getInt("objectID");
        piece.imageURL = jsonObject.getString("primaryImage");
        piece.title = jsonObject.getString("title");
        piece.artist = jsonObject.getString("artistDisplayName");
        piece.department = jsonObject.getString("department");
        piece.objectDate = jsonObject.getString("objectDate");
        piece.medium = jsonObject.getString("medium");
        piece.metURL = jsonObject.getString("objectURL");

        piece.tags = new ArrayList<>();
        if (!jsonObject.isNull("tags")) {
            JSONArray jsonTags = jsonObject.getJSONArray("tags");
            for (int i = 0; i < jsonTags.length(); i += 1) {
                JSONObject tag = (JSONObject) jsonTags.get(i);
                piece.tags.add(tag.getString("term"));
            }
        }

        return piece;
    }

    @Override
    public @NotNull String toString() {
        return "Piece{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", tags=" + tags +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public List<String> getTags() {
        return tags;
    }
}
