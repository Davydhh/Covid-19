package com.davydh.covid_19.service;

import com.davydh.covid_19.model.TopHeadlinesApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NewsService {
    @GET("everything")
    Call<TopHeadlinesApiResponse> getTopHeadlines(@Query("language") String language,
                                                  @Query("qInTitle") String qInTitle,
                                                  @Query("sortBy") String sortBy,
                                                  @Query("pageSize") int pageSize,
                                                  @Query("page") int page,
                                                  @Header("Authorization") String apiKey);


}
