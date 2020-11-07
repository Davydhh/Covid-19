package com.davydh.covid_19.service;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CovidService {
    @GET("dpc-covid19-ita-andamento-nazionale.json")
    Call<JsonArray> getNationData();

    @GET("dpc-covid19-ita-regioni-latest.json")
    Call<JsonArray> getLastRegionData();

    @GET("dpc-covid19-ita-province-latest.json")
    Call<JsonArray> getLastProvinceData();
}
