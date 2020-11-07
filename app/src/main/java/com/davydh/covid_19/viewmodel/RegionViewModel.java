package com.davydh.covid_19.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.davydh.covid_19.model.Region;
import com.davydh.covid_19.model.Resource;
import com.davydh.covid_19.repository.CovidRepository;

import java.util.List;

public class RegionViewModel extends ViewModel {
    private static final String TAG = RegionViewModel.class.getSimpleName();

    private MutableLiveData<Resource<List<Region>>> lastRegionData;

    public LiveData<Resource<List<Region>>> getLastRegionData() {
        if (lastRegionData == null) {
            lastRegionData = new MutableLiveData<>();
            Log.d(TAG, "getLastRegionData: Download the nation data from Internet");
            CovidRepository.getInstance().getLastRegionData(lastRegionData);
        }

        return lastRegionData;
    }
}
