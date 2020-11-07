package com.davydh.covid_19.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.davydh.covid_19.model.Article;
import com.davydh.covid_19.model.Resource;
import com.davydh.covid_19.model.TopHeadlinesApiResponse;
import com.davydh.covid_19.service.NewsService;
import com.davydh.covid_19.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Repository layer to get the news.
 */
public class NewsRepository {

    private static final String TAG = "NewsRepository";

    private static NewsRepository instance;
    private final NewsService newsService;

    /**
     * Private constructor to avoid the instantiation outside the class.
     */
    private NewsRepository() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.NEWS_API_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        newsService = retrofit.create(NewsService.class);
    }

    public static synchronized NewsRepository getInstance() {
        if (instance == null) {
            instance = new NewsRepository();
        }
        return instance;
    }

    public void getCountryNews(MutableLiveData<Resource<List<Article>>> articlesResource, int page) {
        Call<TopHeadlinesApiResponse> call = newsService.getTopHeadlines("it","coronavirus OR " +
                        "covid OR dpcm", "publishedAt", Constants.NEWS_API_PAGE_SIZE, page, Constants.NEWS_API_KEY);

        Log.d(TAG, "Page: " + page);

        // It shows the use of method enqueue to do the HTTP request asynchronously.
        call.enqueue(new Callback<TopHeadlinesApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<TopHeadlinesApiResponse> call, @NotNull Response<TopHeadlinesApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Resource<List<Article>> resource = new Resource<>();

                    if (articlesResource.getValue() != null && articlesResource.getValue().getData() != null) {
                        List<Article> currentArticleList = articlesResource.getValue().getData();

                        // It removes the null element added to show the loading item in the RecyclerView
                        currentArticleList.remove(currentArticleList.size() - 1);
                        currentArticleList.addAll(response.body().getArticles());
                        resource.setData(currentArticleList);
                    } else {
                        resource.setData(response.body().getArticles());
                    }

                    resource.setTotalResults(response.body().getTotalResults());
                    resource.setStatusCode(response.code());
                    resource.setStatusMessage(response.message());
                    resource.setLoading(false);
                    articlesResource.postValue(resource);
                } else if (response.errorBody() != null) {
                    Resource<List<Article>> resource = new Resource<>();
                    resource.setStatusCode(response.code());
                    try {
                        resource.setStatusMessage(response.errorBody().string() + "- " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    articlesResource.postValue(resource);
                }
            }

            @Override
            public void onFailure(Call<TopHeadlinesApiResponse> call, Throwable t) {
                Resource<List<Article>> resource = new Resource<>();
                resource.setStatusMessage(t.getMessage());
                articlesResource.postValue(resource);
            }
        });
    }
}
