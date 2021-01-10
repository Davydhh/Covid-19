package com.davydh.covid_19.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.davydh.covid_19.R;
import com.davydh.covid_19.fragment.DashboardFragment;
import com.davydh.covid_19.fragment.NewsFragment;
import com.davydh.covid_19.fragment.CovidFragment;
import com.davydh.covid_19.fragment.InfoBottomSheetDialog;
import com.davydh.covid_19.fragment.MapFragment;
import com.davydh.covid_19.fragment.StatsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


public class MainActivity extends AppCompatActivity {

    public static final String PREFS_KEY = "Prefs";
    public static final String DASH_KEY = "DashKey";
    public static final String MAP_KEY = "MapKey";
    public static final String DARK_MODE_KEY = "DarkMode";
    public static SharedPreferences preferences;

    private BottomNavigationView bottomNavigation;
    private static FragmentManager fragmentManager;
    private DashboardFragment dashboardFragment;
    private MapFragment mapFragment;
    private static FragmentTransaction ft;
    private boolean darkMode;

    private static BottomSheetBehavior sheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        preferences = getSharedPreferences(PREFS_KEY,0);
        preferences.edit().putBoolean(DASH_KEY,false).apply();
        preferences.edit().putBoolean(MAP_KEY,false).apply();

        darkMode = preferences.getBoolean(DARK_MODE_KEY, false);
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        LinearLayout bottomSheet = findViewById(R.id.province_bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        dashboardFragment = new DashboardFragment();
        mapFragment = new MapFragment();

        fragmentManager = getSupportFragmentManager();

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            ft = fragmentManager.beginTransaction();

            if (item.isChecked()) {
                return false;
            }

            switch (item.getItemId()) {
                case R.id.dashboard_item:
                    replaceFragment(dashboardFragment, false);
                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                    return true;
                case R.id.map_item:
                    replaceFragment(mapFragment, false);
                    return true;
                case R.id.stats_item:
                    ft.replace(R.id.fragment_container, new StatsFragment()).commit();
                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                    return true;
                case R.id.news_item:
                    replaceFragment(new NewsFragment(), false);
                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                    return true;
                default:
                    return false;
            }
        });
    }

    public static void replaceFragment(Fragment fragment, boolean backStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (backStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.info_item) {
            InfoBottomSheetDialog infoBottomSheetDialog = new InfoBottomSheetDialog();
            infoBottomSheetDialog.show(getSupportFragmentManager(),"InfoBottomSheetDialog");
        } else if (id == R.id.dark_mode_item) {
            if (darkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                preferences.edit().putBoolean(DARK_MODE_KEY, false).apply();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                preferences.edit().putBoolean(DARK_MODE_KEY, true).apply();
            }
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
