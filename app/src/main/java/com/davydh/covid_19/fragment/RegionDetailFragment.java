package com.davydh.covid_19.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.davydh.covid_19.R;
import com.davydh.covid_19.activity.MainActivity;
import com.davydh.covid_19.adapter.HashMapAdapter;
import com.davydh.covid_19.adapter.IntHashMapAdapter;
import com.davydh.covid_19.databinding.FragmentRegionDetailBinding;
import com.davydh.covid_19.model.Province;
import com.davydh.covid_19.model.Region;
import com.davydh.covid_19.viewmodel.ProvinceViewModel;
import com.davydh.covid_19.viewmodel.RegionViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegionDetailFragment extends Fragment {
    private static final String TAG = RegionDetailFragment.class.getSimpleName();

    private FragmentRegionDetailBinding binding;

    private ListView provincesListView;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegionDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setProvinceListView();

        binding.progressBarContainer.setVisibility(View.VISIBLE);

        Bundle bundle = getArguments();
        String value;
        if (bundle != null) {
            value = bundle.getString("name");
            binding.regionTextView.setText(value);
            assert value != null;
            String imageName = value;
            if (value.contains(" ")) {
                imageName = imageName.replace(" ", "_");
            }
            if (value.contains(".")) {
                imageName = imageName.replace(".", "_");
            }
            if (value.contains("'")) {
                imageName = imageName.replace("'", "_");
            }
            binding.stemmaRegione.setImageResource(getResources().getIdentifier("com" +
                    ".davydh.covid_19:drawable" +
                    "/stemma_" + imageName.toLowerCase(), null, null));

            RegionViewModel regionViewModel = new ViewModelProvider(requireActivity()).get(RegionViewModel.class);

            regionViewModel.getCovidRegionData(value).observe(getViewLifecycleOwner(), listResource -> {
                List<Region> regions = listResource.getData();

                if (regions != null && !regions.isEmpty()) {
                    Region lastRegion = regions.get(regions.size() - 1);

                    String note = lastRegion.getNote();
                    if (note != null) {
                        binding.regionNote.setText(note);
                        binding.regionNote.setVisibility(View.VISIBLE);
                    }

                    fillRegionInfo(lastRegion, regions);
                } else {
                    Snackbar.make(requireView(), "Impossibile scaricare i dati relativi alla " +
                                    "regione"
                            , BaseTransientBottomBar.LENGTH_LONG).show();
                    Log.d(TAG, "onViewCreated: Errore --> Code: " + listResource.getStatusCode() + " " +
                            "Message: " + listResource.getStatusMessage());
                }

                binding.progressBarContainer.setVisibility(View.INVISIBLE);
            });

            ProvinceViewModel provinceViewModel = new ViewModelProvider(requireActivity()).get(ProvinceViewModel.class);
            provinceViewModel.getLastProvinceData().observe(getViewLifecycleOwner(), listResource -> {
                List<Province> provinceData = listResource.getData();
                if (provinceData != null && !provinceData.isEmpty()) {
                    binding.showCitiesDataButton.setOnClickListener(v -> {
                        MainActivity.setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED);
                        fillProvinceInfo(provinceData, value);
                    });
                    binding.showCitiesDataButton.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG,
                            "fillProvinceInfo: Errore --> Code: " + listResource.getStatusCode() + " " +
                                    "Message: " + listResource.getStatusMessage());
                    Snackbar.make(requireView(), "Impossibile scaricare i dati relativi alle regioni"
                            , BaseTransientBottomBar.LENGTH_LONG);
                }
            });
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setProvinceListView() {
        provincesListView = requireActivity().findViewById(R.id.province_list_view);
        provincesListView.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
            } else if (action == MotionEvent.ACTION_UP) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }

            v.onTouchEvent(event);
            return true;
        });
    }

    private void fillRegionInfo(Region lastRegionData, List<Region> regionsData) {
        Map<String, String> nationInfo = new LinkedHashMap<>();

        int totaleCasi = lastRegionData.getTotaleCasi();
        int dimessi = lastRegionData.getDimessi();
        int deceduti = lastRegionData.getDeceduti();
        int nuoviCasiPositivi = lastRegionData.getNuoviPositivi();
        int ospedalizzati = lastRegionData.getTotaleOspedalizzati();
        int terapiaIntensiva = lastRegionData.getTerapiaIntensiva();
        int ricoverati = lastRegionData.getRicoveratiConSintomi();
        int isolamentoDomiciliare = lastRegionData.getIsolamentoDomiciliare();
        int tamponi = lastRegionData.getTamponi();
        int totaleNuoviCasiPositivi = lastRegionData.getTotaleNuoviPositivi();

        Region previousRegionData = regionsData.get(regionsData.size() - 2);
        int previousDeaths = previousRegionData.getDeceduti();
        int previousRecovered = previousRegionData.getDimessi();

        int variazioneDeceduti = deceduti - previousDeaths;
        int variazioneDimessi = dimessi - previousRecovered;

        Region oldRegionData = regionsData.get(regionsData.size() - 3);

        int varTotaleCasi = totaleCasi - previousRegionData.getTotaleCasi();
        int varTotaleNuoviCasiPositivi = totaleNuoviCasiPositivi - previousRegionData.getTotaleNuoviPositivi();
        int varNuoviCasiPositivi = nuoviCasiPositivi - previousRegionData.getNuoviPositivi();
        int varDimessi = variazioneDimessi - (previousRecovered - oldRegionData.getDimessi());
        int varDeceduti = variazioneDeceduti - (previousDeaths - oldRegionData.getDeceduti());
        int varOspedalizzati = ospedalizzati - previousRegionData.getTotaleOspedalizzati();
        int varTerapiaIntensiva = terapiaIntensiva - previousRegionData.getTerapiaIntensiva();
        int varRicoverati = ricoverati - previousRegionData.getRicoveratiConSintomi();
        int varIsolamentoDom = isolamentoDomiciliare - previousRegionData.getIsolamentoDomiciliare();
        int varTamponi = tamponi - previousRegionData.getTamponi();

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

        HashMapAdapter hashMapAdapter = new HashMapAdapter(nationInfo, requireContext());
        binding.regionInfoList.setAdapter(hashMapAdapter);
    }

    private void fillProvinceInfo(List<Province> provincesData, String regionName) {
        Map<String, Integer> provinceInfo = new HashMap<>();
        for (int i = 0; i < provincesData.size(); i++) {
            Province province = provincesData.get(i);
            if (province.getNomeRegione().equalsIgnoreCase(regionName)) {
                provinceInfo.put(province.getNomeProvincia(), province.getTotaleCasi());
            }
        }

        if (android.os.Build.VERSION.SDK_INT <= 23){
            Map<String,Integer> sortedMap = sortByComparator(provinceInfo,false);
            IntHashMapAdapter hashMapAdapter = new IntHashMapAdapter(sortedMap);
            provincesListView.setAdapter(hashMapAdapter);
        } else{
            LinkedHashMap<String,Integer> sortedMap = new LinkedHashMap<>();
            provinceInfo.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
            IntHashMapAdapter hashMapAdapter = new IntHashMapAdapter(sortedMap);
            provincesListView.setAdapter(hashMapAdapter);
        }
    }

    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order) {

        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        Collections.sort(list, (o1, o2) -> {
            if (order) {
                return o1.getValue().compareTo(o2.getValue());
            }
            else {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}