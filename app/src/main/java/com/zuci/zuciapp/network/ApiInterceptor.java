package com.zuci.zuciapp.network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.zuci.zuciapp.domain.pref.SharedPref;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public final class ApiInterceptor implements Interceptor {

    private SharedPref sharedPref;

    @Inject
    ApiInterceptor(SharedPref sharedPref) {
        this.sharedPref = sharedPref;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        // build new request
        Request.Builder builder = request.newBuilder();

        /*
                .addHeader("Accept","application/json")
                .addHeader("Authorization","Bearer "+preferences.getString("token",""));
                */

        request = builder.header("Accept","application/json")
                .header("Authorization", "Bearer " + sharedPref.getToken())
                .method(request.method(), request.body())
                .build();
        Response response = chain.proceed(request);

        if (response.body() != null) {
            String responseBody = getResponseString(response);
            Log.e("Api", "Response::" + responseBody);
        }

        return response;
    }

    /**
     * Returns a copy of String {@code response.body()} without consuming it.
     */
    private String getResponseString(Response response) {
        try {
            ResponseBody responseBody = response.body();
            BufferedSource source = Objects.requireNonNull(responseBody).source();
            source.request(Long.MAX_VALUE); // request the entire body.
            Buffer buffer = source.buffer();
            // clone buffer before reading from it
            return buffer.clone().readString(Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
