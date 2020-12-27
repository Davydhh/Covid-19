package com.davydh.covid_19.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.davydh.covid_19.model.Nation;
import com.davydh.covid_19.model.Province;
import com.davydh.covid_19.model.Region;
import com.davydh.covid_19.model.Resource;
import com.davydh.covid_19.service.CovidService;
import com.davydh.covid_19.utils.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

public class CovidRepository {
    private static final String TAG = CovidRepository.class.getSimpleName();

    private static CovidRepository instance;
    private final CovidService covidService;

    private CovidRepository() {
        Retrofit retrofit =
                new Retrofit.Builder().baseUrl(Constants.COVID_API_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        covidService = retrofit.create(CovidService.class);
    }

    public static synchronized CovidRepository getInstance() {
        if (instance == null) {
            instance = new CovidRepository();
        }
        return instance;
    }

    public void getNationData(MutableLiveData<Resource<List<Nation>>> nationDataResource) {
        Call<JsonArray> call = covidService.getNationData();

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NotNull Call<JsonArray> call, @NotNull Response<JsonArray> response) {
                Resource<List<Nation>> resource = new Resource<>();

                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Nation> nationList = new ArrayList<>();

                    JsonArray body = response.body().getAsJsonArray();
                    for (int i = 0; i < body.size(); i++) {
                        JsonObject object = body.get(i).getAsJsonObject();
                        String data = object.get("data").getAsString();
                        String stato = object.get("stato").getAsString();
                        int ricoveratiConSintomi = object.get("ricoverati_con_sintomi").getAsInt();
                        int terapiaIntensiva = object.get("terapia_intensiva").getAsInt();
                        int totaleOspedalizzati = object.get("totale_ospedalizzati").getAsInt();
                        int isolamentoDomiciliare = object.get("isolamento_domiciliare").getAsInt();
                        int attualmentePositivi = object.get("totale_positivi").getAsInt();
                        int nuoviPositivi = object.get("variazione_totale_positivi").getAsInt();
                        int dimessi = object.get("dimessi_guariti").getAsInt();
                        int deceduti = object.get("deceduti").getAsInt();
                        int totaleCasi = object.get("totale_casi").getAsInt();
                        int tamponi = object.get("tamponi").getAsInt();
                        int totaleNuoviPositivi = object.get("nuovi_positivi").getAsInt();
                        JsonElement ingressiTerapiaIntensivaObj = object.get(
                                "ingressi_terapia_intensiva");
                        int ingressiTerapiaIntensiva;
                        if (!ingressiTerapiaIntensivaObj.isJsonNull()) {
                            ingressiTerapiaIntensiva = ingressiTerapiaIntensivaObj.getAsInt();
                        } else {
                            ingressiTerapiaIntensiva = 0;
                        }

                        JsonElement casiTestatiObj = object.get("casi_testati");
                        int casiTestati;
                        if (!casiTestatiObj.isJsonNull()) {
                            casiTestati = casiTestatiObj.getAsInt();
                        } else {
                            casiTestati = 0;
                        }

                        Nation nation = new Nation(data,stato,ricoveratiConSintomi,terapiaIntensiva,
                                totaleOspedalizzati,isolamentoDomiciliare,attualmentePositivi,
                                nuoviPositivi,dimessi,deceduti,totaleCasi,tamponi,
                                totaleNuoviPositivi, ingressiTerapiaIntensiva, casiTestati);

                        nationList.add(nation);
                        resource.setData(nationList);
                    }
                } else {
                    try {
                        Log.d(TAG, "onResponse: Errore --> " + response.errorBody().string());
                        resource.setStatusCode(response.code());
                        resource.setStatusMessage(response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                nationDataResource.postValue(resource);
            }

            @Override
            public void onFailure(@NotNull Call<JsonArray> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: Errore --> ", t);
                Resource<List<Nation>> resource = new Resource<>();
                resource.setStatusMessage(t.getMessage());
                nationDataResource.postValue(resource);
            }
        });
    }

    public void getLastRegionData(MutableLiveData<Resource<List<Region>>> regionDataResource) {
        Call<JsonArray> call = covidService.getLastRegionData();

        call.enqueue(new Callback<JsonArray>() {
            final Resource<List<Region>> resource = new Resource<>();
            @Override
            public void onResponse(@NotNull Call<JsonArray> call, @NotNull Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Region> regionList = new ArrayList<>();

                    JsonArray body = response.body().getAsJsonArray();
                    for (int i = 0; i < body.size(); i++) {
                        JsonObject object = body.get(i).getAsJsonObject();
                        String data = object.get("data").getAsString();
                        String stato = object.get("stato").getAsString();
                        int codiceRegionale = object.get("codice_regione").getAsInt();
                        String nome = object.get("denominazione_regione").getAsString();
                        double latitude = object.get("lat").getAsDouble();
                        double longitude = object.get("long").getAsDouble();
                        int ricoveratiConSintomi = object.get("ricoverati_con_sintomi").getAsInt();
                        int terapiaIntensiva = object.get("terapia_intensiva").getAsInt();
                        int totaleOspedalizzati = object.get("totale_ospedalizzati").getAsInt();
                        int isolamentoDomiciliare = object.get("isolamento_domiciliare").getAsInt();
                        int attualmentePositivi = object.get("totale_positivi").getAsInt();
                        int nuoviPositivi = object.get("variazione_totale_positivi").getAsInt();
                        int dimessi = object.get("dimessi_guariti").getAsInt();
                        int deceduti = object.get("deceduti").getAsInt();
                        int totaleCasi = object.get("totale_casi").getAsInt();
                        int tamponi = object.get("tamponi").getAsInt();
                        int totaleNuoviPositivi = object.get("nuovi_positivi").getAsInt();

                        Region region = new Region(data,stato,codiceRegionale,nome,latitude,
                                longitude,ricoveratiConSintomi,terapiaIntensiva,totaleOspedalizzati,
                                isolamentoDomiciliare,attualmentePositivi,nuoviPositivi,dimessi,
                                deceduti,totaleCasi,tamponi,"",totaleNuoviPositivi);

                        regionList.add(region);
                        resource.setData(regionList);
                    }
                } else {
                    try {
                        Log.d(TAG, "onResponse: Errore --> " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    resource.setStatusCode(response.code());
                    resource.setStatusMessage(response.message());
                }

                regionDataResource.postValue(resource);
            }

            @Override
            public void onFailure(@NotNull Call<JsonArray> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: Errore --> ", t);
                resource.setStatusMessage(t.getMessage());
                regionDataResource.postValue(resource);
            }
        });
    }

    public void getLastProvinceData(MutableLiveData<Resource<List<Province>>> provinceDataResource) {
        Call<JsonArray> call = covidService.getLastProvinceData();

        call.enqueue(new Callback<JsonArray>() {
            final Resource<List<Province>> resource = new Resource<>();
            @Override
            public void onResponse(@NotNull Call<JsonArray> call, @NotNull Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Province> provinceList = new ArrayList<>();
                    JsonArray body = response.body().getAsJsonArray();
                    for (int i = 0; i < body.size(); i++) {
                        JsonObject object = body.get(i).getAsJsonObject();
                        String data = object.get("data").getAsString();
                        String stato = object.get("stato").getAsString();
                        int codiceRegionale = object.get("codice_regione").getAsInt();
                        String nomeRegione = object.get("denominazione_regione").getAsString();
                        int codiceProvincia = object.get("codice_provincia").getAsInt();
                        String nomeProvincia = object.get("denominazione_provincia").getAsString();
                        String siglaProvincia = object.get("sigla_provincia").getAsString();
                        int totaleCasi = object.get("totale_casi").getAsInt();

                        if (!nomeProvincia.equals("In fase di definizione/aggiornamento") && !nomeProvincia.contains("Fuori Regione")) {
                            Province province = new Province(data, stato, codiceRegionale, nomeRegione,
                                    codiceProvincia, nomeProvincia, siglaProvincia,
                                    totaleCasi);

                            provinceList.add(province);
                            resource.setData(provinceList);
                        }
                    }
                } else {
                    Log.d(TAG, "onResponse: Errore --> " + response.errorBody());
                    resource.setStatusCode(response.code());
                    resource.setStatusMessage(response.message());
                }

                provinceDataResource.postValue(resource);
            }

            @Override
            public void onFailure(@NotNull Call<JsonArray> call, @NotNull Throwable t) {
                Log.d(TAG, "onResponse: Errore --> ", t);
                resource.setStatusMessage(t.getMessage());
                provinceDataResource.postValue(resource);
            }
        });
    }
}
