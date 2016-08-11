package com.ziomacki.stackoverflowclient.search.presenter;

import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;
import com.ziomacki.stackoverflowclient.search.view.ResultsView;

import java.util.List;

import javax.inject.Inject;

public class ResultsPresenter {

    private List<SearchResultItem> searchResultItemList;
    private ResultsView resultsView;

    @Inject
    public ResultsPresenter() {

    }

    public void attachView(ResultsView resultsView) {
        this.resultsView = resultsView;
        if (searchResultItemList != null) {
            resultsView.displayResults(searchResultItemList);
        }
    }

    public void setSearchResultItemList(List<SearchResultItem> searchResultItemList) {
        this.searchResultItemList = searchResultItemList;
        resultsView.displayResults(searchResultItemList);
    }
}
