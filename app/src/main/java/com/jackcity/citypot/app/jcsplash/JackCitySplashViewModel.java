package com.jackcity.citypot.app.jcsplash;

import android.app.Activity;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.jackcity.citypot.app.R;
import com.jackcity.citypot.app.jcview.JackCityViewViewModel;

import java.util.concurrent.TimeUnit;

public class JackCitySplashViewModel extends ViewModel {

    MutableLiveData<AsyncTask<Void, Void, Void>> mutableLiveData;
    Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setLazyLoader(LazyLoader lazyLoader) {
        lazyLoader.setAnimDuration(500);
        lazyLoader.setFirstDelayDuration(100);
        lazyLoader.setSecondDelayDuration(200);
    }

    public JackCitySplashViewModel()
    {
        mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void unused) {
                NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_jackCitySplash_to_jackCityView);
            }

            @Override

            protected Void doInBackground(Void... voids) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    public MutableLiveData<AsyncTask<Void, Void, Void>> getMutableLiveData() {
        return mutableLiveData;
    }
}