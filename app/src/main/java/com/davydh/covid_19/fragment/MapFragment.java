package com.davydh.covid_19.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.davydh.covid_19.model.Province;
import com.davydh.covid_19.model.Region;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private static final String TAG = MapFragment.class.getSimpleName();
    private MapView mapView;
    private GoogleMap mMap;

    private List<Region> regionsData;
    private List<Region> lastRegionData;

    private List<Province> provincesData;
    private List<Province> lastProvincesData;
    private Map<String, Integer> provinceInfo = new HashMap<>();

    private ListView provincesListView;

    private RequestQueue queue;

    private Context context;

    public MapFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.fragment_map,container,false);

        mapView = rootview.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return rootview;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnInfoWindowClickListener(this);

        SharedPreferences preferences = getActivity().getSharedPreferences(MainActivity.PREFS,Context.MODE_PRIVATE);
        Boolean firstStart = preferences.getBoolean(MainActivity.MAP_KEY,true);

        if (firstStart == true) {
            getRegionDataFromServer();
            getProvinceDataFromServer();
            preferences.edit().putBoolean(MainActivity.MAP_KEY,false);
        }

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            context, R.raw.style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.9109,12.4818), 5.5f));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        provincesListView = getActivity().findViewById(R.id.province_list_view);
        context = getActivity().getApplicationContext();
        queue = Volley.newRequestQueue(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void getRegionDataFromServer() {
        Log.i("PROVA", "Download dati regione");
        String regionUrl = "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-regioni.json";

        regionsData = new ArrayList<>();
        lastRegionData = new ArrayList<>();

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, regionUrl, null, response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            String data = object.getString("data");
                            String stato = object.getString("stato");
                            int codiceRegionale = object.getInt("codice_regione");
                            String nome = object.getString("denominazione_regione");
                            double latitude = object.getDouble("lat");
                            double longitude = object.getDouble("long");
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

                            Region region = new Region(data,stato,codiceRegionale,nome,latitude,
                                    longitude,ricoveratiConSintomi,terapiaIntensiva,totaleOspedalizzati,
                                    isolamentoDomiciliare,attualmentePositivi,nuoviPositivi,dimessi,
                                    deceduti,totaleCasi,tamponi);

                            regionsData.add(region);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    lastRegionData = regionsData.subList(regionsData.size()-20,regionsData.size());

                    createMarkers();

                }, error -> {
                    Toast toast = Toast.makeText(context,"Impossibile scaricare i dati", Toast.LENGTH_SHORT);
                    toast.show();
                });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    private void getProvinceDataFromServer() {
        Log.i("PROVA", "Download dati province");
        final String provinceUrl = "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-province.json";

        provincesData = new ArrayList<>();
        lastProvincesData = new ArrayList<>();

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, provinceUrl, null, response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            String data = object.getString("data");
                            String stato = object.getString("stato");
                            int codiceRegionale = object.getInt("codice_regione");
                            String nomeRegione = object.getString("denominazione_regione");
                            int codiceProvincia = object.getInt("codice_provincia");
                            String nomeProvincia = object.getString("denominazione_provincia");
                            String siglaProvincia = object.getString("sigla_provincia");
                            double latitude = object.getDouble("lat");
                            double longitude = object.getDouble("long");
                            int totaleCasi = object.getInt("totale_casi");

                            if (!nomeProvincia.equals("In fase di definizione/aggiornamento")) {
                                Province province = new Province(data, stato, codiceRegionale, nomeRegione,
                                        codiceProvincia, nomeProvincia, siglaProvincia, latitude, longitude,
                                        totaleCasi);

                                provincesData.add(province);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    lastProvincesData = provincesData.subList(provincesData.size()-107, provincesData.size());
                }, error -> {
                    Toast toast = Toast.makeText(context,"Impossibile scaricare i dati", Toast.LENGTH_SHORT);
                    toast.show();
                });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    private void createMarkers() {
        for (int i = 0; i < lastRegionData.size(); i++) {
            Region region = lastRegionData.get(i);
            LatLng position = new LatLng(region.getLatitude(), region.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(region.getNome()).snippet("Contagiati: " + region.getAttualmentePositivi()));
            marker.setTag(region.getNome());

            int recovered = region.getAttualmentePositivi();

            if (recovered > 8000) {
                marker.setIcon(bitmapDescriptorFromVector(context, R.drawable.circle_red));
            } else if (recovered > 4000) {
                marker.setIcon(bitmapDescriptorFromVector(context, R.drawable.circle_orange));
            } else {
                marker.setIcon(bitmapDescriptorFromVector(context, R.drawable.circle_yellow));
            }

        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        MainActivity.setBottomSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        fillProvinceInfo((String) marker.getTag());
    }

    private void fillProvinceInfo(String regionName) {
        provinceInfo.clear();
        for (int i = 0; i < lastProvincesData.size(); i++) {
            Province province = lastProvincesData.get(i);
            if (province.getNomeRegione().equalsIgnoreCase(regionName)) {
                provinceInfo.put(province.getNomeProvincia(), province.getTotaleCasi());
            }
        }

        LinkedHashMap<String,Integer> sortedMap = new LinkedHashMap<>();
        provinceInfo.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        HashMapAdapter hashMapAdapter = new HashMapAdapter(sortedMap);
        provincesListView.setAdapter(hashMapAdapter);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



}
