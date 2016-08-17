package com.ziomacki.stackoverflowclient.inject;

import android.content.Context;

import com.ziomacki.stackoverflowclient.search.eventbus.StackOverflowBusIndex;

import org.greenrobot.eventbus.EventBus;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final Context appContext;
    private final EventBus eventBus;

    public ApplicationModule(Context appContext) {
        this.appContext = appContext;
        setupBusIndex();
        eventBus = EventBus.getDefault();
    }

    private void setupBusIndex() {
        EventBus.builder().addIndex(new StackOverflowBusIndex()).installDefaultEventBus();
    }

    @Provides
    public Context provideApplicationContext() {
        return appContext;
    }

    @Provides
    @ApplicationScope
    public EventBus provideEventBus() {
        return eventBus;
    }
}
