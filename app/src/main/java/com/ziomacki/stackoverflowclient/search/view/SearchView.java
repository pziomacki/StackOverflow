package com.ziomacki.stackoverflowclient.search.view;

import com.ziomacki.stackoverflowclient.search.model.Order;
import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;
import com.ziomacki.stackoverflowclient.search.model.Sort;

import java.util.List;

public interface SearchView {
    void displayErrorMessage();
    void displaySearchResults(List<SearchResultItem> results);
    void displayNoResultsMessage();
    void setQuery(String query);
    void setOrder(Order order);
    void setSort(Sort sort);
    void displayDataLoading();
    void hideDataLoading();
    void displayEmptyQueryMessage();
}
