package com.example.museum;

/*
 * responsible for communicating with the MET REST API
 * documentation: https://metmuseum.github.io/
 *
*/

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class MetClient {

    public static final String TAG = "MetClient";

    private final RequestQueue queue;
    public MetClient(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    // searches for keywords & returns a list of piece IDs
    public void getSearch(String term, Response.Listener<JSONObject> handler) {
        String URL = String.format("https://collectionapi.metmuseum.org/public/collection/v1/search?q=%s", term);
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, URL,null, handler, error -> Log.e(TAG, "failure retrieving search", error));
        queue.add(jsonObjectRequest);
    }

    // gets object information
    public void getPiece(int id, Response.Listener<JSONObject> handler) {
        String URL = String.format("https://collectionapi.metmuseum.org/public/collection/v1/objects/%s", id);
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, URL,null, handler,
                        error -> Log.e(TAG, "failure retrieving search", error));
        queue.add(jsonObjectRequest);
    }
}
