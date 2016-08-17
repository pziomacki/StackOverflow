package com.ziomacki.stackoverflowclient.search.model;

import com.google.gson.annotations.SerializedName;
import io.realm.RealmList;
import io.realm.RealmObject;

public class SearchResults extends RealmObject{

    @SerializedName("items")
    private RealmList<SearchResultItem> searchResultItemList;
    @SerializedName("has_more")
    private boolean hasMore;

    public RealmList<SearchResultItem> getSearchResultItemList() {
        return searchResultItemList;
    }

    public void setSearchResultItemList(RealmList<SearchResultItem> searchResultItemList) {
        this.searchResultItemList = searchResultItemList;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
