package com.ziomacki.stackoverflowclient.inject;

import android.content.Context;
import com.ziomacki.stackoverflowclient.StackOverflowApplication;
import dagger.Component;

@ApplicationScope
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {
    void inject(StackOverflowApplication stackOverflowApplication);

    SearchComponent searchComponent(SearchModule searchModule);
    Context appContext();
}
