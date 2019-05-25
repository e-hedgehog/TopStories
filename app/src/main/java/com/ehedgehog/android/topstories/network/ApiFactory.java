package com.ehedgehog.android.topstories.network;

import com.ehedgehog.android.topstories.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    private static OkHttpClient sClient;
    private volatile static NewsService sService;

    public ApiFactory() {
    }

    public static NewsService buildNewsService() {
        NewsService service = sService;
        if (service == null) {
            synchronized (ApiFactory.class) {
                service = sService;
                if (service == null)
                    service = sService = buildRetrofit().create(NewsService.class);
            }
        }
        return service;
    }

    private static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_ENDPOINT)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static OkHttpClient getClient() {
        OkHttpClient client = sClient;
        if (client == null) {
            synchronized (ApiFactory.class) {
                client = sClient;
                if (client == null)
                    client = sClient = buildClient();
            }
        }
        return client;
    }

    private static OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor())
                .addInterceptor(new ApiKeyInterceptor())
                .build();
    }
}
