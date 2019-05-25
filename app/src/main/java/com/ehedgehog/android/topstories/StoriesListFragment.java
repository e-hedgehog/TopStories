package com.ehedgehog.android.topstories;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.ehedgehog.android.topstories.model.StoriesResponse;
import com.ehedgehog.android.topstories.network.ApiFactory;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

public class StoriesListFragment extends Fragment {

    private static final String TAG = "StoriesListFragment";

//    private static final int ITEMS_PER_PAGE = 20;

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private Spinner mCategorySpinner;

//    private int mCurrentPage;
    private String mCategory;
    private String mCountry;

//    private int mTotalItems;
//    private int mPagesCount;
    private boolean isLoading = false;

    private Disposable mStoriesSubscription;

    public static StoriesListFragment newInstance() {
        return new StoriesListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        isOnline();

//        mCurrentPage = 1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stories_list, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        }

        mRefreshLayout = view.findViewById(R.id.news_list_refresh_container);
        setupSwipeRefresh();

        mCategorySpinner = view.findViewById(R.id.news_category_spinner);
        setupCategorySpinner();

        mProgressBar = view.findViewById(R.id.news_progress_bar);
        mRecyclerView = view.findViewById(R.id.news_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int visibleItemsCount = layoutManager.getChildCount();
//                int invisibleItemsCount = layoutManager.findFirstVisibleItemPosition();
//                int totalItemsCount = layoutManager.getItemCount();
//                if ((visibleItemsCount + invisibleItemsCount) >= totalItemsCount) {
//                    if ((mCurrentPage <= mPagesCount) && !isLoading) {
//                        Log.i(TAG, "Loading new data...");
//                        mCurrentPage++;
//                        mProgressBar.setVisibility(View.VISIBLE);
//                        loadNews(getAllNews(mCategory));
//                    }
//                }
//            }
//        });


        loadNews(getAllNews(mCategory));

        return view;
    }

    @Override
    public void onPause() {
        if (mStoriesSubscription != null)
            mStoriesSubscription.dispose();

        super.onPause();
    }

    private void loadNews(Observable<StoriesResponse> observable) {
        mStoriesSubscription = observable
                .map(storiesResponse -> {
                    isLoading = true;
                    Log.i(TAG, "Status = " + storiesResponse.getStatus());
                    return storiesResponse.getArticles();
                })
                .flatMap(articles -> {
                    Realm.init(getActivity());
                    Realm.getDefaultInstance().executeTransaction(realm -> {
//                        if (mCurrentPage == 1)
                        realm.delete(Article.class);
                        realm.insert(articles);
                    });
                    return Observable.just(articles);
                })
                .onErrorResumeNext(throwable -> {
                    Log.e(TAG, "Something is wrong", throwable);
                    Realm.init(getActivity());
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Article> results = realm.where(Article.class).findAll();
                    return Observable.just(realm.copyFromRealm(results));
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateUI, throwable ->
                        Log.e(TAG, "Something is wrong", throwable));
    }

    private Observable<StoriesResponse> getAllNews(String category) {
        category = category.toLowerCase().replaceAll("\\s", "");
        Log.i(TAG, "category = " + category);
        return ApiFactory.buildNewsService()
                .getStories(category);
    }

    private void updateUI(List<Article> articles) {
        StoriesAdapter adapter = (StoriesAdapter) mRecyclerView.getAdapter();
        if (adapter == null) {
            adapter = new StoriesAdapter(getActivity(), articles);
            mRecyclerView.setAdapter(adapter);
        } else {
//            if (mCurrentPage == 1) {
                adapter.setArticles(articles);
                adapter.notifyDataSetChanged();
//            } else {
//                adapter.addAll(articles);
//                adapter.notifyItemRangeInserted(
//                        mCurrentPage * ITEMS_PER_PAGE, ITEMS_PER_PAGE);
//            }
        }

        isLoading = false;
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
//            mCurrentPage = 1;
            if (isOnline()) {
                loadNews(getAllNews(mCategory));
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
//                mCurrentPage = 1;
                loadNews(getAllNews(mCategory));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
