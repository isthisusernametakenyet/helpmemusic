package tig167.com.helpmusic;

public enum Action {

    LOGIN("login"),
    ADD_USER("add user"),
    GET_FRIENDS("get friends"),
    ADD_FRIEND("add friend"),
    ADD_PROFILE_IMG("add profile-img");

    private String value;

    Action(String string){
        value = string;
    }

    public String value() {
        return value;
    }

}