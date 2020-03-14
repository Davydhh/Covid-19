package com.davydh.covid_19.fragment;

import android.content.Intent;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.davydh.covid_19.R;
import com.davydh.covid_19.activity.MainActivity;
import com.davydh.covid_19.adapter.HashMapAdapter;
import com.davydh.covid_19.model.Nation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private TextView infectedText;
    private TextView recoveredText;
    private TextView deadText;
    private ListView nationListView;
    private List<Nation> nationsData = new ArrayList<>();
    private Nation lastNationData;
    private Map<String, Integer> nationInfo = new HashMap<>();
    HashMapAdapter hashMapAdapter;

    public DashboardFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        infectedText = getActivity().findViewById(R.id.infected_text);
        recoveredText = getActivity().findViewById(R.id.recovered_text);
        deadText = getActivity().findViewById(R.id.dead_text);
        nationListView = getActivity().findViewById(R.id.nation_info_list);

        getNationDataFromServer();
    }

    private void getNationDataFromServer() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getContext());
        String nationUrl = "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-andamento-nazionale.json";

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, nationUrl, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
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

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(MainActivity.getContext(),"Impossibile scaricare i dati", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    private void setText() {
        infectedText.setText(lastNationData.getAttualmentePositivi()+"");
        recoveredText.setText(lastNationData.getDimessi()+"");
        deadText.setText(lastNationData.getDeceduti()+"");
    }

    private void fillNationInfo() {
        nationInfo.put("Totale casi:",lastNationData.getTotaleCasi());
        nationInfo.put("Nuovi casi positivi:",lastNationData.getNuoviPositivi());
        nationInfo.put("Totale ospedalizzati:",lastNationData.getTotaleOspedalizzati());
        nationInfo.put("Terapia intensiva:",lastNationData.getTerapiaIntensiva());
        nationInfo.put("Ricoverati", lastNationData.getRicoveratiConSintomi());
        nationInfo.put("Isolamento domiciliare",lastNationData.getIsolamentoDomiciliare());
        nationInfo.put("Tamponi", lastNationData.getTamponi());

        hashMapAdapter = new HashMapAdapter(nationInfo);
        nationListView.setAdapter(hashMapAdapter);
    }
}
