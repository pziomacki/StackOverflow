package com.ziomacki.stackoverflowclient.search.presenter;

import android.os.Bundle;

import com.ziomacki.stackoverflowclient.search.eventbus.SearchEvent;
import com.ziomacki.stackoverflowclient.search.model.Order;
import com.ziomacki.stackoverflowclient.search.model.QueryParams;
import com.ziomacki.stackoverflowclient.search.model.QueryParamsRepository;
import com.ziomacki.stackoverflowclient.search.model.QueryValidator;
import com.ziomacki.stackoverflowclient.search.model.Sort;
import com.ziomacki.stackoverflowclient.search.view.SearchView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class SearchPresenter {


    private SearchView searchView;
    private QueryParamsRepository queryParamsRepository;
    private QueryValidator queryValidator;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private EventBus eventBus;

    @Inject
    public SearchPresenter(QueryParamsRepository queryParamsRepository, QueryValidator queryValidator,
                           EventBus eventBus) {
        this.queryParamsRepository = queryParamsRepository;
        this.queryValidator = queryValidator;
        this.eventBus = eventBus;
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

    public void search(String query, Order order, Sort sort) {
        if (isQueryStringValid(query)) {
            QueryParams queryParams = new QueryParams.Builder()
                    .query(query)
                    .order(order)
                    .sort(sort)
                    .build();
            storeQueryParams(queryParams);
            eventBus.post(new SearchEvent(queryParams));
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


    private void storeQueryParams(QueryParams queryParams) {
        queryParamsRepository.saveQueryParams(queryParams);
    }


    public void attachView(SearchView searchView) {
        this.searchView = searchView;
    }

    public void onStop() {
        if (subscriptions != null && !subscriptions.isUnsubscribed()) {
            subscriptions.clear();
        }
    }


}
