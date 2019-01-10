package com.tig167.helpmusic.data.remote;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

/**
 * Handles all volley requests throughout the application.
 */

public class VolleyService {

    private VolleyResultCallback resultCallback;
    private Context context;

    public VolleyService(VolleyResultCallback resultCallback, Context context) {
        this.resultCallback = resultCallback;
        this.context = context;
    }

    /**
     * Handle POST requests.
     * @param requestType   a string holding request type, preferably post!
     * @param url           the server url
     * @param array         the array to be sent
     */
    public void postDataVolley(final String requestType, String url, JSONArray array) {
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.POST, url, array, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (resultCallback != null)
                        resultCallback.notifySuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (resultCallback != null)
                        resultCallback.notifyError(requestType, error);
                }
            });

            queue.add(jsonArrayRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle GET requests.
     * @param requestType   a string with the specified request type i.e. get
     * @param url           the server url for this request
     */
    public void getDataVolley(final String requestType, String url) {
        try {
            RequestQueue queue = Volley.newRequestQueue(context);

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (resultCallback != null)
                        resultCallback.notifySuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (resultCallback != null)
                        resultCallback.notifyError(requestType, error);
                }
            });

            queue.add(jsonArrayRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
