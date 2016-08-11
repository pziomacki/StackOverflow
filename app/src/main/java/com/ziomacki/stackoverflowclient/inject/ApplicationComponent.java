package com.ziomacki.stackoverflowclient.inject;

import android.content.Context;

import com.ziomacki.stackoverflowclient.StackOverflowApplication;
import com.ziomacki.stackoverflowclient.search.view.ResultsFragment;

import dagger.Component;
import retrofit2.Retrofit;

@ApplicationScope
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(StackOverflowApplication stackOverflowApplication);
    void inject(ResultsFragment resultsFragment);

    Context appContext();
    Retrofit retrofit();
}
