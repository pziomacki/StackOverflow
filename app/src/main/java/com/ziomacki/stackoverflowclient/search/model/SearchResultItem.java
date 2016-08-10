package com.ziomacki.stackoverflowclient.search.model;

import com.google.gson.annotations.SerializedName;

public class SearchResultItem {

    @SerializedName("link")
    private String link;
    @SerializedName("title")
    private String title;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
