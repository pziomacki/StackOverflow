package com.ziomacki.stackoverflowclient.search.presenter;

import android.os.Bundle;
import com.ziomacki.stackoverflowclient.search.model.Order;
import com.ziomacki.stackoverflowclient.search.model.QueryParams;
import com.ziomacki.stackoverflowclient.search.model.QueryParamsRepository;
import com.ziomacki.stackoverflowclient.search.model.QueryValidator;
import com.ziomacki.stackoverflowclient.search.model.Sort;
import com.ziomacki.stackoverflowclient.search.view.SearchView;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class SearchPresenter {

    private SearchView searchView;
    private QueryParamsRepository queryParamsRepository;
    private QueryValidator queryValidator;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Inject
    public SearchPresenter(QueryParamsRepository queryParamsRepository, QueryValidator queryValidator) {
        this.queryParamsRepository = queryParamsRepository;
        this.queryValidator = queryValidator;
    }

    public void setInitialQueryParams(final Bundle savedInstance) {
        Subscription subscription = queryParamsRepository.getQueryParamsObservable()
                .subscribe(new Action1<QueryParams>() {
                    @Override
                    public void call(QueryParams queryParams) {
                        searchIfRecreated(savedInstance, queryParams);
                        updateViewsIfNotRecreated(savedInstance, queryParams);
                    }
                });
        subscriptions.add(subscription);
    }

    private void searchIfRecreated(Bundle savedInstance, QueryParams queryParams) {
        if (savedInstance != null) {
            search(queryParams);
        }
    }

    private void updateViewsIfNotRecreated(Bundle savedInstance, QueryParams queryParams) {
        if (savedInstance == null) {
            searchView.setQuery(queryParams.getQuery());
            searchView.setOrder(queryParams.getOrder());
            searchView.setSort(queryParams.getSort());
        }
    }

    public void onSearchActionPerformed(String query, Order order, Sort sort) {
        if (isQueryStringValid(query)) {
            QueryParams queryParams = new QueryParams.Builder()
                    .query(query)
                    .order(order)
                    .sort(sort)
                    .build();
            search(queryParams);
        } else {
            displayEmptyQueryMessage();
        }
    }

    private void search(QueryParams queryParams) {
        searchView.closeKeyboard();
        storeQueryParams(queryParams);
        searchView.search(queryParams);
    }

    private boolean isQueryStringValid(String query) {
        if (queryValidator.isQueryValid(query) == QueryValidator.EMPTY_QUERY) {
            return false;
        } else {
            return true;
        }
    }

    private void displayEmptyQueryMessage() {
        searchView.displayEmptyQueryMessage();
    }

    private void storeQueryParams(QueryParams queryParams) {
        queryParamsRepository.saveQueryParams(queryParams);
    }

    public void attachView(SearchView searchView) {
        this.searchView = searchView;
    }

    public void onStop() {
        subscriptions.clear();
    }
}
