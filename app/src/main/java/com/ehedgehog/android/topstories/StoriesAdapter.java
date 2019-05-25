package com.ehedgehog.android.topstories;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ehedgehog.android.topstories.model.Article;

import java.util.List;

public class StoriesAdapter extends RecyclerView.Adapter<StoryHolder> {

    private Context mContext;
    private List<Article> mArticles;

    public StoriesAdapter(Context context, List<Article> articles) {
        mContext = context;
        mArticles = articles;
    }

    @NonNull
    @Override
    public StoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.article_item, viewGroup, false);
        return new StoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryHolder storyHolder, int i) {
        storyHolder.bind(mArticles.get(i));
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public void setArticles(List<Article> articles) {
        mArticles = articles;
    }

    public void addAll(List<Article> articles) {
        mArticles.addAll(articles);
    }
}
