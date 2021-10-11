package com.jackcity.citypot.app.jcapp;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.jackcity.citypot.app.JackCityConst;
import com.jackcity.citypot.app.R;
import com.onesignal.OneSignal;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class JackCityApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        JackCityConst.flyerID = AppsFlyerLib.getInstance().getAppsFlyerUID(this);
        Log.e("flyerID", JackCityConst.flyerID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName();
            if (getPackageName() != processName)
                WebView.setDataDirectorySuffix(processName);
        }
        MobileAds.initialize(this);

        AsyncTask.execute(() -> {
            try {
                AdvertisingIdClient.Info adInfo= AdvertisingIdClient.getAdvertisingIdInfo(this);
                JackCityConst.AID =adInfo.getId();
                Log.e("AID", JackCityConst.AID);
            } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            }
        });

        AppsFlyerConversionListener appsWildosWaveListener=new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> map) {
                for (String attrWildosWaveName:map.keySet())
                    Log.d("Conversation Atrr","Conversation Atrr : "+attrWildosWaveName+" = " +map.get(attrWildosWaveName));

                if (Objects.equals(map.get("af_status"), "Organic")){
                    JackCityConst.afStatus ="Organic";
                }
                else {
                    JackCityConst.afStatus ="Non-Organic";

                    String campaign_WildosWave= Objects.requireNonNull(map.get("campaign")).toString();
                    Log.e("APS", "campaing: "+campaign_WildosWave);

                    String []splitWildosWave_Str=campaign_WildosWave.split("\\|\\|");
                    String oneWildosWave=splitWildosWave_Str[1];
                    String twoWildosWave=splitWildosWave_Str[2];
                    String threeWildosWave=splitWildosWave_Str[3];
                    String []splitSubOneWildosWave=oneWildosWave.split(":");
                    String []splitSubTwoWildosWave=twoWildosWave.split(":");
                    String []splitSubThreeWildosWave=threeWildosWave.split(":");

                    JackCityConst.subKey = (!splitSubOneWildosWave[1].equals("")) ? splitSubOneWildosWave[1] : null;
                    JackCityConst.sub3 =(!splitSubTwoWildosWave[1].equals("")) ? splitSubTwoWildosWave[1] : null;
                    JackCityConst.sub4 =(!splitSubThreeWildosWave[1].equals("")) ? splitSubTwoWildosWave[1] : null;
                    Log.e("subKey", JackCityConst.subKey);
                    Log.e("sub3", JackCityConst.sub3);
                    Log.e("sub4", JackCityConst.sub4);

                    JackCityConst.adGroup = Objects.requireNonNull(map.get("adgroup")).toString();
                    JackCityConst.adSet = Objects.requireNonNull(map.get("adset")).toString();
                    JackCityConst.afChannel = Objects.requireNonNull(map.get("af_channel")).toString();
                    JackCityConst.mediaSource = Objects.requireNonNull(map.get("media_source")).toString();

                }

            }

            @Override
            public void onConversionDataFail(String s) { }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) { }

            @Override
            public void onAttributionFailure(String s) { }
        };

        AppsFlyerLib.getInstance().init(new String(Base64.decode(getApplicationContext().getResources().getString(R.string.appflyer), Base64.DEFAULT)),
                appsWildosWaveListener,
                getApplicationContext());
        AppsFlyerLib.getInstance().start(this);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE,OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(getApplicationContext());
        OneSignal.setAppId(new String(Base64.decode(getApplicationContext().getResources().getString(R.string.onesignal), Base64.DEFAULT)));
    }
}
