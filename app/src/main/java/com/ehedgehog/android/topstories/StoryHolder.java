package com.ehedgehog.android.topstories;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ehedgehog.android.topstories.model.Article;
import com.squareup.picasso.Picasso;

public class StoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView mArticleImage;
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;

    private Article mArticle;

    public StoryHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        mArticleImage = itemView.findViewById(R.id.article_item_image);
        mTitleTextView = itemView.findViewById(R.id.article_item_title);
        mDescriptionTextView = itemView.findViewById(R.id.article_item_description);
    }

    public void bind(Article article) {
        mArticle = article;

        mTitleTextView.setText(article.getTitle());
        mDescriptionTextView.setText(article.getByline());
        if (!article.getMultimedia().isEmpty())
            Picasso.get().load(article.getMultimedia().get(0).getUrl())
                    .fit().centerCrop().into(mArticleImage);
        else
            mArticleImage.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        Intent i = StoryDetailsActivity.newIntent(context, mArticle.getUrl());
        context.startActivity(i);
    }
}
