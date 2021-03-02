package com.davydh.covid_19.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.davydh.covid_19.R;
import com.davydh.covid_19.activity.MainActivity;
import com.davydh.covid_19.adapter.IntHashMapAdapter;
import com.davydh.covid_19.model.Province;
import com.davydh.covid_19.model.Region;
import com.davydh.covid_19.viewmodel.ProvinceViewModel;
import com.davydh.covid_19.viewmodel.RegionViewModel;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private static final String TAG = MapFragment.class.getSimpleName();
    private MapView mapView;
    private GoogleMap mMap;
    private ListView provincesListView;
    private List<Province> provincesData;

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

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            requireContext(), R.raw.style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.9109,12.4818), 5f));
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        RegionViewModel regionViewModel = new ViewModelProvider(requireActivity()).get(RegionViewModel.class);
        regionViewModel.getLastCovidRegionData().observe(getViewLifecycleOwner(), listResource -> {
            List<Region> lastRegionData = listResource.getData();
            if (lastRegionData != null && !lastRegionData.isEmpty()) {
                createMarkers(lastRegionData);
            } else {
                Log.d(TAG,
                        "onChanged: Errore scaricamento dati regione --> Code: " + listResource.getStatusCode() + " Message: " + listResource.getStatusMessage());
                Snackbar.make(requireView(), "Impossibile scaricare i dati relativi alle " +
                        "regioni", BaseTransientBottomBar.LENGTH_LONG);
            }
        });

        mMap.setOnInfoWindowClickListener(this);
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

    private void createMarkers(List<Region> regionsData) {
        for (int i = 0; i < regionsData.size(); i++) {
            Region region = regionsData.get(i);
            LatLng position = new LatLng(region.getLatitude(), region.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(position).title(region.getNome()));
            if (region.getTotaleNuoviPositivi() > 0) {
                marker.setSnippet("Contagiati: " + region.getAttualmentePositivi()
                        + " (+" + region.getTotaleNuoviPositivi() + ')');
            } else {
                marker.setSnippet("Contagiati: " + region.getAttualmentePositivi()
                        + " (" + region.getTotaleNuoviPositivi() + ')');
            }
            marker.setTag(region.getNome());

//            int recovered = region.getAttualmentePositivi();
//
//            if (recovered > 10000) {
//                marker.setIcon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_coronavirus_red));
//            } else if (recovered > 5000) {
//                marker.setIcon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_coronavirus_orange));
//            } else {
//                marker.setIcon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_coronavirus_yellow));
//            }

            int codiceRegione = region.getCodice();

            switch (codiceRegione) {
                case 3:
                case 1:
                case 13:
                case 15:
                case 8:
                case 11:
                case 9:
                case 21:
                case 22:
                case 10:
                    marker.setIcon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_coronavirus_orange));
                    break;
                case 17:
                case 14:
                    marker.setIcon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_coronavirus_red));
                    break;
                case 18:
                case 6:
                case 12:
                case 7:
                case 16:
                case 19:
                case 2:
                case 5:
                    marker.setIcon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_coronavirus_yellow));
                    break;
                default:
                    marker.setIcon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_coronavirus_white));
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        RegionDetailFragment fragment = new RegionDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", (String) marker.getTag());
        fragment.setArguments(bundle);
        ((MainActivity) requireActivity()).replaceFragment(fragment, true);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        Objects.requireNonNull(vectorDrawable).setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}