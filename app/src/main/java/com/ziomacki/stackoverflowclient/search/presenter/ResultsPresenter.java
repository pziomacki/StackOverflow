package com.ziomacki.stackoverflowclient.search.presenter;

import com.ziomacki.stackoverflowclient.search.eventbus.SearchEvent;
import com.ziomacki.stackoverflowclient.search.model.QueryParams;
import com.ziomacki.stackoverflowclient.search.model.QueryParamsRepository;
import com.ziomacki.stackoverflowclient.search.model.Search;
import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;
import com.ziomacki.stackoverflowclient.search.model.SearchResults;
import com.ziomacki.stackoverflowclient.search.view.ResultsView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ResultsPresenter {

    private List<SearchResultItem> searchResultItemList;
    private ResultsView resultsView;

    private Search search;
    private QueryParams queryParams;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private EventBus eventBus;

    @Inject
    public ResultsPresenter(Search search,  EventBus eventBus) {
        this.search = search;
        this.eventBus = eventBus;
    }

    public void attachView(ResultsView resultsView) {
        this.resultsView = resultsView;
        if (searchResultItemList != null) {
            resultsView.displayResults(searchResultItemList);
        }
        if (queryParams == null) {
            disableRefresh();
        } else {
            enableRefresh();
        }
    }

    private void enableRefresh() {
        resultsView.enableRefresh();
    }

    private void disableRefresh() {
        resultsView.disableRefresh();
    }

    private void search(QueryParams queryParams) {
        this.queryParams = queryParams;
        enableRefresh();
        resultsView.displayDataLoading();
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
                        resultsView.hideDataLoading();
                        resultsView.displayErrorMessage();
                    }
                });
        subscriptions.add(subscription);
    }

    private void handleSuccesfullResponse(SearchResults searchResults) {
        resultsView.hideDataLoading();
        if (searchResults.getSearchResultItemList().size() > 0) {
            searchResultItemList = searchResults.getSearchResultItemList();
            resultsView.displayResults(searchResultItemList);
        } else {
            resultsView.displayNoResultsMessage();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchEvent(SearchEvent searchEvent) {
        search(searchEvent.queryParams);
    }

    public void refresh() {
        search(queryParams);
    }

    public void onStart() {
        eventBus.register(this);
    }

    public void onStop() {
        if (subscriptions != null && !subscriptions.isUnsubscribed()) {
            subscriptions.clear();
        }
        eventBus.unregister(this);

    }


}
