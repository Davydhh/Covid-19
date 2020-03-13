package com.davydh.covid_19.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.davydh.covid_19.R;
import com.davydh.covid_19.activity.MainActivity;

public class DashboardFragment extends Fragment {

    private TextView infectedText;
    private TextView recoveredText;
    private TextView deadText;

    public DashboardFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        infectedText = getActivity().findViewById(R.id.infected_text);
        recoveredText = getActivity().findViewById(R.id.recovered_text);
        deadText = getActivity().findViewById(R.id.dead_text);



    }


    public TextView getInfectedText() {
        return infectedText;
    }

    public TextView getRecoveredText() {
        return recoveredText;
    }

    public TextView getDeadText() {
        return deadText;
    }
}
