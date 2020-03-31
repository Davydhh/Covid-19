package com.davydh.covid_19.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.davydh.covid_19.R;
import com.davydh.covid_19.activity.MainActivity;
import com.davydh.covid_19.adapter.HashMapAdapter;
import com.davydh.covid_19.model.Nation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DashboardFragment extends Fragment {

    private TextView infectedText;
    private TextView recoveredText;
    private TextView deadText;
    private TextView dataText;
    private ListView nationListView;
    private static List<Nation> nationsData;
    private static List<Integer> totalNewPositiveData;
    private Nation lastNationData;
    private Context context;
    private SharedPreferences preferences;

    public DashboardFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_layout, container, false);
        infectedText = rootView.findViewById(R.id.infected_text);
        recoveredText = rootView.findViewById(R.id.recovered_text);
        deadText = rootView.findViewById(R.id.dead_text);
        dataText = rootView.findViewById(R.id.data_text);
        nationListView = rootView.findViewById(R.id.nation_info_list);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = Objects.requireNonNull(getActivity()).getApplicationContext();

        preferences = getActivity().getSharedPreferences(MainActivity.PREFS_KEY,0);

        if (!preferences.getBoolean(MainActivity.DASH_KEY, false)) {
            getNationDataFromServer();
        } else {
            setText();
            fillNationInfo();
            getTotalPositiveVariation();
        }
    }

    private void getNationDataFromServer() {
        Log.i("Download","Download Nation data");
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String nationUrl = "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-andamento-nazionale.json";

        nationsData = new ArrayList<>();

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, nationUrl, null, response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            String data = object.getString("data");
                            String stato = object.getString("stato");
                            int ricoveratiConSintomi = object.getInt("ricoverati_con_sintomi");
                            int terapiaIntensiva = object.getInt("terapia_intensiva");
                            int totaleOspedalizzati = object.getInt("totale_ospedalizzati");
                            int isolamentoDomiciliare = object.getInt("isolamento_domiciliare");
                            int attualmentePositivi = object.getInt("totale_attualmente_positivi");
                            int nuoviPositivi = object.getInt("nuovi_attualmente_positivi");
                            int dimessi = object.getInt("dimessi_guariti");
                            int deceduti = object.getInt("deceduti");
                            int totaleCasi = object.getInt("totale_casi");
                            int tamponi = object.getInt("tamponi");

                            Nation nation = new Nation(data,stato,ricoveratiConSintomi,terapiaIntensiva,
                                    totaleOspedalizzati,isolamentoDomiciliare,attualmentePositivi,
                                    nuoviPositivi,dimessi,deceduti,totaleCasi,tamponi);

                            nationsData.add(nation);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    lastNationData = nationsData.get(nationsData.size() - 1);

                    setText();

                    fillNationInfo();

                    getTotalPositiveVariation();

                    preferences.edit().putBoolean(MainActivity.DASH_KEY,true).apply();
                }, error -> {
                    Toast toast = Toast.makeText(context,"Impossibile scaricare i dati", Toast.LENGTH_SHORT);
                    toast.show();
                });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    private void setText() {
        infectedText.setText(Integer.toString(lastNationData.getAttualmentePositivi()));
        recoveredText.setText(Integer.toString(lastNationData.getDimessi()));
        deadText.setText(Integer.toString(lastNationData.getDeceduti()));
        String date = lastNationData.getData();
        String day = date.substring(8,10);
        String month = date.substring(5,7);
        String year = date.substring(0,4);
        dataText.setText("Dati aggiornati al: " + day + '/' + month + '/' + year);
    }

    private void fillNationInfo() {
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

        Nation previousNationData = nationsData.get(nationsData.size()-2);
        int previousDeaths = previousNationData.getDeceduti();
        int previousRecovered = previousNationData.getDimessi();

        int totaleNuoviCasiPositivi =  nuoviCasiPositivi + (dimessi - previousRecovered)
                + (deceduti - previousDeaths);
        int variazioneDeceduti = deceduti - previousDeaths;
        int variazioneDimessi = dimessi - previousRecovered;

        Nation oldNationData = nationsData.get(nationsData.size()-3);

        int varTotaleCasi = totaleCasi - previousNationData.getTotaleCasi();
        int varTotaleNuoviCasiPositivi = totaleNuoviCasiPositivi - (previousNationData.getNuoviPositivi()
                + (previousRecovered - oldNationData.getDimessi()
                + (previousDeaths - oldNationData.getDeceduti())));
        int varNuoviCasiPositivi = nuoviCasiPositivi - previousNationData.getNuoviPositivi();
        int varDimessi = variazioneDimessi - (previousRecovered - oldNationData.getDimessi());
        int varDeceduti = variazioneDeceduti - (previousDeaths - oldNationData.getDeceduti());
        int varOspedalizzati = ospedalizzati - previousNationData.getTotaleOspedalizzati();
        int varTerapiaIntensiva = terapiaIntensiva - previousNationData.getTerapiaIntensiva();
        int varRicoverati = ricoverati - previousNationData.getRicoveratiConSintomi();
        int varIsolamentoDom = isolamentoDomiciliare - previousNationData.getIsolamentoDomiciliare();
        int varTamponi = tamponi - previousNationData.getTamponi();

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
            nationInfo.put("Totale ospedalizzati", ospedalizzati + " (+" + varOspedalizzati + ')');
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
            nationInfo.put("Isolamento domiciliare", isolamentoDomiciliare + " (+" + varIsolamentoDom + ')');
        } else {
            nationInfo.put("Isolamento domiciliare", isolamentoDomiciliare + " (" + varIsolamentoDom + ')');
        }

        if (varTamponi > 0) {
            nationInfo.put("Tamponi", tamponi + " (+" + varTamponi + ')');
        } else {
            nationInfo.put("Tamponi", tamponi + " (" + varTamponi + ')');
        }

        HashMapAdapter hashMapAdapter = new HashMapAdapter(nationInfo);
        nationListView.setAdapter(hashMapAdapter);
    }

    private void getTotalPositiveVariation() {
        totalNewPositiveData = new ArrayList<>();
       for (int i = 0; i < nationsData.size(); i++) {
           Nation nation = nationsData.get(i);
           int date = nation.getNuoviPositivi();
           if (i != 0) {
               Nation previousNation = nationsData.get(i-1);
               date += (nation.getDimessi() - previousNation.getDimessi())
                       + (nation.getDeceduti() - previousNation.getDeceduti());
           }
           totalNewPositiveData.add(date);
       }
    }

    public static List<Nation> getNationsData() {
        return nationsData;
    }

    public static List<Integer> getTotalNewPositiveData() {
        return totalNewPositiveData;
    }
}
