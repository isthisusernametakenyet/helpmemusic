package com.tig167.helpmusic.data.remote;

public enum ServerAction {

    CONNECT("connect"),
    LOGIN("login"),
    ADD_USER("addUser"),
    GET_USER_NAME("getUserName"),
    GET_FRIENDS("getFriends"),
    ADD_FRIEND("addFriend"),
    GET_PROFILE_IMG("getProfileImg"),
    ADD_PROFILE_IMG("addProfileImg");

    private String value;

    ServerAction(String string){
        value = string;
    }

    public String value() {
        return value;
    }

}