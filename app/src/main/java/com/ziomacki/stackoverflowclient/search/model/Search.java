package com.ziomacki.stackoverflowclient.search.model;

import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;

public class Search {

    private SearchService searchService;
    private SearchResultsRepository searchResultsRepository;

    @Inject
    public Search(SearchService searchService, SearchResultsRepository searchResultsRepository) {
        this.searchService = searchService;
        this.searchResultsRepository = searchResultsRepository;
    }

    public Observable<SearchResults> startSearch(QueryParams query) {
        return searchService.startSearch(query).doOnNext(new Action1<SearchResults>() {
            @Override
            public void call(SearchResults searchResults) {
                searchResultsRepository.storeResults(searchResults);
            }
        });
    }
}
