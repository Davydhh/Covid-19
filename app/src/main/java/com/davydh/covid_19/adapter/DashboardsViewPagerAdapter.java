package com.davydh.covid_19.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardsViewPagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentListTitles = new ArrayList<>();

    public DashboardsViewPagerAdapter(@NonNull FragmentActivity fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragmentListTitles.size();
    }

    public String getPageTitle(int position){
        return mFragmentListTitles.get(position);
    }

    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentListTitles.add(title);
    }


}
