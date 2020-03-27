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
        dataText.setText("Dati aggiornati al: " + lastNationData.getData());
    }

    private void fillNationInfo() {
        Map<String, Integer> nationInfo = new HashMap<>();
        nationInfo.put("Totale casi",lastNationData.getTotaleCasi());
        nationInfo.put("Nuovi casi positivi",lastNationData.getNuoviPositivi());
        nationInfo.put("Totale ospedalizzati",lastNationData.getTotaleOspedalizzati());
        nationInfo.put("Terapia intensiva",lastNationData.getTerapiaIntensiva());
        nationInfo.put("Ricoverati", lastNationData.getRicoveratiConSintomi());
        nationInfo.put("Isolamento domiciliare",lastNationData.getIsolamentoDomiciliare());
        nationInfo.put("Tamponi", lastNationData.getTamponi());

        HashMapAdapter hashMapAdapter = new HashMapAdapter(nationInfo);
        nationListView.setAdapter(hashMapAdapter);
    }

    public static List<Nation> getNationsData() {
        return nationsData;
    }
}
