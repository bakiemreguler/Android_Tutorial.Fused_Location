package com.baki.android.fusedlocationwithbackgroundservice.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {
    @SerializedName("statusCode")
    public int statusCode;

    @SerializedName("isSuccess")
    public boolean isSuccess;

    @SerializedName("explanation")
    public String explanation;

    @SerializedName("dataToReturn")
    public T dataToReturn;
}
