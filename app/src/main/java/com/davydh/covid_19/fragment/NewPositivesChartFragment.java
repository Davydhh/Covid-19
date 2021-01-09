package com.davydh.covid_19.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davydh.covid_19.R;
import com.davydh.covid_19.databinding.FragmentNewPositivesChartBinding;
import com.davydh.covid_19.model.Nation;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NewPositivesChartFragment extends Fragment {
    private FragmentNewPositivesChartBinding binding;
    private final List<Integer> totalNewPositiveData;

    private NewPositivesChartFragment(List<Integer> totalNewPositiveData) {
        this.totalNewPositiveData = totalNewPositiveData;
    }

    public static NewPositivesChartFragment newInstance(List<Integer> totalNewPositiveData) {
        return new NewPositivesChartFragment(totalNewPositiveData);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewPositivesChartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Entry> totalNewPositiveEntries = new ArrayList<>();

        for (int i = 0; i < totalNewPositiveData.size(); i++) {
            totalNewPositiveEntries.add(new Entry(i,(float) totalNewPositiveData.get(i)));
        }

        LineDataSet newTotalInfectedDataSet = new LineDataSet(totalNewPositiveEntries, "Totale nuovi casi positivi");
        newTotalInfectedDataSet.setDrawCircles(false);
        newTotalInfectedDataSet.setColor(Color.BLUE);

        LineData positiveDataSets = new LineData(newTotalInfectedDataSet);
        binding.newRecoveredChart.setData(positiveDataSets);
        setLineChartStyle(binding.newRecoveredChart);
        binding.newRecoveredChart.invalidate();
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