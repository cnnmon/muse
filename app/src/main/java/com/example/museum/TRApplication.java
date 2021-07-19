package com.example.museum;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.example.museum.models.Piece;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TRApplication {

    private static final String TAG = "TRApplication";

    private static final int MAX_PIECES = 3;
    private static final int MAX_OPTIONS = 3;
    private static final int MAX_KEYWORDS = 6;

    private static TextRank tr;
    private static MetClient met;

    public static void initialize(Context context) {
        met = new MetClient(context);
        InputStream sent = context.getResources().openRawResource(R.raw.en_sent);
        InputStream token = context.getResources().openRawResource(R.raw.en_token);
        InputStream stop = context.getResources().openRawResource(R.raw.stopwords);
        InputStream exstop = context.getResources().openRawResource(R.raw.extended_stopwords);

        try {
            tr = new TextRank(sent, token, stop, exstop);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Analyzes text using TextRank and finds important keywords.
     * Sends best keyword to search for art.
     * @param contents contents of text to analyze
     * @param callback function that happens after analysis is finished (ex. load gallery)
     */
    public static void onAnalysis(String contents, Callback callback) {
        //doesn't parse em-dashes correctly
        contents = contents.replace('\u2014', ' ');

        ArrayList<TextRank.TokenVertex> rankedTokens = tr.keywordExtraction(contents);
        List<String> keywords = new ArrayList<>();
        for(int i = 0; i < rankedTokens.size() && i < MAX_KEYWORDS; i++){
            TextRank.TokenVertex tv = rankedTokens.get(i);
            keywords.add(i, tv.getToken());
        }

        // iterate through all keywords to get a list of pieces for each
        Log.i(TAG, "KEYWORDS: " + String.join(",", keywords));
        Map<String, List<Piece>> options = new HashMap<>();
        int maxCount = Math.min(keywords.size(), MAX_KEYWORDS);
        for (int i = 0; i < maxCount; i += 1) {
            String currentKey = keywords.get(i);
            searchKeywords(currentKey, options, new Callback() {
                @Override
                public void run(Map<String, List<Piece>> options) {
                    if (options.size() >= MAX_OPTIONS) {
                        // ERROR HANDLING:
                        // * totalValues validation to ensure all values are found
                        // * final >= comparison may result in more results saved than necessary,
                        //   but I'm not sure of a better way to skip null queries
                        int totalValues = 0;
                        for (int i = 0; i < options.size(); i += 1) {
                            if (options.containsKey(keywords.get(i))) {
                                totalValues += options.get(keywords.get(i)).size();
                            }
                        }
                        if (totalValues >= MAX_PIECES * MAX_OPTIONS) {
                            callback.run(options);
                        }
                    }
                }

                @Override
                public void run() { }

            }, new Callback() { // error handling, need to skip this key
                @Override
                public void run(Map<String, List<Piece>> options) { }

                @Override
                public void run() {
                    // if doesn't find any IDs, remove from options and skip this key
                    options.remove(currentKey);
                    Log.i(TAG, "SKIPPED SEARCH: " + currentKey);
                }
            });

        }
    }

    /**
     * Searches MET API based on a given word.
     */
    private static void searchKeywords(String key, Map<String, List<Piece>> options, Callback callback, Callback error) {
        List<Piece> pieces = new ArrayList<>();
        options.put(key, pieces);

        // retrieves art pieces from search
        met.getSearch(key, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject arrayResponse) {
                try {
                    JSONArray array = arrayResponse.getJSONArray("objectIDs");
                    int maxCount = Math.min(array.length(), MAX_PIECES);
                    for (int i = 0; i < maxCount; i += 1) {
                        met.getPiece(array.getInt(i), response -> {
                            try {
                                Piece piece = Piece.fromJson(response);
                                pieces.add(piece);
                                if (pieces.size() == maxCount) {
                                    Log.i(TAG, "PIECES: " + pieces.toString());
                                    callback.run(options);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (JSONException e) {
                    error.run();
                }
            }
        });
    }
}
