package com.davydh.covid_19.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.davydh.covid_19.databinding.FragmentStatsLayoutBinding;
import com.davydh.covid_19.model.Nation;
import com.davydh.covid_19.viewmodel.NationViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {

    private FragmentStatsLayoutBinding binding;

    public StatsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStatsLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NationViewModel nationViewModel = new ViewModelProvider(requireActivity()).get(NationViewModel.class);

        List<Nation> nationsData = nationViewModel.getNationData().getValue().getData();
        List<Integer> totalNewPositiveData = new ArrayList<>();

        for (Nation nation: nationsData) {
            totalNewPositiveData.add(nation.getTotaleNuoviPositivi());
        }

        setChartData(nationsData,totalNewPositiveData);
    }

    private void setChartData(List<Nation> inputData, List<Integer> inputDataNewRecovered) {
        List<Entry> infectedEntries = new ArrayList<>();
        List<Entry> recoveredEntries = new ArrayList<>();
        List<Entry> deadEntries = new ArrayList<>();
        List<BarEntry> contagionsPercentage = new ArrayList<>();
        List<Entry> totalNewPositiveEntries = new ArrayList<>();

        float count = 0;
        for (int i = 0; i < inputData.size(); i++) {
            Nation nation = inputData.get(i);
            infectedEntries.add(new Entry(count,(float) nation.getAttualmentePositivi()));
            recoveredEntries.add(new Entry(count,(float) nation.getDimessi()));
            deadEntries.add(new Entry(count,(float) nation.getDeceduti()));
            int varTamponi = nation.getTamponi();

            if (i > 0) {
               varTamponi -= inputData.get(i - 1).getTamponi();
            }

            contagionsPercentage.add(new BarEntry(count,
                    ((float) nation.getTotaleNuoviPositivi() /  varTamponi  * 100)));
            count++;
        }

        for (int i = 0; i < inputDataNewRecovered.size(); i++) {
            totalNewPositiveEntries.add(new Entry(i,(float) inputDataNewRecovered.get(i)));
        }

        LineDataSet infectedDataSet = new LineDataSet(infectedEntries, "Attualmente positivi");
        infectedDataSet.setDrawCircles(false);
        LineDataSet recoveredDataSet = new LineDataSet(recoveredEntries, "Dimessi");
        recoveredDataSet.setDrawCircles(false);
        LineDataSet deadDataSet = new LineDataSet(deadEntries, "Deceduti");
        deadDataSet.setDrawCircles(false);
        BarDataSet contagionsPercentageDataSet = new BarDataSet(contagionsPercentage,
                "Percentuale contagi (rapporto tamponi - nuovi positivi)");
        LineDataSet newTotalInfectedDataSet = new LineDataSet(totalNewPositiveEntries, "Totale nuovi casi positivi");
        newTotalInfectedDataSet.setDrawCircles(false);

        contagionsPercentageDataSet.setColor(Color.GRAY);
        infectedDataSet.setColor(Color.RED);
        recoveredDataSet.setColor(Color.GREEN);
        deadDataSet.setColor(Color.BLACK);
        newTotalInfectedDataSet.setColor(Color.BLUE);

        List<ILineDataSet> nationDataSets = new ArrayList<>();
        nationDataSets.add(infectedDataSet);
        nationDataSets.add(recoveredDataSet);
        nationDataSets.add(deadDataSet);

        LineData nationData = new LineData(nationDataSets);
        binding.nationChart.setData(nationData);
        setLineChartStyle(binding.nationChart);
        binding.nationChart.invalidate();

        LineData positiveDataSets = new LineData(newTotalInfectedDataSet);
        binding.newRecoveredChart.setData(positiveDataSets);
        setLineChartStyle(binding.newRecoveredChart);
        binding.newRecoveredChart.invalidate();

        BarData contagionPercentageDataSets = new BarData(contagionsPercentageDataSet);
        binding.contagionsPercentageChart.setData(contagionPercentageDataSets);
        setBarChartStyle(binding.contagionsPercentageChart);
        binding.contagionsPercentageChart.invalidate();
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

    private void setBarChartStyle(BarChart chart) {
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