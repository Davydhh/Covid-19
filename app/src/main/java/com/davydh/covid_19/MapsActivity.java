package com.davydh.covid_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    }
}
