package com.davydh.covid_19.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.davydh.covid_19.model.Province;
import com.davydh.covid_19.model.Resource;
import com.davydh.covid_19.repository.CovidRepository;

import java.util.List;

public class ProvinceViewModel extends ViewModel {
    private static final String TAG = ProvinceViewModel.class.getSimpleName();

    private MutableLiveData<Resource<List<Province>>> lastProvinceData;

    public LiveData<Resource<List<Province>>> getLastProvinceData() {
        if (lastProvinceData == null) {
            lastProvinceData = new MutableLiveData<>();
            Log.d(TAG, "getLastProvinceData: Download the nation data from Internet");
            CovidRepository.getInstance().getLastProvinceData(lastProvinceData);
        }

        return lastProvinceData;
    }
}
