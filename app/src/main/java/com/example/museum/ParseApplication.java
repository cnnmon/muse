package com.example.museum;

import android.app.Application;
import android.content.Context;

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

    private static final String TAG = "ParseApplication";
    private static ParseApplication instance = null;

    public static ParseApplication get() {
        // no null handler because initialization needs context
        // assumes it's been initialized
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        instance = new ParseApplication(context);
    }

    public ParseApplication() {}

    /**
     * Private constructor to setup singleton.
     */
    public ParseApplication(Context context) {
        String app_id = context.getResources().getString(R.string.back4app_app_id);
        String server_url = context.getResources().getString(R.string.back4app_server_url);
        String client_key = context.getResources().getString(R.string.back4app_client_key);

        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Journal.class);
        Parse.initialize(new Parse.Configuration.Builder(context)
                .applicationId(app_id)
                .clientKey(client_key)
                .server(server_url)
                .build());
    }

    /**
     * Loads all journals from current user.
     */
    public void queryJournals(FindCallback<Journal> callback) {
        ParseQuery<Journal> query = ParseQuery.getQuery(Journal.class);
        query.include(Journal.KEY_AUTHOR);
        query.whereEqualTo(Journal.KEY_AUTHOR, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder("createdAt");
        query.findInBackground(callback);
    }

    /**
     * Saves new journal to current user.
     */
    public void saveJournal(String title, String content, SaveCallback callback) {
        Journal journal = new Journal();
        journal.setTitle(title);
        journal.setContent(content);
        journal.setUser(ParseUser.getCurrentUser());

        TRApplication.get().onAnalysis(content, new Callback() {
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

    /**
     * Used when saving a new journal.
     * Does extra work to ensure any empty options aren't saved
     * and aren't offered in the future.
     */
    private void prune(Map<String, List<Piece>> options) {
        List<String> toRemove = new ArrayList<>();
        for (String key : options.keySet()) {
            if (options.get(key).size() == 0) {
                toRemove.add(key);
            }
        }
        for (String key : toRemove) {
            options.remove(key);
        }
    }

    /**
     * Updates an existing journal
     * and regenerates a cover.
     */
    public void updateJournal(Journal journal, String title, String content, SaveCallback callback) {
        journal.setTitle(title);
        journal.setContent(content);
        TRApplication.get().onAnalysis(content, new Callback() {
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

    public void updateTitle(Journal journal, String title, SaveCallback callback) {
        journal.setTitle(title);
        journal.saveInBackground(callback);
    }

    public void deleteJournal(Journal journal, DeleteCallback callback) {
        journal.deleteInBackground(callback);
    }

    public void loginUser(String username, String password, LogInCallback callback) {
        ParseUser.logInInBackground(username, password, callback);
    }

    public void logoutUser(LogOutCallback callback) {
        ParseUser.logOutInBackground(callback);
    }

    public void registerUser(String firstName, String username, String password, SignUpCallback callback) {
        User user = new User();
        user.setFirstName(firstName);
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(callback);
    }

    public void updateActiveCover(Journal journal, Cover cover, SaveCallback callback) {
        try {
            journal.setCover(cover.getJson());
            journal.saveInBackground(callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
