package com.ziomacki.stackoverflowclient.search.model;

import com.google.gson.annotations.SerializedName;

public class SearchResultItem {

    @SerializedName("link")
    private String link;
    @SerializedName("title")
    private String title;
    @SerializedName("answer_count")
    private int answerCount;
    @SerializedName("owner")
    private Owner owner;

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public Owner getOwner() {
        return owner;
    }

}
