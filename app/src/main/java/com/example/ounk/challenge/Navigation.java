package com.example.ounk.challenge;

import org.json.JSONArray;

public class Navigation {

    private int type;
    private String name;
    private String link;
    private JSONArray children;

    Navigation(int type, String name, String link, JSONArray children) {
        this.type=type;
        this.name=name;
        this.link=link;
        this.children=children;
    }

    public int getType () {
        return type;
    }

    public String getName () {
        return name;
    }

    public String getLink () {
        return link;
    }

    public JSONArray getChildren() {
        return children;
    }

    public void setType (int type) {
        this.type=type;
    }

    public void setChildren (JSONArray children) {
        this.children=children;
    }

    public void setLink (String link) {
        this.link=link;
    }

}

