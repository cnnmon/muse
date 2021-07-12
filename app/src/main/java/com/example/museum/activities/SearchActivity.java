package com.example.museum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.museum.MetClient;
import com.example.museum.R;
import com.example.museum.TextRank;
import com.example.museum.models.Piece;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    public static final String TAG = "SearchActivity";
    public static final int MAX_PIECES = 7;
    public static final int MAX_KEYWORDS = 4;
    public MetClient client;

    private static TextRank tr;

    List<Piece> pieces;
    TextView etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        pieces = new ArrayList<>();
        client = new MetClient(this);
        etText = findViewById(R.id.etText);

        initializeTextRank();
    }

    private void initializeTextRank(){
        if(tr == null){
            InputStream sent = getResources().openRawResource(R.raw.en_sent);
            InputStream token = getResources().openRawResource(R.raw.en_token);
            InputStream stop = getResources().openRawResource(R.raw.stopwords);
            InputStream exstop = getResources().openRawResource(R.raw.extended_stopwords);
            try {
                tr = new TextRank(sent, token, stop, exstop);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Analyzes text using TextRank and finds important keywords.
     * Sends best keyword to search for art.
     */
    public void onAnalysis(View view){
        String contents = etText.getText().toString();

        //doesn't parse em-dashes correctly
        contents = contents.replace('\u2014', ' ');

        if(contents != null && contents != ""){
            ArrayList<TextRank.TokenVertex> rankedTokens = tr.keywordExtraction(contents);
            List<String> keywords = new ArrayList<>();
            for(int i = 0; i < rankedTokens.size() && i < MAX_KEYWORDS; i++){
                TextRank.TokenVertex tv = rankedTokens.get(i);
                keywords.add(i, tv.getToken());
            }

            // TODO: find more relevant keyword than first
            Log.i(TAG, "KEYWORDS: " + String.join(",", keywords));
            searchKeyword(keywords.get(0));
        }
        else {
            Toast.makeText(this, "your journal cannot be empty", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Searches MET API based on a given word.
     */
    public void searchKeyword(String key) {
        pieces = new ArrayList<>();
        client.getSearch(key, arrayResponse -> {
            try {
                JSONArray array = arrayResponse.getJSONArray("objectIDs");
                // if no search results
                if (array.length() == 0) {
                    return;
                }

                // else, iterate through search results
                int maxCount = Math.min(array.length(), MAX_PIECES);
                for (int i = 0; i < maxCount; i += 1) {
                    client.getPiece(array.getInt(i), response -> {
                        try {
                            Piece piece = Piece.fromJson(response);
                            pieces.add(piece);
                            if (pieces.size() == maxCount) {
                                Log.i(TAG, "PIECES: " + pieces.toString());
                                loadGallery();
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

    private void loadGallery() {
        Intent i = new Intent(this, GalleryActivity.class);
        i.putExtra("pieces", Parcels.wrap(pieces));
        startActivity(i);
    }
}