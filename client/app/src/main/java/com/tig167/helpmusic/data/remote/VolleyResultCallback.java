package com.tig167.helpmusic.data.remote;

import com.android.volley.VolleyError;

import org.json.JSONArray;

public interface VolleyResultCallback {

    void notifySuccess(String requestType, JSONArray response);
    void notifyError(String requestType, VolleyError error);

}
