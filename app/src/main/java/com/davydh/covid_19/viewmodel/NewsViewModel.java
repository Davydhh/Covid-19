package com.davydh.covid_19.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.davydh.covid_19.model.Article;
import com.davydh.covid_19.model.Resource;
import com.davydh.covid_19.repository.NewsRepository;

import java.util.List;

/**
 * ViewModel to manage the news associated with a country.
 */
public class NewsViewModel extends ViewModel {

    private static final String TAG = "CountryNewsViewModel";

    private MutableLiveData<Resource<List<Article>>> articles;
    private int page = 1;
    private int currentResults;
    private boolean isLoading;

    public LiveData<Resource<List<Article>>> getArticlesResource() {
        if (articles == null) {
            articles = new MutableLiveData<>();
            Log.d(TAG, "getArticles: Download the news from Internet");
            NewsRepository.getInstance().getCountryNews(articles, page);
        }

        Log.d(TAG, "getArticles: News");
        return articles;
    }

    public LiveData<Resource<List<Article>>> getMoreArticlesResource() {
        Log.d(TAG, "getArticles: More News");
        NewsRepository.getInstance().getCountryNews(articles, page);
        return articles;
    }

    /**
     * It returns the reference to the LiveData object associated with the list of articles.
     *
     * @return The LiveData object associated with the list of articles.
     */
    public MutableLiveData<Resource<List<Article>>> getArticlesLiveData() {
        return articles;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCurrentResults() {
        return currentResults;
    }

    public void setCurrentResults(int currentResults) {
        this.currentResults = currentResults;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
