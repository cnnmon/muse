package com.example.museum;

import android.app.Application;

import com.example.museum.models.Cover;
import com.example.museum.models.Journal;
import com.example.museum.models.Piece;
import com.example.museum.models.User;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParseApplication extends Application {

    public static final String TAG = "ParseApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Journal.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }

    public static void queryJournals(FindCallback<Journal> callback) {
        ParseQuery<Journal> query = ParseQuery.getQuery(Journal.class);
        query.include(Journal.KEY_AUTHOR);
        query.whereEqualTo(Journal.KEY_AUTHOR, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder("createdAt");
        query.findInBackground(callback);
    }

    public static void saveJournal(String title, String content, SaveCallback callback) {
        Journal journal = new Journal();
        journal.setTitle(title);
        journal.setContent(content);
        journal.setUser(ParseUser.getCurrentUser());

        TRApplication.onAnalysis(content, new Callback() {
            @Override
            public void run() {
                // needed to have for callback; isn't called
            }

            @Override
            public void run(Map<String, List<Piece>> options) {
                try {
                    prune(options);
                    Cover cover = new Cover(options);
                    journal.setCover(cover.getJson());
                    journal.saveInBackground(callback);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void prune(Map<String, List<Piece>> options) {
        List<String> toRemove = new ArrayList<>();
        // prune empty objects in options
        for (String key : options.keySet()) {
            if (options.get(key).size() == 0) {
                toRemove.add(key);
            }
        }
        for (String key : toRemove) {
            options.remove(key);
        }
    }

    // update journal & regenerate cover
    public static void updateJournal(Journal journal, String title, String content, SaveCallback callback) {
        journal.setTitle(title);
        journal.setContent(content);
        TRApplication.onAnalysis(content, new Callback() {
            @Override
            public void run() {
                // Needed to have for callback; isn't called
            }

            @Override
            public void run(Map<String, List<Piece>> options) {
                try {
                    prune(options);
                    Cover cover = new Cover(options);
                    journal.setCover(cover.getJson());
                    journal.saveInBackground(callback);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void updateTitle(Journal journal, String title, SaveCallback callback) {
        journal.setTitle(title);
        journal.saveInBackground(callback);
    }

    public static void deleteJournal(Journal journal, DeleteCallback callback) {
        journal.deleteInBackground(callback);
    }

    public static void loginUser(String username, String password, LogInCallback callback) {
        ParseUser.logInInBackground(username, password, callback);
    }

    public static void logoutUser(LogOutCallback callback) {
        ParseUser.logOutInBackground(callback);
    }

    public static void registerUser(String firstName, String username, String password, SignUpCallback callback) {
        User user = new User();
        user.setFirstName(firstName);
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(callback);
    }

    public static void updateActiveCover(Journal journal, Cover cover, SaveCallback callback) {
        try {
            journal.setCover(cover.getJson());
            journal.saveInBackground(callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
