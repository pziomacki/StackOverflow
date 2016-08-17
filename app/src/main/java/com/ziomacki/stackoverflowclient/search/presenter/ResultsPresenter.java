package com.ziomacki.stackoverflowclient.search.presenter;

import com.ziomacki.stackoverflowclient.search.model.QueryParams;
import com.ziomacki.stackoverflowclient.search.model.Search;
import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;
import com.ziomacki.stackoverflowclient.search.model.SearchResults;
import com.ziomacki.stackoverflowclient.search.view.ResultsView;

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

    private Action1 fetchSuccesfulResonseAction = new Action1<SearchResults>() {
        @Override
        public void call(SearchResults searchResults) {
            resultsView.hideDataLoading();
            if (searchResults.getSearchResultItemList().size() > 0) {
                searchResultItemList = searchResults.getSearchResultItemList();
                resultsView.displayResults(searchResultItemList);
            } else {
                resultsView.displayNoResultsMessage();
            }
        }
    };

    private Action1 fetchErrorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            //TODO: handle specific messages
            resultsView.hideDataLoading();
            resultsView.displayErrorMessage();
        }
    };

    @Inject
    public ResultsPresenter(Search search) {
        this.search = search;
    }

    public void attachView(ResultsView resultsView) {
        this.resultsView = resultsView;
        displayResutlsIfAvailable(resultsView);
        initRefreshFeature();
    }

    private void displayResutlsIfAvailable(ResultsView resultsView) {
        if (searchResultItemList != null) {
            resultsView.displayResults(searchResultItemList);
        }
    }

    private void initRefreshFeature() {
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

    public void search(QueryParams queryParams) {
        this.queryParams = queryParams;
        enableRefresh();
        resultsView.displayDataLoading();
        Subscription subscription = search.startSearch(queryParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fetchSuccesfulResonseAction, fetchErrorAction);
        subscriptions.add(subscription);
    }

    public void resultItemSelected(String detailsUrl) {
        resultsView.displayDetails(detailsUrl);
    }

    public void refresh() {
        search(queryParams);
    }

    public void onStop() {
        subscriptions.clear();
    }
}
