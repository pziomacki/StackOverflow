package com.ziomacki.stackoverflowclient.search.model;

import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;

public class SearchResultItem extends RealmObject{

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

    public void setLink(String link) {
        this.link = link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
