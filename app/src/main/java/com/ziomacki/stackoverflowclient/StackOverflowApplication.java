package com.ziomacki.stackoverflowclient;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.ziomacki.stackoverflowclient.inject.ApplicationComponent;
import com.ziomacki.stackoverflowclient.inject.ApplicationModule;
import com.ziomacki.stackoverflowclient.inject.DaggerApplicationComponent;

public class StackOverflowApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initApplicationComponent();
        LeakCanary.install(this);
    }

    private void initApplicationComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
