package com.ehedgehog.android.topstories;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.ehedgehog.android.topstories.model.Article;
import com.ehedgehog.android.topstories.network.ApiFactory;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class StoriesListFragment extends Fragment {

    private static final String TAG = "StoriesListFragment";

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private Spinner mCategorySpinner;

    private StoriesPresenter mPresenter;

    private String mCategory;

    private Disposable mStoriesSubscription;

    public static StoriesListFragment newInstance() {
        return new StoriesListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        isOnline();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stories_list, container, false);

        setupStoriesPresenter();

        mRefreshLayout = view.findViewById(R.id.stories_list_refresh_container);
        setupSwipeRefresh();

        mCategorySpinner = view.findViewById(R.id.stories_category_spinner);
        setupCategorySpinner();

        mProgressBar = view.findViewById(R.id.stories_progress_bar);
        mRecyclerView = view.findViewById(R.id.stories_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        loadStories(mCategory);

        return view;
    }

    @Override
    public void onPause() {
        if (mStoriesSubscription != null)
            mStoriesSubscription.dispose();

        super.onPause();
    }

    private void loadStories(String category) {
        mStoriesSubscription = mPresenter.loadStories(getActivity(), category);
    }

    private void setupStoriesPresenter() {
        mPresenter = new StoriesPresenter(ApiFactory.buildStoriesService());
        mPresenter.addListener(new StoriesPresenter.Listener() {
            @Override
            public void onStoriesLoaded(List<Article> articles) {
                updateUI(articles);
            }

            @Override
            public void onLoadingError(String message) {
                Log.e(TAG, message);
            }
        });
    }

    private void updateUI(List<Article> articles) {
        StoriesAdapter adapter = (StoriesAdapter) mRecyclerView.getAdapter();
        if (adapter == null) {
            adapter = new StoriesAdapter(getActivity(), articles);
            mRecyclerView.setAdapter(adapter);
        } else {
            adapter.setArticles(articles);
            adapter.notifyDataSetChanged();
        }

        mProgressBar.setVisibility(View.GONE);
    }

    private boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null && info.isConnected())
            return true;

        Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void setupSwipeRefresh() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.black,
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_dark
        );
        mRefreshLayout.setOnRefreshListener(() -> {
            mRefreshLayout.setRefreshing(true);
            if (isOnline()) {
                loadStories(mCategory);
            }
            mRefreshLayout.setRefreshing(false);
        });
    }

    private void setupCategorySpinner() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(adapter);

        String[] categories = getActivity()
                .getResources().getStringArray(R.array.categories_array);
        mCategory = categories[StoriesPreferences.getStoredCategory(getActivity())];

        mCategorySpinner.setSelection(StoriesPreferences.getStoredCategory(getActivity()));
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCategory = categories[position];
                StoriesPreferences.setStoredCategory(getActivity(), position);
                mProgressBar.setVisibility(View.VISIBLE);

                loadStories(mCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
