package com.ziomacki.stackoverflowclient.search.presenter;

import android.os.Bundle;
import com.ziomacki.stackoverflowclient.search.model.QueryParams;
import com.ziomacki.stackoverflowclient.search.model.QueryParamsRepository;
import com.ziomacki.stackoverflowclient.search.model.QueryValidator;
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

    public void onSaveInstance(QueryParams queryParams) {
        storeQueryParams(queryParams);
    }

    public void setInitialQueryParams(final Bundle savedInstance) {
        Subscription subscription = queryParamsRepository.getQueryParamsObservable()
                .subscribe(new Action1<QueryParams>() {
                    @Override
                    public void call(QueryParams queryParams) {
                        updateViews(queryParams);
                    }
                });
        subscriptions.add(subscription);
    }

    private void updateViews(QueryParams queryParams) {
        searchView.initQuery(queryParams.getQuery());
        searchView.initOrderValue(queryParams.getOrder());
        searchView.initSortValue(queryParams.getSort());
    }

    public void onSearchActionPerformed(QueryParams queryParams) {
        if (isQueryStringValid(queryParams.getQuery())) {
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
