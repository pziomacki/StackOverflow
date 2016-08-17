package com.ziomacki.stackoverflowclient.inject;

import android.content.Context;
import android.content.SharedPreferences;

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

    @Provides
    public SharedPreferences provideSearchSharedPreferences(Context context) {
        return context.getSharedPreferences("search_preferences", Context.MODE_PRIVATE);
    }
}
