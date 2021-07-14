package com.example.museum;

import android.content.Context;
import android.util.Log;

import com.example.museum.models.Piece;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TRApplication {

    private static final String TAG = "TRClient";

    private static final int MAX_PIECES = 1;
    private static final int MAX_KEYWORDS = 1;

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

    // TODO: Fix (No such file or directory) error
    /*
    private void POStagger(String content) throws IOException {
        InputStream enpos = new FileInputStream("/Users/tiffanywang/Documents/Museum/app/src/main/res/raw/en_pos_maxent.bin");
        pos = new POSModel(enpos);
        Log.i(TAG, String.valueOf(pos == null));
        //Instantiating POSTaggerME class
        POSTaggerME tagger = new POSTaggerME(pos);

        //Tokenizing the sentence using WhitespaceTokenizer class
        WhitespaceTokenizer whitespaceTokenizer= WhitespaceTokenizer.INSTANCE;
        String[] tokens = whitespaceTokenizer.tokenize(content);

        //Generating tags
        String[] tags = tagger.tag(tokens);

        //Instantiating the POSSample class
        POSSample sample = new POSSample(tokens, tags);
        Log.i(TAG, sample.toString());
    }*/

    /**
     * Analyzes text using TextRank and finds important keywords.
     * Sends best keyword to search for art.
     * @contents: contents of text to analyze
     * @callback: function that happens after analysis is finished (ex. load gallery)
     */
    public static void onAnalysis(String contents, Callback callback) {
        //doesn't parse em-dashes correctly
        contents = contents.replace('\u2014', ' ');

        if(contents != null && contents.split(" ").length > 1){
            ArrayList<TextRank.TokenVertex> rankedTokens = tr.keywordExtraction(contents);
            List<String> keywords = new ArrayList<>();
            for(int i = 0; i < rankedTokens.size() && i < MAX_KEYWORDS; i++){
                TextRank.TokenVertex tv = rankedTokens.get(i);
                keywords.add(i, tv.getToken());
            }

            // TODO: find more relevant keyword than first
            Log.i(TAG, "KEYWORDS: " + String.join(",", keywords));
            //POStagger(String.join(",", keywords));
            searchKeyword(keywords.get(0), callback);
        }
        else {
            // TODO: error handling
        }
    }

    /**
     * Searches MET API based on a given word.
     */
    private static void searchKeyword(String key, Callback callback) {
        List<Piece> pieces = new ArrayList<>();

        // retrieves art pieces from search
        met.getSearch(key, arrayResponse -> {
            try {
                JSONArray array = arrayResponse.getJSONArray("objectIDs");
                // if no search results
                if (array.length() == 0) {
                    return;
                }

                // else, iterate through search results
                int maxCount = Math.min(array.length(), MAX_PIECES);
                for (int i = 0; i < maxCount; i += 1) {
                    met.getPiece(array.getInt(i), response -> {
                        try {
                            Piece piece = Piece.fromJson(response);
                            pieces.add(piece);
                            if (pieces.size() == maxCount) {
                                Log.i(TAG, "PIECES: " + pieces.toString());
                                callback.run(pieces.get(0));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    /*
    private void loadGallery(List<Piece> pieces) {
        Intent i = new Intent(this, GalleryActivity.class);
        i.putExtra("pieces", Parcels.wrap(pieces));
        context.startActivity(i);
    }*/
}
