package com.davydh.covid_19.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.davydh.covid_19.databinding.FragmentNationChartBinding;
import com.davydh.covid_19.model.Nation;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NationChartFragment extends Fragment {
    private FragmentNationChartBinding binding;

    public static NationChartFragment newInstance(ArrayList<Nation> nationsData) {
        NationChartFragment myFragment = new NationChartFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList("nationsData", nationsData);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNationChartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        ArrayList<Nation> nationsData = getArguments().getParcelableArrayList("nationsData");

        List<Entry> infectedEntries = new ArrayList<>();
        List<Entry> recoveredEntries = new ArrayList<>();
        List<Entry> deadEntries = new ArrayList<>();

        float count = 0;
        for (int i = 0; i < nationsData.size(); i++) {
            Nation nation = nationsData.get(i);
            infectedEntries.add(new Entry(count,(float) nation.getAttualmentePositivi()));
            recoveredEntries.add(new Entry(count,(float) nation.getDimessi()));
            deadEntries.add(new Entry(count,(float) nation.getDeceduti()));
            count++;
        }

        LineDataSet infectedDataSet = new LineDataSet(infectedEntries, "Attualmente positivi");
        infectedDataSet.setDrawCircles(false);
        LineDataSet recoveredDataSet = new LineDataSet(recoveredEntries, "Dimessi");
        recoveredDataSet.setDrawCircles(false);
        LineDataSet deadDataSet = new LineDataSet(deadEntries, "Deceduti");
        deadDataSet.setDrawCircles(false);

        infectedDataSet.setColor(Color.RED);
        recoveredDataSet.setColor(Color.GREEN);
        deadDataSet.setColor(Color.BLACK);

        List<ILineDataSet> nationDataSets = new ArrayList<>();
        nationDataSets.add(infectedDataSet);
        nationDataSets.add(recoveredDataSet);
        nationDataSets.add(deadDataSet);

        LineData nationData = new LineData(nationDataSets);
        binding.nationChart.setData(nationData);
        setLineChartStyle(binding.nationChart);
        binding.nationChart.invalidate();
    }

    private void setLineChartStyle(LineChart chart) {
        chart.setExtraBottomOffset(10f);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);

        YAxis leftYAxis = chart.getAxisRight();
        leftYAxis.setDrawGridLines(false);
        leftYAxis.setDrawLabels(false);
        leftYAxis.setDrawAxisLine(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        Legend legend = chart.getLegend();
        legend.setFormSize(10f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(10f);
        legend.setXEntrySpace(10f);

        Description description = chart.getDescription();
        description.setEnabled(false);
    }
}