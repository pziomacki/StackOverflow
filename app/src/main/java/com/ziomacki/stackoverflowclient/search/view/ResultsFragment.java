package com.ziomacki.stackoverflowclient.search.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziomacki.stackoverflowclient.R;
import com.ziomacki.stackoverflowclient.StackOverflowApplication;
import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;
import com.ziomacki.stackoverflowclient.search.presenter.ResultsPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResultsFragment extends Fragment implements ResultsView {

    @Inject
    ResultsPresenter resultsPresenter;
    @Bind(R.id.results_recycler_view)
    RecyclerView resultsRecyclerView;

    private ResultsAdapter resultsAdapter;

    public static ResultsFragment getInstance() {
        return new ResultsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((StackOverflowApplication) getActivity().getApplication()).getApplicationComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);
        ButterKnife.bind(this, rootView);
        initRecyclerView();
        resultsPresenter.attachView(this);
        return rootView;
    }

    private void initRecyclerView() {
        resultsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        resultsRecyclerView.setLayoutManager(linearLayoutManager);
        resultsAdapter = new ResultsAdapter();
        resultsRecyclerView.setAdapter(resultsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setResults(List<SearchResultItem> resultItemList) {
        resultsPresenter.setSearchResultItemList(resultItemList);
    }

    @Override
    public void displayResults(List<SearchResultItem> resultItemList) {
        resultsAdapter.setResult(resultItemList);
    }

}
