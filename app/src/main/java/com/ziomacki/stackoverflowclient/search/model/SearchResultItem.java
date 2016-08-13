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

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
