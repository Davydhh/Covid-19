package com.davydh.covid_19.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.davydh.covid_19.R;
import com.davydh.covid_19.model.Nation;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class StatsFragment extends Fragment {

    private LineChart nationLineChart;
    private LineChart newRecoveredLineChart;
    private List<Entry> infectedEntries = new ArrayList<>();
    private List<Entry> recoveredEntries = new ArrayList<>();
    private List<Entry> deadEntries = new ArrayList<>();
    private List<Entry> newInfectedEntries = new ArrayList<>();

    public StatsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nationLineChart = getActivity().findViewById(R.id.nation_chart);
        newRecoveredLineChart = getActivity().findViewById(R.id.new_recovered_chart);

        List<Nation> nationsData;
        if (DashboardFragment.getNationsData() != null || !DashboardFragment.getNationsData().isEmpty()) {
            nationsData = DashboardFragment.getNationsData();
        } else {
            nationsData = new DashboardFragment().getNationsData();
        }

        setChartData(nationsData);
    }

    private void setChartData(List<Nation> inputData) {
        for (Nation nation: inputData) {
            String day = nation.getData().substring(8,10);
            String month = nation.getData().substring(5,7);
            String data = month + "." + day;
            infectedEntries.add(new Entry(Float.parseFloat(data),(float) nation.getAttualmentePositivi()));
            recoveredEntries.add(new Entry(Float.parseFloat(data),(float) nation.getDimessi()));
            deadEntries.add(new Entry(Float.parseFloat(data),(float) nation.getDeceduti()));
            newInfectedEntries.add(new Entry(Float.parseFloat(data), nation.getNuoviPositivi()));
        }

        LineDataSet infectedDataSet = new LineDataSet(infectedEntries, "Attualmente positivi");
        LineDataSet recoveredDataSet = new LineDataSet(recoveredEntries, "Dimessi");
        LineDataSet deadDataSet = new LineDataSet(deadEntries, "Deceduti");

        LineDataSet newInfectedDataSet = new LineDataSet(newInfectedEntries, "Nuovi casi positivi");
        newInfectedDataSet.setColor(Color.RED);

        infectedDataSet.setColor(Color.RED);
        recoveredDataSet.setColor(Color.GREEN);
        deadDataSet.setColor(Color.BLACK);

        List<ILineDataSet> nationDataSets = new ArrayList<ILineDataSet>();
        nationDataSets.add(infectedDataSet);
        nationDataSets.add(recoveredDataSet);
        nationDataSets.add(deadDataSet);
        LineData nationData = new LineData(nationDataSets);
        nationLineChart.setData(nationData);
        Description description = new Description();
        description.setText("");
        nationLineChart.setDescription(description);

        YAxis nationYAxis = nationLineChart.getAxisLeft();
        nationYAxis.setAxisMinimum(0f);

        nationLineChart.invalidate();

        LineData newInfectedData = new LineData(newInfectedDataSet);
        newRecoveredLineChart.setData(newInfectedData);
        newRecoveredLineChart.setDescription(description);

        YAxis infectedYAxis = newRecoveredLineChart.getAxisLeft();
        infectedYAxis.setAxisMinimum(0f);

        newRecoveredLineChart.invalidate();
    }
}
