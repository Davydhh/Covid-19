package com.davydh.covid_19.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.davydh.covid_19.adapter.HashMapAdapter;
import com.davydh.covid_19.databinding.FragmentDashboardLayoutBinding;
import com.davydh.covid_19.model.Nation;
import com.davydh.covid_19.utils.DateTimeUtil;
import com.davydh.covid_19.viewmodel.NationViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardFragment extends Fragment {
    private static final String TAG = DashboardFragment.class.getSimpleName();

    private NationViewModel nationViewModel;
    private FragmentDashboardLayoutBinding binding;

    public DashboardFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nationViewModel = new ViewModelProvider(requireActivity()).get(NationViewModel.class);

        nationViewModel.getNationData().observe(getViewLifecycleOwner(), listResource -> {
            List<Nation> nationData = listResource.getData();
            if (nationData != null) {
                Nation lastNationData = nationData.get(nationData.size() - 1);
                setText(lastNationData);
                fillNationInfo(lastNationData, nationData);
            } else {
                Snackbar.make(requireView(), "Impossibile scaricare i dati relativi alle nazioni"
                        , BaseTransientBottomBar.LENGTH_LONG).show();
                Log.d(TAG, "onViewCreated: Errore --> Code: " + listResource.getStatusCode() + " " +
                        "Message: " + listResource.getStatusMessage());
            }
        });
    }

    private void setText(Nation lastNationData) {
        binding.infectedText.setText(String.format(Locale.ITALIAN, "%d",
                lastNationData.getAttualmentePositivi()));
        binding.recoveredText.setText(String.format(Locale.ITALIAN, "%d", lastNationData.getDimessi()));
        binding.deadText.setText(String.format(Locale.ITALIAN, "%d", lastNationData.getDeceduti()));
        binding.dataText.setText("Dati aggiornati al: " + DateTimeUtil.getDataDate(lastNationData.getData()));
    }

    private void fillNationInfo(Nation lastNationData, List<Nation> nationsData) {
        Map<String, String> nationInfo = new LinkedHashMap<>();

        int totaleCasi = lastNationData.getTotaleCasi();
        int dimessi = lastNationData.getDimessi();
        int deceduti = lastNationData.getDeceduti();
        int nuoviCasiPositivi = lastNationData.getNuoviPositivi();
        int ospedalizzati = lastNationData.getTotaleOspedalizzati();
        int terapiaIntensiva = lastNationData.getTerapiaIntensiva();
        int ricoverati = lastNationData.getRicoveratiConSintomi();
        int isolamentoDomiciliare = lastNationData.getIsolamentoDomiciliare();
        int tamponi = lastNationData.getTamponi();
        int totaleNuoviCasiPositivi = lastNationData.getTotaleNuoviPositivi();
        int ingressiTerapiaIntensiva = lastNationData.getIngressiTerapiaIntensiva();
        int casiTestati = lastNationData.getCasiTestati();

        Nation previousNationData = nationsData.get(nationsData.size() - 2);
        int previousDeaths = previousNationData.getDeceduti();
        int previousRecovered = previousNationData.getDimessi();

        int variazioneDeceduti = deceduti - previousDeaths;
        int variazioneDimessi = dimessi - previousRecovered;

        Nation oldNationData = nationsData.get(nationsData.size() - 3);

        int varTotaleCasi = totaleCasi - previousNationData.getTotaleCasi();
        int varTotaleNuoviCasiPositivi = totaleNuoviCasiPositivi - previousNationData.getTotaleNuoviPositivi();
        int varNuoviCasiPositivi = nuoviCasiPositivi - previousNationData.getNuoviPositivi();
        int varDimessi = variazioneDimessi - (previousRecovered - oldNationData.getDimessi());
        int varDeceduti = variazioneDeceduti - (previousDeaths - oldNationData.getDeceduti());
        int varOspedalizzati = ospedalizzati - previousNationData.getTotaleOspedalizzati();
        int varTerapiaIntensiva = terapiaIntensiva - previousNationData.getTerapiaIntensiva();
        int varRicoverati = ricoverati - previousNationData.getRicoveratiConSintomi();
        int varIsolamentoDom = isolamentoDomiciliare - previousNationData.getIsolamentoDomiciliare();
        int varTamponi = tamponi - previousNationData.getTamponi();
        int varIngressiTerapiaIntensiva =
                ingressiTerapiaIntensiva - previousNationData.getIngressiTerapiaIntensiva();
        int varCasiTestati = casiTestati - previousNationData.getCasiTestati();

        if (varTotaleCasi > 0) {
            nationInfo.put("Totale casi",totaleCasi + " (+" + varTotaleCasi + ')');
        } else {
            nationInfo.put("Totale casi",totaleCasi + " (" + varTotaleCasi + ')');
        }

        if (varTotaleNuoviCasiPositivi > 0) {
            nationInfo.put("Totale nuovi casi positivi", totaleNuoviCasiPositivi + " (+" + varTotaleNuoviCasiPositivi + ')');
        } else {
            nationInfo.put("Totale nuovi casi positivi", totaleNuoviCasiPositivi + " (" + varTotaleNuoviCasiPositivi + ')');
        }

        if (varNuoviCasiPositivi > 0) {
            nationInfo.put("Nuovi casi positivi", nuoviCasiPositivi + " (+" + varNuoviCasiPositivi + ')');
        } else {
            nationInfo.put("Nuovi casi positivi", nuoviCasiPositivi + " (" + varNuoviCasiPositivi + ')');
        }

        if (varDimessi > 0) {
            nationInfo.put("Nuovi guariti", variazioneDimessi + " (+" + varDimessi + ')');
        } else {
            nationInfo.put("Nuovi guariti", variazioneDimessi + " (" + varDimessi + ')');
        }

        if (varDeceduti > 0) {
            nationInfo.put("Nuovi deceduti", variazioneDeceduti + " (+" + varDeceduti + ')');
        } else {
            nationInfo.put("Nuovi deceduti", variazioneDeceduti + " (" + varDeceduti + ')');
        }

        if (varOspedalizzati > 0) {
            nationInfo.put("Totale ospedalizzati", ospedalizzati + " (+" + varOspedalizzati + ')');
        } else {
            nationInfo.put("Totale ospedalizzati", ospedalizzati + " (" + varOspedalizzati + ')');
        }

        if (varTerapiaIntensiva > 0) {
            nationInfo.put("Terapia intensiva", terapiaIntensiva + " (+" + varTerapiaIntensiva + ')');
        } else {
            nationInfo.put("Terapia intensiva", terapiaIntensiva + " (" + varTerapiaIntensiva + ')');
        }

        if (varRicoverati > 0) {
            nationInfo.put("Ricoverati", ricoverati + " (+" + varRicoverati + ')');
        } else {
            nationInfo.put("Ricoverati", ricoverati + " (" + varRicoverati + ')');
        }

        if (varIsolamentoDom > 0) {
            nationInfo.put("Isolamento", isolamentoDomiciliare + " (+" + varIsolamentoDom + ')');
        } else {
            nationInfo.put("Isolamento", isolamentoDomiciliare + " (" + varIsolamentoDom + ')');
        }

        if (varTamponi > 0) {
            nationInfo.put("Tamponi", tamponi + " (+" + varTamponi + ')');
        } else {
            nationInfo.put("Tamponi", tamponi + " (" + varTamponi + ')');
        }

        if (varIngressiTerapiaIntensiva > 0) {
            nationInfo.put("Ingressi terapia intensiva",
                    ingressiTerapiaIntensiva + " (+" + varIngressiTerapiaIntensiva + ')');
        } else {
            nationInfo.put("Ingressi terapia intensiva",
                    ingressiTerapiaIntensiva + " (" + varIngressiTerapiaIntensiva + ')');
        }

        if (varCasiTestati > 0) {
            nationInfo.put("Casi testati", casiTestati + " (+" + varCasiTestati + ')');
        } else {
            nationInfo.put("Casi testati", casiTestati + " (" + varCasiTestati + ')');
        }

        HashMapAdapter hashMapAdapter = new HashMapAdapter(nationInfo, requireContext());
        binding.nationInfoList.setAdapter(hashMapAdapter);
    }
}
