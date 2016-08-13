package com.ziomacki.stackoverflowclient.search.eventbus;

import com.ziomacki.stackoverflowclient.search.model.QueryParams;

public class SearchEvent {
    public final QueryParams queryParams;

    public SearchEvent(QueryParams queryParams) {
        this.queryParams = queryParams;
    }
}
