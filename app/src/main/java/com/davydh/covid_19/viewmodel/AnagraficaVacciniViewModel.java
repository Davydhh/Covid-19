package com.davydh.covid_19.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.davydh.covid_19.model.AnagraficaVaccini;
import com.davydh.covid_19.model.Resource;
import com.davydh.covid_19.repository.VaccinesRepository;

import java.util.List;

public class AnagraficaVacciniViewModel extends ViewModel {
    private static final String TAG = AnagraficaVacciniViewModel.class.getSimpleName();

    private MutableLiveData<Resource<List<AnagraficaVaccini>>> summaryLatest;

    public LiveData<Resource<List<AnagraficaVaccini>>> getSummaryLatest() {
        if (summaryLatest == null) {
            summaryLatest = new MutableLiveData<>();
            Log.d(TAG, "getSummaryLatest: Download the anagrafica vaccini data from Internet");
            VaccinesRepository.getInstance().getAnagraficaSummaryLatest(summaryLatest);
        }

        return summaryLatest;
    }
}
