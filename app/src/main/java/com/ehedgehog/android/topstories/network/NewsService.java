package com.ehedgehog.android.topstories.network;

import com.ehedgehog.android.topstories.model.StoriesResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsService {

    @GET("{section}.json")
    Observable<StoriesResponse> getStories(@Path("section") String section);

}
