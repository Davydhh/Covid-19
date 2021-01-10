package com.davydh.covid_19.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.davydh.covid_19.R;
import com.davydh.covid_19.databinding.FragmentVaccinesBinding;
import com.davydh.covid_19.model.Region;
import com.davydh.covid_19.viewmodel.LastUpdateViewModel;
import com.davydh.covid_19.viewmodel.RegionViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class VaccinesFragment extends Fragment {
    private static final String TAG = VaccinesFragment.class.getSimpleName();

    private FragmentVaccinesBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVaccinesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RegionViewModel regionViewModel =
                new ViewModelProvider(requireActivity()).get(RegionViewModel.class);

        regionViewModel.getLastVaccinesRegionData().observe(getViewLifecycleOwner(), listResource -> {
            List<Region> regionData = listResource.getData();
            if (regionData != null) {
                DataTable dataTable = binding.dataTable;

                DataTableHeader header = new DataTableHeader.Builder()
                        .item("Regioni",  1)
                        .item("Somministrazioni", 1)
                        .item("Dosi", 1)
                        .item("%", 1).build();

                ArrayList<DataTableRow> rows = new ArrayList<>();

                int vaccinated = 0;
                int somministrazioniTotale = 0;
                int dosiConsegnateTotale = 0;

                for (Region region: regionData) {
                    vaccinated += region.getDosiSomministrate();
                    somministrazioniTotale += region.getDosiSomministrate();
                    dosiConsegnateTotale += region.getDosiConsegnate();

                    switch (region.getArea()) {
                        case "ABR":
                            region.setNome("Abruzzo");
                            break;
                        case "BAS":
                            region.setNome("Basilicata");
                            break;
                        case "CAL":
                            region.setNome("Calabria");
                            break;
                        case "CAM":
                            region.setNome("Campania");
                            break;
                        case "EMR":
                            region.setNome("Emilia-Romagna");
                            break;
                        case "FVG":
                            region.setNome("Friuli-Venezia-Giulia");
                            break;
                        case "LAZ":
                            region.setNome("Lazio");
                            break;
                        case "LIG":
                            region.setNome("Liguria");
                            break;
                        case "LOM":
                            region.setNome("Lombardia");
                            break;
                        case "MAR":
                            region.setNome("Marche");
                            break;
                        case "MOL":
                            region.setNome("Molise");
                            break;
                        case "PAB":
                            region.setNome("P.A. Bolzano");
                            break;
                        case "PAT":
                            region.setNome("P.A. Trento");
                            break;
                        case "PIE":
                            region.setNome("Piemonte");
                            break;
                        case "PUG":
                            region.setNome("Puglia");
                            break;
                        case "SAR":
                            region.setNome("Sardegna");
                            break;
                        case "SIC":
                            region.setNome("Sicilia");
                            break;
                        case "TOS":
                            region.setNome("Toscana");
                            break;
                        case "UMB":
                            region.setNome("Umbria");
                            break;
                        case "VDA":
                            region.setNome("Valle d'Aosta");
                            break;
                        case "VEN":
                            region.setNome("Veneto");
                            break;
                        default:
                            break;
                    }

                    DataTableRow row = new DataTableRow.Builder()
                            .value(region.getNome())
                            .value(String.format(Locale.ITALIAN, "%,d",
                                    Long.parseLong(String.valueOf(region.getDosiSomministrate()))))
                            .value(String.format(Locale.ITALIAN, "%,d",
                                    Long.parseLong(String.valueOf(region.getDosiConsegnate()))))
                            .value(String.valueOf(region.getPercentualeSomministrazione()).concat("%"))
                            .build();
                    rows.add(row);
                }
                binding.totalVaccinatedTextView.setText(String.format(Locale.ITALIAN, "%,d",
                        Long.parseLong(String.valueOf(vaccinated))));
                dataTable.setHeader(header);

                DataTableRow row = new DataTableRow.Builder()
                        .value("Totale")
                        .value(String.format(Locale.ITALIAN, "%,d",
                                Long.parseLong(String.valueOf(somministrazioniTotale))))
                        .value(String.format(Locale.ITALIAN, "%,d",
                                Long.parseLong(String.valueOf(dosiConsegnateTotale))))
                        .value(new DecimalFormat("#.#").format((double) somministrazioniTotale / dosiConsegnateTotale * 100).concat(
                                "%"))
                        .build();
                rows.add(row);

                dataTable.setRows(rows);
                dataTable.inflate(requireContext());
            } else {
                Snackbar.make(requireView(), "Impossibile scaricare i dati relativi ai vaccini " +
                                "delle regioni"
                        , BaseTransientBottomBar.LENGTH_LONG).show();
                Log.d(TAG, "onViewCreated: Errore --> Code: " + listResource.getStatusCode() + " " +
                        "Message: " + listResource.getStatusMessage());
            }
        });

        LastUpdateViewModel lastUpdateViewModel =
                new ViewModelProvider(requireActivity()).get(LastUpdateViewModel.class);

        lastUpdateViewModel.getLastUpdate().observe(getViewLifecycleOwner(), stringResource -> {
            String date = stringResource.getData();
            if (date != null) {
                binding.vaccinesDataText.setText(getString(R.string.last_update, date));
            } else {
                Snackbar.make(requireView(), "Impossibile scaricare la data di aggiornamento"
                        , BaseTransientBottomBar.LENGTH_LONG).show();
                Log.d(TAG, "onViewCreated: Errore --> Code: " + stringResource.getStatusCode() + " " +
                        "Message: " + stringResource.getStatusMessage());
            }
        });
    }
}