package com.davydh.covid_19.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private static List<Nation> nationsData = new ArrayList<>();
    private static Nation lastNationData;
    private static Context context;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        context = getApplicationContext();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new DashboardFragment());
        fragmentTransaction.commit();

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

    public static Context getContext() {
        return context;
    }
}
