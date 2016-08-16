package com.ziomacki.stackoverflowclient.search.eventbus;

public class ResultItemClickEvent {
    public final String detailsUrl;

    public ResultItemClickEvent(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }
}
