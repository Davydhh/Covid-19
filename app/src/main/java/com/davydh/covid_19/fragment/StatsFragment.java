package com.davydh.covid_19.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.davydh.covid_19.adapter.ChartsViewPagerAdapter;
import com.davydh.covid_19.databinding.FragmentStatsLayoutBinding;
import com.davydh.covid_19.model.Nation;
import com.davydh.covid_19.viewmodel.NationViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatsFragment extends Fragment {

    private FragmentStatsLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStatsLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // implementing ViewPager2 for BestRanking and WorstRanking
        ViewPager2 mViewPager2 = binding.ViewPagerRanking;

        NationViewModel nationViewModel = new ViewModelProvider(requireActivity()).get(NationViewModel.class);

        ArrayList<Nation> nationsData =
                (ArrayList<Nation>) Objects.requireNonNull(nationViewModel.getNationData().getValue()).getData();
        List<Integer> totalNewPositiveData = new ArrayList<>();

        for (Nation nation: nationsData) {
            totalNewPositiveData.add(nation.getTotaleNuoviPositivi());
        }

        ChartsViewPagerAdapter chartsViewPagerAdapter = new ChartsViewPagerAdapter(requireActivity());
        chartsViewPagerAdapter.addFragment(NationChartFragment.newInstance(nationsData));
        chartsViewPagerAdapter.addFragment(NewPositivesChartFragment.newInstance(totalNewPositiveData));
        chartsViewPagerAdapter.addFragment(ContagionsChartFragment.newInstance(nationsData));

        mViewPager2.setAdapter(chartsViewPagerAdapter);

        new TabLayoutMediator(binding.tabDots, mViewPager2,
                (tab, position) -> mViewPager2.setCurrentItem(tab.getPosition(), true)
        ).attach();

    }
}