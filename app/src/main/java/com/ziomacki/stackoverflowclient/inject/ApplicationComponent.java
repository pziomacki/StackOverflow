package com.ziomacki.stackoverflowclient.inject;

import android.content.Context;

import com.ziomacki.stackoverflowclient.StackOverflowApplication;

import org.greenrobot.eventbus.EventBus;

import dagger.Component;
import retrofit2.Retrofit;

@ApplicationScope
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(StackOverflowApplication stackOverflowApplication);

    Context appContext();
    Retrofit retrofit();
    EventBus eventBus();
}
