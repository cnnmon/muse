package com.example.museum.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.museum.R;
import com.example.museum.models.Piece;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    public static final String TAG = "SearchActivity";

    List<Piece> pieces;
    TextView etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        pieces = new ArrayList<>();
        etText = findViewById(R.id.etText);
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


}