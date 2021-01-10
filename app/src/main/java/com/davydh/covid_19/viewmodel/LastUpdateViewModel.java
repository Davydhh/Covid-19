package com.davydh.covid_19.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.davydh.covid_19.model.Resource;
import com.davydh.covid_19.repository.VaccinesRepository;

public class LastUpdateViewModel extends ViewModel {
    private static final String TAG = LastUpdateViewModel.class.getSimpleName();

    private MutableLiveData<Resource<String>> lastUpdate;

    public LiveData<Resource<String>> getLastUpdate() {
        if (lastUpdate == null) {
            lastUpdate = new MutableLiveData<>();
            Log.d(TAG, "getLastUpdate: Download the last update vaccine data from Internet");
            VaccinesRepository.getInstance().getLastUpdate(lastUpdate);
        }

        return lastUpdate;
    }
}
