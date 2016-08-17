package com.ziomacki.stackoverflowclient.inject;

import com.ziomacki.stackoverflowclient.search.view.SearchActivity;
import com.ziomacki.stackoverflowclient.search.view.SearchResultsFragment;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {SearchModule.class})
public interface SearchComponent {
    void inject(SearchActivity searchActivity);
    void inject(SearchResultsFragment searchResultsFragment);
}
