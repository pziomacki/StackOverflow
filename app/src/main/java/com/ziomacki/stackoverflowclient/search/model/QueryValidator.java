package com.ziomacki.stackoverflowclient.search.model;

import javax.inject.Inject;

public class QueryValidator {
    public static final int QUERY_OK = 0;
    public static final int EMPTY_QUERY = 1;

    @Inject
    public QueryValidator() {
    }

    public int isQueryValid(String query) {
        if (query == null || query.equals("")) {
            return EMPTY_QUERY;
        } else {
            return QUERY_OK;
        }
    }
}
