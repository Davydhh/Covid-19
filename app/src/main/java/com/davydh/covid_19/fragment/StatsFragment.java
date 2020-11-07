package com.davydh.covid_19.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.davydh.covid_19.R;
import com.davydh.covid_19.model.Nation;
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
import java.util.Objects;

public class StatsFragment extends Fragment {

    private LineChart nationLineChart;
    private LineChart newRecoveredLineChart;
    private BarChart contagionsPercentageBarChart;
    private List<Entry> infectedEntries = new ArrayList<>();
    private List<Entry> recoveredEntries = new ArrayList<>();
    private List<Entry> deadEntries = new ArrayList<>();
    private List<BarEntry> contagionsPercentage = new ArrayList<>();
    private List<Entry> totalNewPositiveEntries = new ArrayList<>();

    public StatsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nationLineChart = Objects.requireNonNull(getActivity()).findViewById(R.id.nation_chart);
        newRecoveredLineChart = getActivity().findViewById(R.id.new_recovered_chart);
        contagionsPercentageBarChart = getActivity().findViewById(R.id.contagions_percentage_chart);

        List<Nation> nationsData = DashboardFragment.nationsData;
        List<Integer> totalNewPositiveData = DashboardFragment.totalNewPositiveData;

        setChartData(nationsData,totalNewPositiveData);
    }

    private void setChartData(List<Nation> inputData, List<Integer> inputDataNewRecovered) {
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
        BarDataSet contagionsPercentageDataSet = new BarDataSet(contagionsPercentage, "Percentuale" +
                " " +
                "contagi");
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
        nationLineChart.setData(nationData);
        setLineChartStyle(nationLineChart);
        nationLineChart.invalidate();

        LineData positiveDataSets = new LineData(newTotalInfectedDataSet);
        newRecoveredLineChart.setData(positiveDataSets);
        setLineChartStyle(newRecoveredLineChart);
        newRecoveredLineChart.invalidate();

        BarData contagionPercentageDataSets = new BarData(contagionsPercentageDataSet);
        contagionsPercentageBarChart.setData(contagionPercentageDataSets);
        setBarChartStyle(contagionsPercentageBarChart);
        contagionsPercentageBarChart.invalidate();
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