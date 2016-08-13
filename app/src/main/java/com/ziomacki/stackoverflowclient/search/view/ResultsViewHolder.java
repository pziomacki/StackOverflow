package com.ziomacki.stackoverflowclient.search.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziomacki.stackoverflowclient.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.result_item_title)
    TextView resultItemTitle;
    @BindView(R.id.result_item_count)
    TextView answersCount;
    @BindView(R.id.result_item_name)
    TextView ownerName;
    @BindView(R.id.result_item_avatar)
    ImageView avatar;

    public ResultsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
