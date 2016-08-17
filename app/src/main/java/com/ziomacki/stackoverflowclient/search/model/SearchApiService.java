package com.ziomacki.stackoverflowclient.search.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface SearchApiService {

    @GET("/2.2/search?site=stackoverflow")
    Observable<SearchResults> fetchSearchResults(@Query("intitle") String searchQuery,
                                                 @Query("sort") Sort sort,
                                                 @Query("order") Order order);
}
