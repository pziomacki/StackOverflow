package com.ziomacki.stackoverflowclient.search.model;

import javax.inject.Inject;
import rx.Observable;

public class SearchService {

    private SearchApiService searchApiService;

    @Inject
    public SearchService(SearchApiService searchApiService) {
        this.searchApiService = searchApiService;
    }

    public Observable<SearchResults> startSearch(QueryParams query) {
        return searchApiService.fetchSearchResults(query.getQuery(), query.getSort(), query.getOrder());
    }

}
