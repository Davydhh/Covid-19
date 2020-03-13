package com.davydh.covid_19.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.davydh.covid_19.R;
import com.davydh.covid_19.fragment.DashboardFragment;
import com.davydh.covid_19.fragment.MapFragment;
import com.davydh.covid_19.model.Nation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private BottomNavigationView bottomNavigation;
    private static List<Nation> nationsData = new ArrayList<>();
    private static Nation lastNationData;
    private static Context context;
    private FragmentManager fragmentManager;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        context = getApplicationContext();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction ft = fragmentManager.beginTransaction();

                if (item.isChecked()) {
                    return false;
                }

                switch (item.getItemId()) {
                    case R.id.dashboard_item:
                        ft.replace(R.id.fragment_container, new DashboardFragment()).commit();
                        return true;
                    case R.id.map_item:
                        ft.replace(R.id.fragment_container, new MapFragment()).commit();
                        return true;
                    case R.id.stats_item:
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        GeoJsonLayer layer = null;
        try {
            layer = new GeoJsonLayer(mMap, R.raw.italy,
                    getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        layer.addLayerToMap();

        LatLng rome= new LatLng(41.9109,12.4818);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(rome));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 5.5f ) );

        getDataFromServer();
    }

    private void getDataFromServer() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String nationUrl = "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-andamento-nazionale.json";
        String regionsUrl = "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-regioni.json";
        String provencesUrl = "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-province.json";

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
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(),"Impossibile scaricare i dati", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    public static Context getContext() {
        return context;
    }
}
