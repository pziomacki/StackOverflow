package com.ziomacki.stackoverflowclient.search.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.jakewharton.rxbinding.widget.RxSearchView;
import com.jakewharton.rxbinding.widget.SearchViewQueryTextEvent;
import com.ziomacki.stackoverflowclient.R;
import com.ziomacki.stackoverflowclient.StackOverflowApplication;
import com.ziomacki.stackoverflowclient.inject.ApplicationComponent;
import com.ziomacki.stackoverflowclient.inject.SearchModule;
import com.ziomacki.stackoverflowclient.search.model.Order;
import com.ziomacki.stackoverflowclient.search.model.QueryParams;
import com.ziomacki.stackoverflowclient.search.model.Sort;
import com.ziomacki.stackoverflowclient.search.presenter.SearchPresenter;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

public class SearchActivity extends AppCompatActivity implements SearchView {
    private static final String FRAGMENT_TAG = "results_tag";

    @BindView(R.id.search_main_container)
    ViewGroup mainContainer;
    @BindView(R.id.search_order)
    Spinner orderSpinner;
    @BindView(R.id.search_sort)
    Spinner sortSpinner;
    @BindView(R.id.search_search_view)
    android.widget.SearchView searchView;
    @BindView(R.id.search_toolbar)
    Toolbar toolbar;
    @Inject
    SearchPresenter searchPresenter;

    private CompositeSubscription subscriptions = new CompositeSubscription();

    private SearchResultsFragment searchResultsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        injectDependencies();
        initViews();
        initPresenter(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        searchPresenter.onSaveInstance(getQueryParams());
    }

    private void initViews() {
        addResultsFragment();
        setupOrderAdapter();
        setSortAdapter();
        setSupportActionBar(toolbar);
        setupSearchView();
    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
    }

    private void initPresenter(Bundle savedInstanceState) {
        searchPresenter.attachView(this);
        searchPresenter.setInitialQueryParams(savedInstanceState);
    }


    private void addResultsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        searchResultsFragment =
                (SearchResultsFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (searchResultsFragment == null) {
            searchResultsFragment = SearchResultsFragment.getInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.search_fragment_container, searchResultsFragment, FRAGMENT_TAG).commit();
        }
    }

    private void injectDependencies() {
        ApplicationComponent applicationComponent =
                ((StackOverflowApplication) getApplication()).getApplicationComponent();
        applicationComponent.searchComponent(new SearchModule()).inject(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        searchPresenter.onStop();
        subscriptions.clear();
    }

    private void onSearchAction() {
        QueryParams queryParams = getQueryParams();
        searchPresenter.onSearchActionPerformed(queryParams);
    }

    @Override
    public void search(QueryParams queryParams) {
        searchResultsFragment.search(queryParams);
    }

    @Override
    public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        mainContainer.requestFocus();
    }

    private void displaySnackbar(String message) {
        Snackbar.make(mainContainer, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayEmptyQueryMessage() {
        displaySnackbar(getString(R.string.search_empty_query));
    }

    private void setSpinnerSelection(Spinner spinner, int position) {
        if (position != -1) {
            spinner.setSelection(position);
        }
    }

    @Override
    public void initQuery(String query) {
        searchView.setQuery(query, false);
        RxSearchView.queryTextChangeEvents(searchView).filter(new Func1<SearchViewQueryTextEvent, Boolean>() {
            @Override
            public Boolean call(SearchViewQueryTextEvent event) {
                return event.isSubmitted();
            }
        }).subscribe(new SearchQueryEdit());

    }

    @Override
    public void initSortValue(Sort sort) {
        int position = ((ArrayAdapter) sortSpinner.getAdapter()).getPosition(sort);
        setSpinnerSelection(sortSpinner, position);
        setSpinnerSubscriber(sortSpinner);
    }

    @Override
    public void initOrderValue(Order order) {
        int position = ((ArrayAdapter) orderSpinner.getAdapter()).getPosition(order);
        setSpinnerSelection(orderSpinner, position);
        setSpinnerSubscriber(orderSpinner);
    }

    private void setupOrderAdapter() {
        ArrayAdapter<Order> orderArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item,
                Order.values());
        orderSpinner.setAdapter(orderArrayAdapter);
    }

    private void setSortAdapter() {
        ArrayAdapter<Sort> sortArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, Sort
                .values());
        sortSpinner.setAdapter(sortArrayAdapter);
    }

    private void setSpinnerSubscriber(Spinner spinner) {
        Subscription subscription = RxAdapterView.itemSelections(spinner).distinctUntilChanged().skip(1)
                .subscribe(new SpinnerSelect());
        subscriptions.add(subscription);
    }

    private QueryParams getQueryParams() {
        String query = searchView.getQuery().toString();
        Order order = (Order) orderSpinner.getSelectedItem();
        Sort sort = (Sort) sortSpinner.getSelectedItem();
        return new QueryParams.Builder()
                .query(query)
                .order(order)
                .sort(sort)
                .build();
    }

    private class SearchQueryEdit implements Action1<SearchViewQueryTextEvent> {
        @Override
        public void call(SearchViewQueryTextEvent event) {
            onSearchAction();
        }
    }

    private class SpinnerSelect implements Action1<Integer> {
        @Override
        public void call(Integer position) {
            onSearchAction();
        }
    }
}
