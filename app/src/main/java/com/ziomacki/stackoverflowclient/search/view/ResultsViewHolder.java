package com.ziomacki.stackoverflowclient.search.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ziomacki.stackoverflowclient.R;
import com.ziomacki.stackoverflowclient.search.eventbus.ResultItemClickEvent;
import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;

import org.greenrobot.eventbus.EventBus;

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
    @BindView(R.id.result_item_container)
    RelativeLayout mainContainer;

    private SearchResultItem resultItem;
    private EventBus eventBus;

    public ResultsViewHolder(View itemView, EventBus eventBus) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.eventBus = eventBus;
    }

    public void bind(SearchResultItem resultItem) {
        this.resultItem = resultItem;
        resultItemTitle.setText(resultItem.getTitle());
        Context context = avatar.getContext();
        answersCount.setText(context.getString(R.string.result_answer_count, resultItem.getAnswerCount()));
        ownerName.setText(resultItem.getOwner().getDisplayName());
        loadAvatar(avatar, context);
        setOnclickListener();
    }

    private void loadAvatar(ImageView imageView, Context context) {
        String imageUrl = resultItem.getOwner().getProfileImage();
        if (isStringNotEmpty(imageUrl)) {
            Picasso.with(context)
                    .load(resultItem.getOwner().getProfileImage())
                    .into(imageView);
        }
    }

    private boolean isStringNotEmpty(String string) {
        return string != null && !string.equals("");
    }

    private void setOnclickListener() {
        if (isStringNotEmpty(resultItem.getLink())) {
            mainContainer.setOnClickListener(new OnResultsItemClickListener(eventBus, resultItem.getLink()));
        }
    }

    private static class OnResultsItemClickListener implements View.OnClickListener {
        private String url;
        private EventBus eventBus;

        public OnResultsItemClickListener(EventBus eventBus, String url) {
            this.url = url;
            this.eventBus = eventBus;
        }

        @Override
        public void onClick(View view) {
            eventBus.post(new ResultItemClickEvent(url));
        }
    }
}
