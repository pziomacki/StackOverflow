package com.ziomacki.stackoverflowclient.search.presenter;

import android.os.Bundle;

import com.ziomacki.stackoverflowclient.search.model.Order;
import com.ziomacki.stackoverflowclient.search.model.QueryParams;
import com.ziomacki.stackoverflowclient.search.model.QueryParamsRepository;
import com.ziomacki.stackoverflowclient.search.model.QueryValidator;
import com.ziomacki.stackoverflowclient.search.model.Search;
import com.ziomacki.stackoverflowclient.search.model.SearchResults;
import com.ziomacki.stackoverflowclient.search.model.Sort;
import com.ziomacki.stackoverflowclient.search.view.SearchView;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SearchPresenter {

    private Search search;
    private SearchView searchView;
    private QueryParamsRepository queryParamsRepository;
    private QueryParams queryParams;
    private QueryValidator queryValidator;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Inject
    public SearchPresenter(Search search, QueryParamsRepository queryParamsRepository, QueryValidator queryValidator) {
        this.search = search;
        this.queryParamsRepository = queryParamsRepository;
        this.queryValidator = queryValidator;
    }

    public void setInitialQueryParamsIfNotRecreated(final Bundle savedInstance) {
        Subscription subscription = queryParamsRepository.getQueryParamsObservable().subscribe(new Action1<QueryParams>() {
            @Override
            public void call(QueryParams queryParams) {
                if (savedInstance == null) {
                    searchView.setQuery(queryParams.getQuery());
                    searchView.setOrder(queryParams.getOrder());
                    searchView.setSort(queryParams.getSort());
                }
            }
        });
        subscriptions.add(subscription);
    }

    public void search(String query, Order order, Sort sort){
        if (isQueryStringValid(query)) {
            queryParams = new QueryParams.Builder()
                    .query(query)
                    .order(order)
                    .sort(sort)
                    .build();
            storeQueryParams(queryParams);
            search(queryParams);
        }
    }

    private boolean isQueryStringValid(String query) {
        if (queryValidator.isQueryValid(query) == QueryValidator.EMPTY_QUERY) {
            searchView.displayEmptyQueryMessage();
            return false;
        } else {
            return true;
        }
    }

    private void search(QueryParams queryParams) {
        searchView.displayDataLoading();
        Subscription subscription = search.startSearch(queryParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SearchResults>() {
                    @Override
                    public void call(SearchResults searchResults) {
                        handleSuccesfullResponse(searchResults);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        //TODO: handle specific messages
                        searchView.hideDataLoading();
                        searchView.displayErrorMessage();
                    }
                });
        subscriptions.add(subscription);
    }

    private void storeQueryParams(QueryParams queryParams) {
        queryParamsRepository.saveQueryParams(queryParams);
    }

    private void handleSuccesfullResponse(SearchResults searchResults) {
        searchView.hideDataLoading();
        if (searchResults.getSearchResultItemList().size() > 0) {
            searchView.displaySearchResults(searchResults.getSearchResultItemList());
        } else {
            searchView.displayNoResultsMessage();
        }
    }

    public void attachView(SearchView searchView) {
        this.searchView = searchView;
    }

    public void onStop() {
        if (subscriptions != null && !subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
        }
    }

    public void refresh() {
        search(queryParams);
    }
}
