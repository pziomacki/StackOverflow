package com.ziomacki.stackoverflowclient.search.presenter;

import com.ziomacki.stackoverflowclient.search.model.QueryParams;
import com.ziomacki.stackoverflowclient.search.model.QueryParamsRepository;
import com.ziomacki.stackoverflowclient.search.model.Search;
import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;
import com.ziomacki.stackoverflowclient.search.model.SearchResults;
import com.ziomacki.stackoverflowclient.search.model.SearchResultsRepository;
import com.ziomacki.stackoverflowclient.search.view.ResultsView;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ResultsPresenter {

    private ResultsView resultsView;
    private Search search;
    private QueryParams queryParams;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private QueryParamsRepository queryParamsRepository;
    private SearchResultsRepository searchResultsRepository;

    private Action1 fetchSuccesfulResonseAction = new Action1<SearchResults>() {
        @Override
        public void call(SearchResults searchResults) {
            resultsView.hideDataLoading();
            if (searchResults.getSearchResultItemList().size() > 0) {
                displayResults(searchResults.getSearchResultItemList());
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
    public ResultsPresenter(Search search, QueryParamsRepository queryParamsRepository,
                            SearchResultsRepository searchResultsRepository) {
        this.search = search;
        this.queryParamsRepository = queryParamsRepository;
        this.searchResultsRepository = searchResultsRepository;
    }

    public void attachView(ResultsView resultsView) {
        this.resultsView = resultsView;
        displayResutlsIfStored();
        restoreQueryParams();
    }

    private void restoreQueryParams() {
        Subscription subscription = queryParamsRepository.getQueryParamsObservable()
                .subscribe(new Action1<QueryParams>() {
                    @Override
                    public void call(QueryParams queryParams) {
                        setQueryParams(queryParams);
                    }
                });
        subscriptions.add(subscription);
    }

    private void setQueryParams(QueryParams queryParams) {
        this.queryParams = queryParams;
        initRefreshFeature();
    }

    private void displayResutlsIfStored() {
        Subscription subscription = searchResultsRepository.getSearchResults().subscribe(new Action1<SearchResults>() {
            @Override
            public void call(SearchResults searchResults) {
                if (searchResults.getSearchResultItemList() != null) {
                    displayResults(searchResults.getSearchResultItemList());
                }
            }
        });
        subscriptions.add(subscription);
    }
    private void displayResults(List<SearchResultItem> resultItemList) {
        resultsView.displayResults(resultItemList);
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
