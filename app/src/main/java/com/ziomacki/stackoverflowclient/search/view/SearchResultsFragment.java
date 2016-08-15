package com.ziomacki.stackoverflowclient.search.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziomacki.stackoverflowclient.R;
import com.ziomacki.stackoverflowclient.StackOverflowApplication;
import com.ziomacki.stackoverflowclient.inject.ApplicationComponent;
import com.ziomacki.stackoverflowclient.inject.SearchModule;
import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;
import com.ziomacki.stackoverflowclient.search.presenter.ResultsPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultsFragment extends Fragment implements ResultsView {

    @Inject
    ResultsPresenter resultsPresenter;
    @BindView(R.id.results_recycler_view)
    RecyclerView resultsRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private ResultsAdapter resultsAdapter;

    public static SearchResultsFragment getInstance() {
        return new SearchResultsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ApplicationComponent applicationComponent =
                ((StackOverflowApplication) getActivity().getApplication()).getApplicationComponent();
        applicationComponent.searchComponent(new SearchModule()).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);
        initRecyclerView();
        resultsPresenter.attachView(this);
        setupRefreshLayout();
        return rootView;
    }

    private void initRecyclerView() {
        resultsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ListDivider listDivider = new ListDivider(getContext());
        resultsRecyclerView.setLayoutManager(linearLayoutManager);
        resultsAdapter = new ResultsAdapter();
        resultsRecyclerView.setAdapter(resultsAdapter);
        resultsRecyclerView.addItemDecoration(listDivider);
    }

    private void setupRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resultsPresenter.refresh();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        resultsPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        resultsPresenter.onStop();
    }

    @Override
    public void displayResults(List<SearchResultItem> resultItemList) {
        resultsAdapter.setResult(resultItemList);
    }


    private void displaySnackbar(String message) {
        Snackbar.make(swipeRefreshLayout, message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void displayErrorMessage() {
        displaySnackbar(getString(R.string.search_error_message));
    }

    @Override
    public void displayNoResultsMessage() {
        displaySnackbar(getString(R.string.search_no_results));
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
    public void enableRefresh() {
        swipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void disableRefresh() {
        swipeRefreshLayout.setEnabled(false);
    }
}
