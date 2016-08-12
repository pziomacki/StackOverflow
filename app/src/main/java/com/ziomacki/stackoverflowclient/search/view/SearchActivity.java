package com.ziomacki.stackoverflowclient.search.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.ziomacki.stackoverflowclient.R;
import com.ziomacki.stackoverflowclient.StackOverflowApplication;
import com.ziomacki.stackoverflowclient.inject.ApplicationComponent;
import com.ziomacki.stackoverflowclient.inject.DaggerSearchComponent;
import com.ziomacki.stackoverflowclient.inject.SearchModule;
import com.ziomacki.stackoverflowclient.search.model.Order;
import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;
import com.ziomacki.stackoverflowclient.search.model.Sort;
import com.ziomacki.stackoverflowclient.search.presenter.SearchPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity implements SearchView {
    private static final String FRAGMENT_TAG = "results_tag";

    @Bind(R.id.search_main_container)
    RelativeLayout mainContainer;
    @Bind(R.id.search_edit_text)
    EditText searchEditText;
    @Bind(R.id.search_button)
    ImageButton searchButton;
    @Bind(R.id.search_fragment_container)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.search_order)
    Spinner orderSpinner;
    @Inject
    SearchPresenter searchPresenter;

    private ResultsFragment resultsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        injectDependencies();
        initViews();
        initPresenter(savedInstanceState);

    }

    private void initViews() {
        addResultsFragment();
        setupRefreshLayout();
        setupOrderSpinner();
    }

    private void initPresenter(Bundle savedInstanceState) {
        searchPresenter.attachView(this);
        searchPresenter.setInitialQueryParamsIfNotRecreated(savedInstanceState);
    }

    private void setupRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchPresenter.refresh();
            }
        });
    }

    private void addResultsFragment() {
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
        Order order = (Order) orderSpinner.getSelectedItem();
        searchPresenter.search(searchEditText.getText().toString(), order);
    }

    @Override
    public void displayErrorMessage() {
        displaySnackbar(getString(R.string.search_error_message));
    }

    private void displaySnackbar(String message) {
        Snackbar.make(mainContainer, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displaySearchResults(List<SearchResultItem> results) {
        resultsFragment.setResults(results);
    }

    @Override
    public void displayNoResultsMessage() {
        displaySnackbar(getString(R.string.search_no_results));
    }

    @Override
    public void setQuery(String query) {
        searchEditText.setText(query);
    }

    @Override
    public void displayDataLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideDataLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void displayEmptyQueryMessage() {
        displaySnackbar(getString(R.string.search_empty_query));
    }

    @Override
    public void setOrder(Order order) {
        int position = ((ArrayAdapter) orderSpinner.getAdapter()).getPosition(order);
        setSpinnerSelection(orderSpinner, position);
    }

    private void setSpinnerSelection(Spinner spinner, int position){
        if (position != -1) {
            spinner.setSelection(position);
        }
    }

    @Override
    public void setSort(Sort sort) {
        //TODO: implement
    }

    private void setupOrderSpinner() {
        ArrayAdapter<Order> orderArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_selectable_list_item,
                Order.values());
        orderSpinner.setAdapter(orderArrayAdapter);
    }
    
}
