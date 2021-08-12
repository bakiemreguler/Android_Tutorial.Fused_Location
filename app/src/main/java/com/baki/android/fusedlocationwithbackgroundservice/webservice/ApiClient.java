package com.baki.android.fusedlocationwithbackgroundservice.webservice;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofitClient = null;
    private static final String baseUrl = "http://192.168.1.20:9090/api/";

    public static Retrofit GetApiClient(){
        if(retrofitClient == null){
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + "Example Token Value")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();

            retrofitClient = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            return retrofitClient;
        }
        return retrofitClient;
    }
}
