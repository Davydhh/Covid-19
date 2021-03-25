package com.davydh.covid_19.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davydh.covid_19.R;
import com.davydh.covid_19.databinding.FragmentAnagraficaChartBinding;
import com.davydh.covid_19.databinding.FragmentContagionsChartBinding;
import com.davydh.covid_19.model.AnagraficaVaccini;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AnagraficaChartFragment extends Fragment {
    private FragmentAnagraficaChartBinding binding;

    private final List<AnagraficaVaccini> anagraficaData;

    private AnagraficaChartFragment(List<AnagraficaVaccini> anagraficaData) {
        this.anagraficaData = anagraficaData;
    }

    public static AnagraficaChartFragment newInstance(List<AnagraficaVaccini> anagraficaData) {
        return new AnagraficaChartFragment(anagraficaData);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAnagraficaChartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<BarEntry> anagraficaEntry = new ArrayList<>();
        final List<String> xAxisLabel = new ArrayList<>();

        float count = 0;
        for(int i = 0; i < this.anagraficaData.size(); i++) {
            AnagraficaVaccini a = this.anagraficaData.get(i);
            anagraficaEntry.add(new BarEntry(count, (float) a.getTotale()));
            xAxisLabel.add(a.getFasciaAnagrafica());
            count++;
        }

        BarDataSet anagraficaDataSet = new BarDataSet(anagraficaEntry, "Vaccinazioni per fascie " +
                "di età");
        anagraficaDataSet.setColor(ContextCompat.getColor(requireContext(), R.color.vaccini));

        BarData anagraficaDataSets = new BarData(anagraficaDataSet);
        binding.anagraficaVacciniChart.setData(anagraficaDataSets);
        setBarChartStyle(binding.anagraficaVacciniChart, xAxisLabel);
        binding.anagraficaVacciniChart.invalidate();
    }

    private void setBarChartStyle(BarChart chart, List<String> xLables) {
        chart.setExtraBottomOffset(10f);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinimum(0f);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setAxisMinimum(0f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setLabelCount(xLables.size());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLables));
        xAxis.setGranularity(1f);

        Legend legend = chart.getLegend();
        legend.setFormSize(10f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(10f);
        legend.setXEntrySpace(10f);

        Description description = chart.getDescription();
        description.setEnabled(false);
    }
}