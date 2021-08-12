package com.baki.android.fusedlocationwithbackgroundservice.commons;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static android.content.Context.ACTIVITY_SERVICE;

public class Utils {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getAppState(Context context) {
        try {

            ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskList = activityManager.getRunningTasks(10);

            if (!taskList.isEmpty())
            {
                List<ActivityManager.RunningTaskInfo> taskListMatched = taskList.stream().filter(a->a.topActivity.getClassName().contains("fusedlocationwithbackground")).collect(Collectors.toList());
                if(taskListMatched.isEmpty())
                    return "Killed";
            }

            ActivityManager.RunningAppProcessInfo myApp = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(myApp);
            boolean isInBackground = myApp.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            if(isInBackground)
                return "Background";
            else
                return "Foreground";
        } catch (Exception ex) {
            //handle exception
        }
        return "Foreground";
    }

    public static String getDeviceMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

    public static String getDeviceInfo() {
        try {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            if (model.startsWith(manufacturer)) {
                return capitalize(model);
            }
            return capitalize(manufacturer) + " " + model;
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

}
