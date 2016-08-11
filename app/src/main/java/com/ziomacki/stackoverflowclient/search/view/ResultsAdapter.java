package com.ziomacki.stackoverflowclient.search.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziomacki.stackoverflowclient.R;
import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder> {

    private List<SearchResultItem> resultItemList;

    public static class ResultsViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.result_item_title)
        TextView resultItemTitle;

        public ResultsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setResult(List<SearchResultItem> resultItemList) {
        this.resultItemList = resultItemList;
        notifyDataSetChanged();
    }

    @Override
    public ResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item_view, parent, false);
        ResultsViewHolder viewHolder = new ResultsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ResultsViewHolder holder, int position) {
        holder.resultItemTitle.setText(resultItemList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return resultItemList == null ? 0 : resultItemList.size();
    }
}
