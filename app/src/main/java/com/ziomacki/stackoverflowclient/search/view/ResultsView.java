package com.ziomacki.stackoverflowclient.search.view;

import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;

import java.util.List;

public interface ResultsView {

    void displayResults(List<SearchResultItem> resultItemList);

}
