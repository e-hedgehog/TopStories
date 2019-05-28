package com.ehedgehog.android.topstories;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ehedgehog.android.topstories.model.Article;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class StoryDetailsFragment extends Fragment {

    private static final String TAG = "StoryDetailsFragment";
    private static final String ARG_ARTICLE = "article";

    private ImageView mStoryImage;
    private TextView mTitleTextView;
    private TextView mAbstractTextView;
    private TextView mPublishedTextView;
    private TextView mBylineTextView;

    private Article mArticle;

    public static StoryDetailsFragment newInstance(String article) {
        Bundle args = new Bundle();
        args.putString(ARG_ARTICLE, article);

        StoryDetailsFragment fragment = new StoryDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            String url = getArguments().getString(ARG_ARTICLE);
            Realm realm = Realm.getDefaultInstance();
            mArticle = realm.where(Article.class).equalTo("mUrl", url).findFirst();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_details, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().setSubtitle(R.string.details_subtitle);
        }

        mStoryImage = view.findViewById(R.id.story_image);
        Log.i(TAG, mArticle.getMultimedia().get(4).getUrl());
        Picasso.get().load(mArticle.getMultimedia().get(4).getUrl()).into(mStoryImage);

        mTitleTextView = view.findViewById(R.id.story_title);
        mTitleTextView.setText(mArticle.getTitle());

        mAbstractTextView = view.findViewById(R.id.story_abstract);
        mAbstractTextView.setText(mArticle.getAbstract());

        mPublishedTextView = view.findViewById(R.id.story_published_at);
        mPublishedTextView.setText(formatDate(mArticle.getPublishedDate()));

        mBylineTextView = view.findViewById(R.id.story_byline);
        mBylineTextView.setText(mArticle.getByline());

        return view;
    }

    private String formatDate(String date) {
        DateFormat format = new SimpleDateFormat(
                getResources().getString(R.string.api_date_format), Locale.getDefault());
        Date apiDate = null;
        try {
            apiDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat(
                getResources().getString(R.string.date_format), Locale.getDefault());

        return format.format(apiDate);
    }
}
