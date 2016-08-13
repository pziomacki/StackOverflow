package com.ziomacki.stackoverflowclient.search.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
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
import com.ziomacki.stackoverflowclient.search.model.Sort;
import com.ziomacki.stackoverflowclient.search.presenter.SearchPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity implements SearchView {
    private static final String FRAGMENT_TAG = "results_tag";

    @BindView(R.id.search_main_container)
    RelativeLayout mainContainer;
    @BindView(R.id.search_edit_text)
    EditText searchEditText;
    @BindView(R.id.search_button)
    ImageButton searchButton;
    @BindView(R.id.search_order)
    Spinner orderSpinner;
    @BindView(R.id.search_sort)
    Spinner sortSpinner;
    @Inject
    SearchPresenter searchPresenter;

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

    private void initViews() {
        addResultsFragment();
        setupOrderSpinner();
        setupSortSpinner();
        searchEditText.clearFocus();
    }

    private void initPresenter(Bundle savedInstanceState) {
        searchPresenter.attachView(this);
        searchPresenter.setInitialQueryParamsIfNotRecreated(savedInstanceState);
    }


    private void addResultsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        searchResultsFragment = (SearchResultsFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (searchResultsFragment == null) {
            searchResultsFragment = SearchResultsFragment.getInstance();
            fragmentManager.beginTransaction().add(R.id.search_fragment_container, searchResultsFragment, FRAGMENT_TAG).commit();
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
        search();
        closeKeyboard();
    }

    private void search() {
        String queryString = searchEditText.getText().toString();
        Order order = (Order) orderSpinner.getSelectedItem();
        Sort sort = (Sort) sortSpinner.getSelectedItem();
        searchPresenter.search(queryString, order, sort);
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
        mainContainer.requestFocus();

    }

    private void displaySnackbar(String message) {
        Snackbar.make(mainContainer, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setQuery(String query) {
        searchEditText.setText(query);
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

    private void setSpinnerSelection(Spinner spinner, int position) {
        if (position != -1) {
            spinner.setSelection(position);
        }
    }

    @Override
    public void setSort(Sort sort) {
        int position = ((ArrayAdapter) sortSpinner.getAdapter()).getPosition(sort);
        setSpinnerSelection(sortSpinner, position);
    }

    private void setupOrderSpinner() {
        ArrayAdapter<Order> orderArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item,
                Order.values());
        orderSpinner.setAdapter(orderArrayAdapter);
    }

    private void setupSortSpinner() {
        ArrayAdapter<Sort> sortArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, Sort
                .values());
        sortSpinner.setAdapter(sortArrayAdapter);
    }

}
