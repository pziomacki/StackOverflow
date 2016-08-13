package com.ziomacki.stackoverflowclient.search.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.ziomacki.stackoverflowclient.R;
import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsViewHolder> {

    private List<SearchResultItem> resultItemList;


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
        SearchResultItem resultItem = resultItemList.get(position);
        holder.resultItemTitle.setText(resultItem.getTitle());
        Context context = holder.avatar.getContext();
        holder.answersCount.setText(context.getString(R.string.result_answer_count, resultItem.getAnswerCount()));
        holder.ownerName.setText(resultItem.getOwner().getDisplayName());
        String imageUrl = resultItem.getOwner().getProfileImage();
        if (!isEmptyString(imageUrl)) {
            Picasso.with(context)
                    .load(resultItem.getOwner().getProfileImage())
                    .into(holder.avatar);
        }
    }

    private boolean isEmptyString(String string) {
        return string == null || string.equals("");
    }

    @Override
    public int getItemCount() {
        return resultItemList == null ? 0 : resultItemList.size();
    }
}
