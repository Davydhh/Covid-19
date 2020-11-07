package com.davydh.covid_19.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * It represent the JSON response associated with the endpoint
 * top-headlines of https://newsapi.org.
 */
public class TopHeadlinesApiResponse {

    private String status;
    private int totalResults;
    private List<Article> articles;

    public TopHeadlinesApiResponse(String status, int totalResults, List<Article> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @NotNull
    @Override
    public String toString() {
        return "TopHeadlinesResponseApi{" +
                "status='" + status + '\'' +
                ", totalResults=" + totalResults +
                ", articles=" + articles +
                '}';
    }
}
