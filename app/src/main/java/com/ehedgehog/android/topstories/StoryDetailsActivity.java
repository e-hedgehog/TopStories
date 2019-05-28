package com.ehedgehog.android.topstories;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class StoryDetailsActivity extends SingleFragmentActivity {

    private static final String EXTRA_ARTICLE = "com.ehedgehog.android.topstories.article";

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, StoryDetailsActivity.class);
        intent.putExtra(EXTRA_ARTICLE, url);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String article = getIntent().getStringExtra(EXTRA_ARTICLE);
        return StoryDetailsFragment.newInstance(article);
    }
}
