package com.zuci.zuciapp.network.entities;

public class ApiError {
    public String code;
    public String detail;

    @Override
    public String toString() {
        return code + "-" + detail;
    }
}
