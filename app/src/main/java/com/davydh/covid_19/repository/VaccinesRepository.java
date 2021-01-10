package com.davydh.covid_19.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.davydh.covid_19.model.Region;
import com.davydh.covid_19.model.Resource;
import com.davydh.covid_19.service.VaccinesService;
import com.davydh.covid_19.utils.Constants;
import com.davydh.covid_19.utils.DateTimeUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VaccinesRepository {
    private static final String TAG = VaccinesRepository.class.getSimpleName();

    private static VaccinesRepository instance;
    private final VaccinesService vaccinesService;

    private VaccinesRepository() {
        Retrofit retrofit =
                new Retrofit.Builder().baseUrl(Constants.VACCINES_API_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        vaccinesService = retrofit.create(VaccinesService.class);
    }

    public static synchronized VaccinesRepository getInstance() {
        if (instance == null) {
            instance = new VaccinesRepository();
        }
        return instance;
    }

    public void getRegionData(MutableLiveData<Resource<List<Region>>> regionDataResource) {
        Call<JsonObject> call = vaccinesService.getVacciniSummaryLatest();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                Resource<List<Region>> resource = new Resource<>();

                if (response.isSuccessful() && response.body() != null) {
                    List<Region> regionList = new ArrayList<>();

                    JsonArray data = response.body().getAsJsonArray("data");
                    for (int i = 0; i < data.size(); i++) {
                        JsonObject object = data.get(i).getAsJsonObject();
                        String area = object.get("area").getAsString();
                        int dosiSomministrate = object.get("dosi_somministrate").getAsInt();
                        int dosiConsegnate = object.get("dosi_consegnate").getAsInt();
                        double percentualeSomministrazione = object.get(
                                "percentuale_somministrazione").getAsDouble();

                        regionList.add(new Region(area, dosiSomministrate, dosiConsegnate,
                                percentualeSomministrazione));
                        resource.setData(regionList);
                    }
                } else {
                    try {
                        assert response.errorBody() != null;
                        Log.d(TAG, "onResponse: Errore --> " + response.errorBody().string());
                        resource.setStatusCode(response.code());
                        resource.setStatusMessage(response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                regionDataResource.postValue(resource);
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: Errore --> ", t);
                Resource<List<Region>> resource = new Resource<>();
                resource.setStatusMessage(t.getMessage());
                regionDataResource.postValue(resource);
            }
        });
    }

    public void getLastUpdate(MutableLiveData<Resource<String>> lastUpdateResource) {
        Call<JsonObject> call = vaccinesService.getLastUpdate();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                Resource<String> resource = new Resource<>();

                if (response.isSuccessful() && response.body() != null) {
                    String date = response.body().get("ultimo_aggiornamento").getAsString();
                    resource.setData(DateTimeUtil.getDataDate(date));
                } else {
                    try {
                        assert response.errorBody() != null;
                        Log.d(TAG, "onResponse: Errore --> " + response.errorBody().string());
                        resource.setStatusCode(response.code());
                        resource.setStatusMessage(response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                lastUpdateResource.setValue(resource);
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: Errore --> ", t);
                Resource<String> resource = new Resource<>();
                resource.setStatusMessage(t.getMessage());
                lastUpdateResource.setValue(resource);
            }
        });
    }
}
