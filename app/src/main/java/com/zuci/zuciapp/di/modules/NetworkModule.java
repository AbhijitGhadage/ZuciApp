package com.zuci.zuciapp.di.modules;

import com.zuci.zuciapp.BuildConfig;
import com.zuci.zuciapp.network.ApiInterceptor;
import com.zuci.zuciapp.network.TipsGoApiService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    public static final String BASE_URL = "http://zucci.gtpdemo.co.in/";
    public static final String IMAGE_BASE_URL = "https://s";
    public static final String IMAGE_URL = "http://zucci.gtpdemo.co.in/content/Image/";
    public static final int TIPSGO_EMPTY_HTTP_CODE = 404;

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    TipsGoApiService provideTipsGoApiService(Gson gson, ApiInterceptor apiInterceptor) {
        List<Interceptor> interceptors = new ArrayList<>();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (BuildConfig.DEBUG) {
            interceptors.add(loggingInterceptor);
        }
        interceptors.add(apiInterceptor);

        OkHttpClient okHttpClient = buildOkHttpClient(interceptors);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(TipsGoApiService.class);
    }

    private OkHttpClient buildOkHttpClient(List<Interceptor> interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors)
                builder.addInterceptor(interceptor);
        }

        builder.connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        return builder.build();
    }
}
