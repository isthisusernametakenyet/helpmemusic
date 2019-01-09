package com.tig167.helpmusic.data.remote;

import com.android.volley.VolleyError;

import org.json.JSONArray;

/**
 * Defines methods for volley response.
 * Instantiated in class using volley, injected into volley service object on construction.
 */

public interface VolleyResultCallback {

    void notifySuccess(String requestType, JSONArray response);
    void notifyError(String requestType, VolleyError error);

}
