package com.ziomacki.stackoverflowclient.search.presenter;

import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;

import java.util.List;

import javax.inject.Inject;

public class ResultsPresenter {

    private List<SearchResultItem> searchResultItemList;

    @Inject
    public ResultsPresenter() {

    }

    public void setSearchResultItemList(List<SearchResultItem> searchResultItemList) {
        this.searchResultItemList = searchResultItemList;
    }
}
