package com.ziomacki.stackoverflowclient.search.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ziomacki.stackoverflowclient.R;
import com.ziomacki.stackoverflowclient.search.model.SearchResultItem;

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

    SearchResultItem resultItem;

    public ResultsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
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
        if (!isEmptyString(imageUrl)) {
            Picasso.with(context)
                    .load(resultItem.getOwner().getProfileImage())
                    .into(imageView);
        }
    }

    private boolean isEmptyString(String string) {
        return string == null || string.equals("");
    }

    private void setOnclickListener() {
        if (!isEmptyString(resultItem.getLink())) {
            mainContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resultItem.getLink()));
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
