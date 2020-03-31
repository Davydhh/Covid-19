package com.davydh.covid_19.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.davydh.covid_19.R;
import com.davydh.covid_19.fragment.DashboardFragment;
import com.davydh.covid_19.fragment.InfoBottomSheetDialog;
import com.davydh.covid_19.fragment.MapFragment;
import com.davydh.covid_19.fragment.StatsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


public class MainActivity extends AppCompatActivity {

    public static final String PREFS_KEY = "Prefs";
    public static final String DASH_KEY = "DashKey";
    public static final String MAP_KEY = "MapKey";
    public static SharedPreferences preferences;

    private BottomNavigationView bottomNavigation;
    private FragmentManager fragmentManager;
    private DashboardFragment dashboardFragment;
    private MapFragment mapFragment;

    private static BottomSheetBehavior sheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        preferences = getSharedPreferences(PREFS_KEY,0);
        preferences.edit().putBoolean(DASH_KEY,false).apply();
        preferences.edit().putBoolean(MAP_KEY,false).apply();

        LinearLayout bottom_sheet = findViewById(R.id.province_bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        dashboardFragment = new DashboardFragment();
        mapFragment = new MapFragment();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, dashboardFragment);
        fragmentTransaction.commit();

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            FragmentTransaction ft = fragmentManager.beginTransaction();

            if (item.isChecked()) {
                return false;
            }

            switch (item.getItemId()) {
                case R.id.dashboard_item:
                    ft.replace(R.id.fragment_container, dashboardFragment).commit();
                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                    return true;
                case R.id.map_item:
                    ft.replace(R.id.fragment_container, mapFragment).commit();
                    return true;
                case R.id.stats_item:
                    ft.replace(R.id.fragment_container, new StatsFragment()).commit();
                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                    return true;
                default:
                    return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_item) {
            InfoBottomSheetDialog infoBottomSheetDialog = new InfoBottomSheetDialog();
            infoBottomSheetDialog.show(getSupportFragmentManager(),"InfoBottomSheetDialog");
        }

        return super.onOptionsItemSelected(item);
    }

    public static void setBottomSheetState(int state) {
        sheetBehavior.setState(state);
    }

    @Override
    public void onBackPressed() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
    }
}
