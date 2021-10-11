package com.jackcity.citypot.app.jcsplash;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackcity.citypot.app.JackCityConst;
import com.jackcity.citypot.app.R;
import com.jackcity.citypot.app.databinding.JackCitySplashFragmentBinding;

public class JackCitySplash extends Fragment {

    private JackCitySplashViewModel mViewModel;
    private JackCitySplashFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(JackCitySplashViewModel.class);
        binding = JackCitySplashFragmentBinding.inflate(inflater, container, false);
        JackCityConst jackCityConst = new JackCityConst();
        jackCityConst.setActivity(getActivity());
        jackCityConst.setBase();
        mViewModel.setActivity(getActivity());
        mViewModel.setLazyLoader(binding.jcpLoader);

        mViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<AsyncTask<Void, Void, Void>>() {
            @Override
            public void onChanged(AsyncTask<Void, Void, Void> voidVoidVoidAsyncTask) {
                voidVoidVoidAsyncTask.execute();
            }
        });

        return binding.getRoot();
    }


}