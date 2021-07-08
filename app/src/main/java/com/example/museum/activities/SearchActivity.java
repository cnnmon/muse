package com.example.museum.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.museum.BuildConfig;
import com.example.museum.ClassPathResource;
import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.museum.MetClient;
import com.example.museum.R;
import com.example.museum.models.Piece;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    public static final String TAG = "SearchActivity";
    public static final int NUM_RESULTS = 7;
    public MetClient client;

    List<Piece> pieces;
    TextView etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        pieces = new ArrayList<>();
        client = new MetClient(this);
        etText = findViewById(R.id.etText);

        Map<String, String> map = new HashMap<>();
        map.put("GOOGLE_APPLICATION_CREDENTIALS", "/users/tiffanywang/Downloads/service-account-file.json");
        map.putAll(System.getenv());
        try {
            setEnv(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> env = System.getenv();
        for (Map.Entry<String, String> entry : env.entrySet()) {
            Log.i(TAG, entry.getKey() + " : " + entry.getValue());
        }
    }

    protected static void setEnv(Map<String, String> newenv) throws Exception {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newenv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>)     theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newenv);
        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for(Class cl : classes) {
                if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(newenv);
                }
            }
        }
    }

    public void onAnalysis(View view) throws IOException {
        String key = etText.getText().toString();
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder().setContent(key).setType(Type.PLAIN_TEXT).build();
            AnalyzeEntitySentimentRequest request =
                    AnalyzeEntitySentimentRequest.newBuilder()
                            .setDocument(doc)
                            .setEncodingType(EncodingType.UTF16)
                            .build();
            // detect entity sentiments in the given string
            AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);
            // Print the response
            for (Entity entity : response.getEntitiesList()) {
                Log.i(TAG, "Entity: %s\n" + entity.getName());
                Log.i(TAG, "Salience: %.3f\n" + entity.getSalience());
                Log.i(TAG, "Sentiment : %s\n" + entity.getSentiment());
                for (EntityMention mention : entity.getMentionsList()) {
                    Log.i(TAG, "Begin offset: %d\n" + mention.getText().getBeginOffset());
                    Log.i(TAG, "Content: %s\n" + mention.getText().getContent());
                    Log.i(TAG, "Magnitude: %.3f\n" + mention.getSentiment().getMagnitude());
                    Log.i(TAG, "Sentiment score : %.3f\n" + mention.getSentiment().getScore());
                    Log.i(TAG, "Type: %s\n\n" + mention.getType());
                }
            }
        }
    }

    public void onSearch(View view) {
        String key = etText.getText().toString();
        pieces = new ArrayList<>();
        if (key.isEmpty()) {
            Toast.makeText(this, "your search cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        // retrieve art pieces from search
        client.getSearch(key, arrayResponse -> {
            try {
                JSONArray array = arrayResponse.getJSONArray("objectIDs");
                // if no search results
                if (array.length() == 0) {
                    Toast.makeText(this, "try another keyword", Toast.LENGTH_LONG).show();
                    return;
                }

                // else, iterate through search results
                int maxCount = Math.min(array.length(), NUM_RESULTS);
                for (int i = 0; i < maxCount; i += 1) {
                    client.getPiece(array.getInt(i), response -> {
                        try {
                            Piece piece = Piece.fromJson(response);
                            pieces.add(piece);
                            if (pieces.size() == maxCount) {
                                Log.i(TAG, "DONE: " + pieces.toString());
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

    public static class SetEnv {
        public static void setEnv(Map<String, String> newenv)
                throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
            try {
                Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
                Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
                theEnvironmentField.setAccessible(true);
                Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
                env.putAll(newenv);
                Field theCaseInsensitiveEnvironmentField = processEnvironmentClass
                        .getDeclaredField("theCaseInsensitiveEnvironment");
                theCaseInsensitiveEnvironmentField.setAccessible(true);
                Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
                cienv.putAll(newenv);
            } catch (NoSuchFieldException e) {
                Class[] classes = Collections.class.getDeclaredClasses();
                Map<String, String> env = System.getenv();
                for (Class cl : classes) {
                    if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                        Field field = cl.getDeclaredField("m");
                        field.setAccessible(true);
                        Object obj = field.get(env);
                        Map<String, String> map = (Map<String, String>) obj;
                        map.clear();
                        map.putAll(newenv);
                    }
                }
            }
        }
    }
}