package com.jackcity.citypot.app.jcview;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.MainThread;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.jackcity.citypot.app.JackCityConst;
import com.jackcity.citypot.app.R;
import com.jackcity.citypot.app.databinding.JackCitySplashFragmentBinding;
import com.jackcity.citypot.app.databinding.JackCityViewFragmentBinding;
import com.jackcity.citypot.app.jcsplash.JackCitySplashViewModel;

public class JackCityView extends Fragment {

    private JackCityViewViewModel mViewModel;
    private JackCityViewFragmentBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(JackCityViewViewModel.class);
        binding = JackCityViewFragmentBinding.inflate(inflater, container, false);

        binding.jcpLoaderView.setAnimDuration(500);
        binding.jcpLoaderView.setFirstDelayDuration(100);
        binding.jcpLoaderView.setSecondDelayDuration(200);

        mViewModel.setData(binding.jcpView, binding.jcpLoaderView, getActivity(), this);
        mViewModel.setSettings();

        mViewModel
                .getWebChromeClientMutableLiveData()
                .observe(getViewLifecycleOwner(), new Observer<WebChromeClient>() {
                    @Override
                    public void onChanged(@Nullable WebChromeClient webChromeClient) {
                        binding.jcpView.setWebChromeClient(webChromeClient);
                    }
                });
        mViewModel
                .getWebViewClientMutableLiveData()
                .observe(getViewLifecycleOwner(), new Observer<WebViewClient>() {
                    @Override
                    public void onChanged(@Nullable WebViewClient webView) {
                        binding.jcpView.setWebViewClient(webView);
                    }
                });
        mViewModel
                .getBroadcastReceiverMutableLiveData()
                .observe(getViewLifecycleOwner(), new Observer<BroadcastReceiver>() {
                    @Override
                    public void onChanged(@Nullable BroadcastReceiver broadcastReceiver) {
                        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
                        getActivity().registerReceiver(broadcastReceiver, intentFilter);
                    }
                });

        binding.jcpView.loadUrl(JackCityConst.jcp_url);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true ) {
            @Override
            @MainThread
            public void handleOnBackPressed() {

                if (binding.jcpView.canGoBack())
                    binding.jcpView.goBack();
                else {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                }

            }
        });


        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (JackCityConst.getCode() == requestCode)
            if (JackCityConst.getCallBack() == null) return;
        if (resultCode != -1) {
            JackCityConst.getCallBack().onReceiveValue(null);
            return;
        }
        Uri result = (data == null)? JackCityConst.getURL() : data.getData();
        if(result != null && JackCityConst.getCallBack() != null)
            JackCityConst.getCallBack().onReceiveValue(new Uri[]{result});
        else if(JackCityConst.getCallBack() != null)
            JackCityConst.getCallBack().onReceiveValue(new Uri[] {JackCityConst.getURL()});
        JackCityConst.setCallBack(null);
        super.onActivityResult(requestCode, resultCode, data);
    }


}