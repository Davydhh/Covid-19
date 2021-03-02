package com.davydh.covid_19.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.davydh.covid_19.adapter.DashboardsViewPagerAdapter;
import com.davydh.covid_19.databinding.FragmentDashboardBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

public class DashboardFragment extends Fragment {
    
    private FragmentDashboardBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 mViewPager2 = binding.viewPagerDashboard;

        DashboardsViewPagerAdapter dashboardsViewPagerAdapter = new DashboardsViewPagerAdapter(requireActivity());
        dashboardsViewPagerAdapter.addFragment(new CovidFragment(), "Covid");
        dashboardsViewPagerAdapter.addFragment(new VaccinesFragment(), "Vaccini");

        mViewPager2.setAdapter(dashboardsViewPagerAdapter);

        new TabLayoutMediator(binding.tabsDashboards, mViewPager2,
                (tab, position) -> tab.setText(dashboardsViewPagerAdapter.getPageTitle(position))).attach();

    }
}