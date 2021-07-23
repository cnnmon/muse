package com.example.museum.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Parcel
public class Cover extends JSONObject {

    String activeKeyword;
    int activeCover;
    List<String> keywords;
    Map<String, List<Piece>> options;

    public Cover() { }

    public Cover(Map<String, List<Piece>> options) {
        this.activeCover = 0;
        this.options = options;

        List<String> keywords = new ArrayList<>();
        keywords.addAll(options.keySet());

        this.keywords = keywords;
        this.activeKeyword = keywords.get(0);
    }

    public static Cover fromJson(JSONObject jsonObject) throws JSONException {
        Cover cover = new Cover();
        cover.activeKeyword = jsonObject.getString("activeKeyword");
        cover.activeCover = jsonObject.getInt("activeCover");
        cover.keywords = new ArrayList<>();
        cover.options = new HashMap<>();
        JSONObject optionsObject = jsonObject.getJSONObject("options");

        Iterator<String> keys = optionsObject.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            cover.keywords.add(key);
            List<Piece> pieces = Piece.fromJsonArray(optionsObject.getJSONArray(key));
            cover.options.put(key, pieces);
        }

        return cover;
    }

    public Piece getPiece() {
        try {
            return options.get(activeKeyword).get(activeCover);
        } catch (Exception e) {
            return Piece.emptyPiece();
        }
    }

    public JSONObject getJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("activeKeyword", activeKeyword);
        jsonObject.put("activeCover", activeCover);

        JSONObject optionsObject = new JSONObject();
        for (int i = 0; i < keywords.size(); i += 1) {
            String currentKeyword = keywords.get(i);
            JSONArray piecesArray = new JSONArray();
            List<Piece> pieces = options.get(currentKeyword);
            for (int j = 0; j < pieces.size(); j += 1) {
                piecesArray.put(pieces.get(j).getJson());
            }
            optionsObject.put(keywords.get(i), piecesArray);
        }

        jsonObject.put("options", optionsObject);
        return jsonObject;
    }

    public void setActiveCover(String keyword, int index) {
        activeKeyword = keyword;
        activeCover = index;
    }

    public String getActiveKeyword() { return activeKeyword; }

    public List<String> getKeywords() {
        return keywords;
    }

    public List<Piece> getOptions(String keyword) {
        return options.get(keyword);
    }

    public void removeFromOptions(List<String> keywords) {
        for (int i = 0; i < keywords.size(); i += 1) {
            options.remove(keywords.get(i));
        }
    }

    public int getOptionsSize() {
        return options.size();
    }
}
