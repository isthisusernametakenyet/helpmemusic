package com.tig167.helpmusic.data.remote;

public enum ServerAction {

    /**
     * Valid server requests
     */

    CONNECT("?connect="),
    LOGIN("login"),
    ADD_USER("addUser"),
    ADD_FRIEND("addFriend"),
    ADD_PROFILE_IMG("addProfileImg"),
    GET_SEARCH_RESULT("?getSearchResult=");

    private String value;

    ServerAction(String string){
        value = string;
    }

    public String value() {
        return value;
    }

}