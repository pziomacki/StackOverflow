package com.ziomacki.stackoverflowclient.search.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziomacki.stackoverflowclient.R;
import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsViewHolder> {

    private List<SearchResultItem> resultItemList = Collections.emptyList();
    private EventBus eventBus;
    private Context context;
    @Inject
    public ResultsAdapter(Context context, EventBus eventBus) {
        this.context = context;
        this.eventBus = eventBus;
    }

    public void setResult(List<SearchResultItem> resultItemList) {
        this.resultItemList = resultItemList;
        notifyDataSetChanged();
    }

    @Override
    public ResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item_view, parent, false);
        return new ResultsViewHolder(view, eventBus, context);
    }

    @Override
    public void onBindViewHolder(ResultsViewHolder holder, int position) {
        SearchResultItem resultItem = resultItemList.get(position);
        holder.bind(resultItem);
    }

    @Override
    public int getItemCount() {
        return resultItemList.size();
    }
}
