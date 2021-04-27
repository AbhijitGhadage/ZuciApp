package com.zuci.zuciapp.utils;

import com.zuci.zuciapp.di.modules.NetworkModule;
import com.zuci.zuciapp.network.entities.BaseTipsGoResponse;

import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import retrofit2.HttpException;

public final class TipsGoUtils {
    public static <T> SingleTransformer<BaseTipsGoResponse<T>, BaseTipsGoResponse<T>> tipsGoApiResponse() {
        return single -> single.flatMap(response -> {
            if (response.errors.size() > 0)
                return Single.error(new Error(response.errors.get(0).detail));

            return Single.just(response);
        });
    }

    public static <T> SingleTransformer<T, T> on404ErrorReturn(T object) {
        return upstream -> upstream.onErrorResumeNext(error -> {
            if (error instanceof HttpException) {
                HttpException httpException = (HttpException) error;
                if (httpException.code() == NetworkModule.TIPSGO_EMPTY_HTTP_CODE) {
                    return Single.just(object);
                }
            }
            return Single.error(error);
        });
    }
}
