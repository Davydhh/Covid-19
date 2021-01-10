package com.davydh.covid_19.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface VaccinesService {
    @GET("vaccini-summary-latest.json")
    Call<JsonObject> getVacciniSummaryLatest();

    @GET("last-update-dataset.json")
    Call<JsonObject> getLastUpdate();
}
