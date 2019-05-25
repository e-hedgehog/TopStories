package com.ehedgehog.android.topstories;

import android.support.v4.app.Fragment;

public class StoriesListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return StoriesListFragment.newInstance();
    }
}
