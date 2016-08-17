package com.ziomacki.stackoverflowclient.search.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SearchResults {

    @SerializedName("items")
    private List<SearchResultItem> searchResultItemList;
    @SerializedName("has_more")
    private boolean hasMore;

    public List<SearchResultItem> getSearchResultItemList() {
        return searchResultItemList;
    }

    public void setSearchResultItemList(List<SearchResultItem> searchResultItemList) {
        this.searchResultItemList = searchResultItemList;
    }

    public boolean isHasMore() {
        return hasMore;
    }
}
