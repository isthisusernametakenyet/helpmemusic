package tig167.com.helpmusic;

public enum Action {

    LOGIN("login"),
    ADD_USER("addUser"),
    GET_USER_NAME("getUserName"),
    GET_FRIENDS("getFriends"),
    ADD_FRIEND("addFriend"),
    GET_PROFILE_IMG("getProfileImg"),
    ADD_PROFILE_IMG("addProfileImg");

    private String value;

    Action(String string){
        value = string;
    }

    public String value() {
        return value;
    }

}