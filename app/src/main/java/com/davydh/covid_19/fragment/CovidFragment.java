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

import com.davydh.covid_19.R;
import com.davydh.covid_19.adapter.HashMapAdapter;
import com.davydh.covid_19.databinding.FragmentCovidLayoutBinding;
import com.davydh.covid_19.model.Nation;
import com.davydh.covid_19.utils.DateTimeUtil;
import com.davydh.covid_19.viewmodel.NationViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CovidFragment extends Fragment {
    private static final String TAG = CovidFragment.class.getSimpleName();

    private NationViewModel nationViewModel;
    private FragmentCovidLayoutBinding binding;

    public CovidFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCovidLayoutBinding.inflate(inflater, container, false);
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
        binding.infectedText.setText(String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(lastNationData.getAttualmentePositivi()))));
        binding.recoveredText.setText(String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(lastNationData.getDimessi()))));
        binding.deadText.setText(String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(lastNationData.getDeceduti()))));
        binding.dataText.setText(getString(R.string.last_update,
                DateTimeUtil.getDataDate(lastNationData.getData())));
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

        final String totaleCasiStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(totaleCasi)));

        final String varTotaleCasiStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(varTotaleCasi)));

        if (varTotaleCasi > 0) {
            nationInfo.put("Totale casi", totaleCasiStr + " (+" + varTotaleCasiStr + ')');
        } else {
            nationInfo.put("Totale casi", totaleCasiStr + " (" + varTotaleCasiStr + ')');
        }

        final String totaleNuoviCasiPositiviStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(totaleNuoviCasiPositivi)));
        final String vatTotaleNuoviCasiPositiviStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(varTotaleNuoviCasiPositivi)));

        if (varTotaleNuoviCasiPositivi > 0) {
            nationInfo.put("Totale nuovi casi positivi", totaleNuoviCasiPositiviStr + " (+" + vatTotaleNuoviCasiPositiviStr + ')');
        } else {
            nationInfo.put("Totale nuovi casi positivi", totaleNuoviCasiPositiviStr + " (" + vatTotaleNuoviCasiPositiviStr + ')');
        }

        final String nuoviCasiPositiviStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(nuoviCasiPositivi)));
        final String varnUoviCasiPositiviStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(varNuoviCasiPositivi)));

        if (varNuoviCasiPositivi > 0) {
            nationInfo.put("Nuovi casi positivi", nuoviCasiPositiviStr + " (+" + varnUoviCasiPositiviStr + ')');
        } else {
            nationInfo.put("Nuovi casi positivi", nuoviCasiPositiviStr + " (" + varnUoviCasiPositiviStr + ')');
        }

        final String variazioneDimessiStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(variazioneDimessi)));
        final String varDimessiStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(varDimessi)));

        if (varDimessi > 0) {
            nationInfo.put("Nuovi guariti", variazioneDimessiStr + " (+" + varDimessiStr + ')');
        } else {
            nationInfo.put("Nuovi guariti", variazioneDimessiStr + " (" + varDimessiStr + ')');
        }

        final String variazioneDecedutiStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(variazioneDeceduti)));
        final String varDecedutiStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(varDeceduti)));

        if (varDeceduti > 0) {
            nationInfo.put("Nuovi deceduti", variazioneDecedutiStr + " (+" + varDecedutiStr + ')');
        } else {
            nationInfo.put("Nuovi deceduti", variazioneDecedutiStr + " (" + varDecedutiStr + ')');
        }

        final String ospedalizzatiStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(ospedalizzati)));
        final String varOspedalizzatiStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(varOspedalizzati)));

        if (varOspedalizzati > 0) {
            nationInfo.put("Totale ospedalizzati", ospedalizzatiStr + " (+" + varOspedalizzatiStr + ')');
        } else {
            nationInfo.put("Totale ospedalizzati", ospedalizzatiStr + " (" + varOspedalizzatiStr + ')');
        }

        final String terapiaIntensivaStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(terapiaIntensiva)));
        final String varTerapiaIntensivaStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(varTerapiaIntensiva)));

        if (varTerapiaIntensiva > 0) {
            nationInfo.put("Terapia intensiva", terapiaIntensivaStr + " (+" + varTerapiaIntensivaStr + ')');
        } else {
            nationInfo.put("Terapia intensiva", terapiaIntensivaStr + " (" + varTerapiaIntensivaStr + ')');
        }

        final String ricoveratiStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(ricoverati)));
        final String varRicoveratiStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(varRicoverati)));

        if (varRicoverati > 0) {
            nationInfo.put("Ricoverati", ricoveratiStr + " (+" + varRicoveratiStr + ')');
        } else {
            nationInfo.put("Ricoverati", ricoveratiStr + " (" + varRicoveratiStr + ')');
        }

        final String isolamentoDomiciliareStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(isolamentoDomiciliare)));
        final String varIsolamentoDomStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(varIsolamentoDom)));

        if (varIsolamentoDom > 0) {
            nationInfo.put("Isolamento", isolamentoDomiciliareStr + " (+" + varIsolamentoDomStr + ')');
        } else {
            nationInfo.put("Isolamento", isolamentoDomiciliareStr + " (" + varIsolamentoDomStr + ')');
        }

        final String tamponiStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(tamponi)));
        final String varTamponiStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(varTamponi)));

        if (varTamponi > 0) {
            nationInfo.put("Tamponi", tamponiStr + " (+" + varTamponiStr + ')');
        } else {
            nationInfo.put("Tamponi", tamponiStr + " (" + varTamponiStr + ')');
        }

        final String ingressiTerapiaIntensivaStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(ingressiTerapiaIntensiva)));
        final String varIngressiTerapiaIntensivaStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(varIngressiTerapiaIntensiva)));

        if (varIngressiTerapiaIntensiva > 0) {
            nationInfo.put("Ingressi terapia intensiva",
                    ingressiTerapiaIntensivaStr + " (+" + varIngressiTerapiaIntensivaStr + ')');
        } else {
            nationInfo.put("Ingressi terapia intensiva",
                    ingressiTerapiaIntensivaStr + " (" + varIngressiTerapiaIntensivaStr + ')');
        }

        final String casiTestatiStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(casiTestati)));
        final String varCasiTestatiStr = String.format(Locale.ITALIAN, "%,d",
                Long.parseLong(String.valueOf(varCasiTestati)));

        if (varCasiTestati > 0) {
            nationInfo.put("Casi testati", casiTestatiStr + " (+" + varCasiTestatiStr + ')');
        } else {
            nationInfo.put("Casi testati", casiTestatiStr + " (" + varCasiTestatiStr + ')');
        }

        HashMapAdapter hashMapAdapter = new HashMapAdapter(nationInfo, requireContext());
        binding.nationInfoList.setAdapter(hashMapAdapter);
    }
}
