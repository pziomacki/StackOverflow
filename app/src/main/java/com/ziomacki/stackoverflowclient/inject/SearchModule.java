package com.ziomacki.stackoverflowclient.inject;

import com.ziomacki.stackoverflowclient.search.model.SearchApiService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class SearchModule {

    @Provides
    public SearchApiService provideSearchApiService(Retrofit retrofit) {
        return retrofit.create(SearchApiService.class);
    }

}
