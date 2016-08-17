package com.ziomacki.stackoverflowclient.search.view;

import com.ziomacki.stackoverflowclient.search.model.Order;
import com.ziomacki.stackoverflowclient.search.model.QueryParams;
import com.ziomacki.stackoverflowclient.search.model.Sort;

public interface SearchView {
    void setQuery(String query);
    void setOrder(Order order);
    void setSort(Sort sort);
    void displayEmptyQueryMessage();
    void closeKeyboard();
    void search(QueryParams queryParams);
}
