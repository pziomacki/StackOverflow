package com.ziomacki.stackoverflowclient.search.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ziomacki.stackoverflowclient.R;
import com.ziomacki.stackoverflowclient.StackOverflowApplication;
import com.ziomacki.stackoverflowclient.inject.ApplicationComponent;
import com.ziomacki.stackoverflowclient.inject.DaggerSearchComponent;
import com.ziomacki.stackoverflowclient.inject.SearchModule;
import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;
import com.ziomacki.stackoverflowclient.search.presenter.SearchPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity implements SearchView{
    private static final String FRAGMENT_TAG = "results_tag";

    @Bind(R.id.search_edit_text)
    EditText searchEditText;
    @Bind(R.id.search_button)
    ImageButton searchButton;
    @Inject
    SearchPresenter searchPresenter;

    private ResultsFragment resultsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        injectDependencies();
        searchPresenter.attachView(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        resultsFragment = (ResultsFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (resultsFragment == null) {
            resultsFragment = ResultsFragment.getInstance();
            fragmentManager.beginTransaction().add(R.id.search_fragment_container, resultsFragment, FRAGMENT_TAG).commit();
        }

    }

    private void injectDependencies() {
        ApplicationComponent applicationComponent =
                ((StackOverflowApplication) getApplication()).getApplicationComponent();

        DaggerSearchComponent.builder()
                .applicationComponent(applicationComponent)
                .searchModule(new SearchModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        searchPresenter.onStop();
    }

    @OnClick(R.id.search_button)
    public void onSearchButtonClick() {
        searchPresenter.search(searchEditText.getText().toString());
    }

    @Override
    public void displayErrorMessage() {

    }

    @Override
    public void displaySearchResults(List<SearchResultItem> results) {
        resultsFragment.setResults(results);
    }
}
