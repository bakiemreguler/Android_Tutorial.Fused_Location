package com.baki.android.fusedlocationwithbackgroundservice.webservice;

import com.baki.android.fusedlocationwithbackgroundservice.model.AddLocationDto;
import com.baki.android.fusedlocationwithbackgroundservice.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestInterface {
    @POST("Location/AddNewLocationData")
    Call<ApiResponse<Integer>> AddNewLocationData(@Body AddLocationDto addLocationDto);
}
