package com.ziomacki.stackoverflowclient.inject;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private Context appContext;

    public ApplicationModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    public Context provideApplicationContext() {
        return appContext;
    }

}
