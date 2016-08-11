package com.ziomacki.stackoverflowclient.search.view;

import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;

import java.util.List;

public interface SearchView {
    void displayErrorMessage();
    void displaySearchResults(List<SearchResultItem> results);
}
