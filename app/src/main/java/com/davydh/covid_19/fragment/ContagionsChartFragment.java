package com.davydh.covid_19.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.davydh.covid_19.databinding.FragmentContagionsChartBinding;
import com.davydh.covid_19.model.Nation;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContagionsChartFragment extends Fragment {

    private FragmentContagionsChartBinding binding;

    private final List<Nation> nationsData;

    private ContagionsChartFragment(List<Nation> nationsData) {
        this.nationsData = nationsData;
    }

    public static ContagionsChartFragment newInstance(List<Nation> nationsData) {
        return new ContagionsChartFragment(nationsData);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContagionsChartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<BarEntry> contagionsPercentage = new ArrayList<>();

        final List<String> xAxisLabel = new ArrayList<>();

        float count = 0;
        for (int i = 0; i < nationsData.size(); i++) {
            Nation nation = nationsData.get(i);
            int varTamponi = nation.getTamponi();

            if (i > 0) {
                varTamponi -= nationsData.get(i - 1).getTamponi();
            }

            contagionsPercentage.add(new BarEntry(count,
                    ((float) nation.getTotaleNuoviPositivi() /  varTamponi  * 100)));

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format2 = new SimpleDateFormat("dd-MM");
            Date date = null;
            try {
                date = format1.parse(nation.getData());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            xAxisLabel.add(format2.format(date));

            count++;
        }

        BarDataSet contagionsPercentageDataSet = new BarDataSet(contagionsPercentage,
                "Percentuale contagi (rapporto tamponi - nuovi positivi)");
        contagionsPercentageDataSet.setColor(Color.GRAY);

        BarData contagionPercentageDataSets = new BarData(contagionsPercentageDataSet);
        binding.contagionsPercentageChart.setData(contagionPercentageDataSets);
        setBarChartStyle(binding.contagionsPercentageChart, xAxisLabel);
        binding.contagionsPercentageChart.invalidate();
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
        xAxis.setLabelCount(10);
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