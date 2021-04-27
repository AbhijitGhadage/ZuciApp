package com.zuci.zuciapp.network.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zuci.zuciapp.network.Status;
import com.google.gson.JsonElement;

import static com.zuci.zuciapp.network.Status.ERROR;
import static com.zuci.zuciapp.network.Status.LOADING;
import static com.zuci.zuciapp.network.Status.SUCCESS;


public class ApiResponse {
    public final Status status;

    @Nullable
    public final JsonElement responce;

    @Nullable
    public final Throwable error;

    private ApiResponse(Status status, @Nullable JsonElement responce, @Nullable Throwable error) {
        this.status = status;
        this.responce = responce;
        this.error = error;
    }

    public static ApiResponse loading() {
        return new ApiResponse(LOADING, null, null);
    }

    public static ApiResponse success(@NonNull JsonElement responce) {
        return new ApiResponse(SUCCESS, responce, null);
    }

    public static ApiResponse error(@NonNull Throwable error) {
        return new ApiResponse(ERROR, null, error);
    }

}
