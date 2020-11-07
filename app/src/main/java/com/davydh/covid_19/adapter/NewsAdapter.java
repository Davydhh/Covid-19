package com.davydh.covid_19.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.davydh.covid_19.R;
import com.davydh.covid_19.model.Article;
import com.davydh.covid_19.utils.DateTimeUtil;

import java.util.List;

/**
 * RecyclerView Adapter to show the news related to a country. It also supports the visualization
 * of a ProgressBar used to show the loading item while the news are loading using the lazy loading strategy.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "CountryNewsAdapter";

    private static final int ARTICLE_VIEW_TYPE = 0;
    private static final int LOADING_VIEW_TYPE = 1;

    /**
     * Interface to intercept the event when the user
     * presses an item in the RecyclerView.
     */
    public interface OnItemClickListener {
        void onItemClick(Article article);
    }

    private List<Article> articleList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public NewsAdapter(Context context, List<Article> articleList, OnItemClickListener onItemClickListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.articleList = articleList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // It creates new views (invoked by the layout manager)

        if (viewType == ARTICLE_VIEW_TYPE) {
            View view = this.layoutInflater.inflate(R.layout.country_news_item, parent, false);
            return new CountryNewsViewHolder(view);
        } else {
            View view = this.layoutInflater.inflate(R.layout.loading_item, parent, false);
            return new LoadingNewsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // It replaces the content of a View with the element located
        // in the position "position" of the dataset.

        if (holder instanceof CountryNewsViewHolder) {
            ((CountryNewsViewHolder) holder).bind(articleList.get(position), this.onItemClickListener);
        } else if (holder instanceof LoadingNewsViewHolder) {
            ((LoadingNewsViewHolder) holder).progressBarLoadingNews.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        if (articleList != null) {
            return articleList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (articleList.get(position) == null) {
            return LOADING_VIEW_TYPE;
        } else {
            return ARTICLE_VIEW_TYPE;
        }
    }

    /**
     * It sets the data in the adapter associated with the RecyclerView.
     *
     * @param articleList The data to show in the RecyclerView
     */
    public void setData(List<Article> articleList) {
        if (articleList != null) {
            this.articleList = articleList;
            notifyDataSetChanged();
        }
    }

    /**
     * It provides a reference to the views for each news item in the RecyclerView.
     */
    static class CountryNewsViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewArticleImage;
        TextView textViewArticleTitle;
        TextView textViewArticleDescription;
        TextView textViewArticleDate;

        CountryNewsViewHolder(View view) {
            super(view);
            imageViewArticleImage = view.findViewById(R.id.imageViewArticlePicture);
            textViewArticleTitle = view.findViewById(R.id.textViewArticleTitle);
            textViewArticleDescription = view.findViewById(R.id.textViewArticleDescription);
            textViewArticleDate = view.findViewById(R.id.textViewArticleDate);
        }

        void bind(Article article, OnItemClickListener onItemClickListener) {

            String url = article.getUrlToImage();
            String newUrl;

            if (url != null) {
                // This action is a possible alternative to manage HTTP addresses that doesn't work
                // in the apps that target API level 28 or higher.
                // If it doesn't work, the other option is this one: https://developer.android.com/guide/topics/manifest/application-element#usesCleartextTraffic
                newUrl = url.replace("http://", "https://").trim();

                // Download the image associated with the article
                Glide.with(itemView.getContext()).load(newUrl).into(imageViewArticleImage);
            }

            textViewArticleTitle.setText(article.getTitle());
            textViewArticleDescription.setText(article.getDescription());
            textViewArticleDate.setText(DateTimeUtil.getArticleDate(article.getPublishedAt()));

            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(article));
        }
    }

    /**
     * It provides a reference to the loading view item in the RecyclerView.
     */
    static class LoadingNewsViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBarLoadingNews;

        LoadingNewsViewHolder(View view) {
            super(view);
            progressBarLoadingNews = view.findViewById(R.id.progressBarLoadingNews);
        }
    }
}
