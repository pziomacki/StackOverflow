package com.ziomacki.stackoverflowclient.search.model;

import javax.inject.Inject;

import rx.Observable;

public class Search {

    private SearchService searchService;

    @Inject
    public Search(SearchService searchService) {
        this.searchService = searchService;
    }

    public Observable<SearchResults> startSearch(QueryParams query) {
        return searchService.startSearch(query);
    }
}
