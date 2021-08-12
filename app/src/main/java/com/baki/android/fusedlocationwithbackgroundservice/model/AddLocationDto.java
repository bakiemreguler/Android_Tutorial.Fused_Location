package com.baki.android.fusedlocationwithbackgroundservice.model;

import com.google.gson.annotations.SerializedName;

public class AddLocationDto {
    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("address")
    private String address;

    @SerializedName("explanation")
    private String explanation;

    @SerializedName("service_title")
    private String service_title;

    @SerializedName("device_title")
    private String device_title;

    @SerializedName("device_mac")
    private String device_mac;

    @SerializedName("app_state")
    private String app_state;

    public AddLocationDto(String latitude, String longitude, String address, String explanation, String service_title, String device_title, String device_mac, String app_state) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.explanation = explanation;
        this.service_title = service_title;
        this.device_title = device_title;
        this.device_mac = device_mac;
        this.app_state = app_state;
    }
}
