package com.ehedgehog.android.topstories;

import android.content.Context;
import android.util.Log;

import com.ehedgehog.android.topstories.model.Article;
import com.ehedgehog.android.topstories.model.StoriesResponse;
import com.ehedgehog.android.topstories.network.ApiFactory;
import com.ehedgehog.android.topstories.network.StoriesService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

public class StoriesPresenter {

    private static final String TAG = "StoriesPresenter";

    public interface Listener {
        void onStoriesLoaded(List<Article> articles);

        void onLoadingError(String message);
    }

    private Listener mListener;
    private final StoriesService mStoriesService;

    public StoriesPresenter(StoriesService service) {
        mStoriesService = service;
    }

    public void addListener(Listener listener) {
        mListener = listener;
    }

    public Disposable loadStories(Context context, String category) {
        return getAllStories(category)
                .map(storiesResponse -> {
                    Log.i(TAG, "Status = " + storiesResponse.getStatus());
                    return storiesResponse.getArticles();
                })
                .flatMap(articles -> {
                    Realm.init(context);
                    Realm.getDefaultInstance().executeTransaction(realm -> {
//                        if (mCurrentPage == 1)
                        realm.delete(Article.class);
                        realm.insert(articles);
                    });
                    return Observable.just(articles);
                })
                .onErrorResumeNext(throwable -> {
                    Log.e(TAG, "Something is wrong", throwable);
                    Realm.init(context);
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Article> results = realm.where(Article.class).findAll();
                    return Observable.just(realm.copyFromRealm(results));
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mListener::onStoriesLoaded, throwable ->
                        mListener.onLoadingError(throwable.getMessage()));
    }

    public Observable<StoriesResponse> getAllStories(String category) {
        category = category.toLowerCase().replaceAll("\\s", "");
        Log.i(TAG, "category = " + category);
        return mStoriesService.getStories(category);
    }
}
