package com.zuci.zuciapp.network.entities;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTipsGoResponse<T> {
    public T data;
    public List<ApiError> errors = new ArrayList<>();
}
