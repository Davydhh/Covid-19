package com.davydh.covid_19.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.davydh.covid_19.model.Region;
import com.davydh.covid_19.model.Resource;
import com.davydh.covid_19.repository.CovidRepository;
import com.davydh.covid_19.repository.VaccinesRepository;

import java.util.List;

public class RegionViewModel extends ViewModel {
    private static final String TAG = RegionViewModel.class.getSimpleName();

    private MutableLiveData<Resource<List<Region>>> lastCovidRegionData;
    private MutableLiveData<Resource<List<Region>>> lastVaccinesRegionData;

    public LiveData<Resource<List<Region>>> getLastCovidRegionData() {
        if (lastCovidRegionData == null) {
            lastCovidRegionData = new MutableLiveData<>();
            Log.d(TAG, "getLastCovidRegionData: Download the nation data from Internet");
            CovidRepository.getInstance().getLastRegionData(lastCovidRegionData);
        }

        return lastCovidRegionData;
    }

    public LiveData<Resource<List<Region>>> getLastVaccinesRegionData() {
        if (lastVaccinesRegionData == null) {
            lastVaccinesRegionData = new MutableLiveData<>();
            Log.d(TAG, "getLastVaccinesRegionData: Download the nation data from Internet");
            VaccinesRepository.getInstance().getRegionData(lastVaccinesRegionData);
        }

        return lastVaccinesRegionData;
    }
}
