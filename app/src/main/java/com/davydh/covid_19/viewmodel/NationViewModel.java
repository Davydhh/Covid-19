package com.davydh.covid_19.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.davydh.covid_19.model.Nation;
import com.davydh.covid_19.model.Resource;
import com.davydh.covid_19.repository.CovidRepository;

import java.util.List;

public class NationViewModel extends ViewModel {
    private static final String TAG = NationViewModel.class.getSimpleName();

    private MutableLiveData<Resource<List<Nation>>> nationData;

    public LiveData<Resource<List<Nation>>> getNationData() {
        if (nationData == null) {
            nationData = new MutableLiveData<>();
            Log.d(TAG, "getNationData: Download the nation data from Internet");
            CovidRepository.getInstance().getNationData(nationData);
        }

        return nationData;
    }
}
