package com.davydh.covid_19.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.davydh.covid_19.databinding.FragmentCountryNewsDetailBinding;
import com.davydh.covid_19.model.Article;
import com.davydh.covid_19.utils.DateTimeUtil;

import org.jetbrains.annotations.NotNull;



/**
 * Fragment that shows the detail of a news selected by the user in
 * the list of articles shown in CountryNewsFragment.
 */
public class NewsDetailFragment extends Fragment {

    private static final String TAG = "CountryNewsDetail";

    private FragmentCountryNewsDetailBinding binding;

    private Article article;

    public NewsDetailFragment(Article article) {
        this.article = article;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCountryNewsDetailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.textViewArticleTitle.setText(article.getTitle());
        String content = article.getContent();

        if (content != null && !content.isEmpty()) {
            binding.textViewArticleDescriptionDetail.setText(content);
        } else {
            binding.textViewArticleDescriptionDetail.setText("Contenuto non disponibile");
        }
        String url = article.getUrlToImage();
        String newUrl;

        if (url != null) {
            // This action is a possible alternative to manage HTTP addresses that doesn't work
            // in the apps that target API level 28 or higher.
            // If it doesn't work, the other option is this one: https://developer.android.com/guide/topics/manifest/application-element#usesCleartextTraffic
            newUrl = url.replace("http://", "https://").trim();

            // Download the image associated with the article
            Glide.with(this).load(newUrl).into(binding.imageViewArticlePictureDetail);
        } else {
            binding.imageViewArticlePictureDetail.setVisibility(View.GONE);
        }
        binding.dataArticleDetail.setText(DateTimeUtil.getArticleDate(article.getPublishedAt()));
        binding.linkArticle.setText(article.getUrl());
    }
}
