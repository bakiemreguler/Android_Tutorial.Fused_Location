package com.baki.android.fusedlocationwithbackgroundservice.service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.baki.android.fusedlocationwithbackgroundservice.R;
import com.baki.android.fusedlocationwithbackgroundservice.commons.Constants;
import com.baki.android.fusedlocationwithbackgroundservice.commons.Utils;
import com.baki.android.fusedlocationwithbackgroundservice.model.AddLocationDto;
import com.baki.android.fusedlocationwithbackgroundservice.model.ApiResponse;
import com.baki.android.fusedlocationwithbackgroundservice.webservice.ApiClient;
import com.baki.android.fusedlocationwithbackgroundservice.webservice.RestInterface;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends Service {

    private LocationCallback locationCallback = new LocationCallback() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            addLocationData(locationResult);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("MissingPermission")
    private void startLocationService(){

        String channelId = "locationNotificationChannel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Location Service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("App is running!");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(notificationManager != null && notificationManager.getNotificationChannel(channelId) == null){
                NotificationChannel notificationChannel = new NotificationChannel(channelId, "Location Service", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("This channel is used by location service.");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(6000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        startForeground(Constants.LOCATION_SERVICE_ID, builder.build());
    }

    private void stopLocationService(){
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            String action = intent.getAction();
            if(action != null){
                if(action.equals(Constants.ACTION_START_LOCATION_SERVICE)){
                    startLocationService();
                }
                else if(action.equals(Constants.ACTION_STOP_LOCATION_SERVICE)){
                    stopLocationService();
                }
            }
        }
        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addLocationData(LocationResult locationResult){
        if(locationResult != null && locationResult.getLastLocation() != null){
            double latitude = locationResult.getLastLocation().getLatitude();
            double longitude = locationResult.getLastLocation().getLongitude();

            RestInterface apiInterface = ApiClient.GetApiClient().create(RestInterface.class);

            AddLocationDto addLocationDto = new AddLocationDto(String.valueOf(latitude), String.valueOf(longitude), "Address", "Fused Location Service", "Fused Location Service", Utils.getDeviceInfo(), Utils.getDeviceMacAddress(), Utils.getAppState(getApplicationContext()));
            Call<ApiResponse<Integer>> request = apiInterface.AddNewLocationData(addLocationDto);

            request.enqueue(new Callback<ApiResponse<Integer>>() {
                @Override
                public void onResponse(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                }
                @Override
                public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                }
            });
        }
    }
}
