package com.davydh.covid_19.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davydh.covid_19.activity.MainActivity;
import com.davydh.covid_19.adapter.NewsAdapter;
import com.davydh.covid_19.databinding.FragmentCountryNewsBinding;
import com.davydh.covid_19.model.Article;
import com.davydh.covid_19.model.Resource;
import com.davydh.covid_19.viewmodel.NewsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Fragment that shows the news related to a country.
 */
public class NewsFragment extends Fragment {

    private static final String TAG = "CountryNewsFragment";

    private NewsViewModel newsViewModel;

    private FragmentCountryNewsBinding binding;

    private int totalItemCount;
    private int lastVisibleItem;
    private int visibleItemCount;
    private final int threshold = 1;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View Binding
        binding = FragmentCountryNewsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create a ViewModel the first time the system calls a Fragment's onViewCreated() method.
        // Re-created fragments receive the same CountryNewsViewModel instance created by the first Fragment.
        newsViewModel =
                new ViewModelProvider(requireActivity()).get(NewsViewModel.class);

        // LayoutManager that is associated with the RecyclerView.
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.countryNewsRecyclerView.setLayoutManager(layoutManager);

        NewsAdapter newsAdapter = new NewsAdapter(getActivity(),
                getArticleList(), article -> {
            // It opens an instance of NewsDetailFragment that shows the detail of the article clicked by the user.

            NewsDetailFragment newsDetailFragment = new NewsDetailFragment(article);
            ((MainActivity) requireActivity()).replaceFragment(newsDetailFragment, true);
        });
        binding.countryNewsRecyclerView.setAdapter(newsAdapter);

        binding.countryNewsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                visibleItemCount = layoutManager.getChildCount();

                // Condition to enable the loading of other news while the user scrolls the list
                if (totalItemCount == visibleItemCount || (
                        totalItemCount <= (lastVisibleItem + threshold) && dy > 0 && !newsViewModel.isLoading()) &&
                        newsViewModel.getArticlesLiveData().getValue() != null &&
                        newsViewModel.getCurrentResults() != newsViewModel.getArticlesLiveData().getValue().getTotalResults()
                ) {
                    Resource<List<Article>> articleListResource = new Resource<>();

                    MutableLiveData<Resource<List<Article>>> articleListMutableLiveData = newsViewModel.getArticlesLiveData();

                    if (articleListMutableLiveData.getValue() != null) {

                        newsViewModel.setLoading(true);

                        List<Article> currentArticleList = articleListMutableLiveData.getValue().getData();

                        // It adds a null element to enable the visualization of the loading item (it is managed by the class CountryNewsAdapter)
                        currentArticleList.add(null);
                        articleListResource.setData(currentArticleList);
                        articleListResource.setStatusMessage(articleListMutableLiveData.getValue().getStatusMessage());
                        articleListResource.setTotalResults(articleListMutableLiveData.getValue().getTotalResults());
                        articleListResource.setStatusCode(articleListMutableLiveData.getValue().getStatusCode());
                        articleListResource.setLoading(true);
                        articleListMutableLiveData.postValue(articleListResource);

                        int page = newsViewModel.getPage() + 1;
                        newsViewModel.setPage(page);

                        newsViewModel.getMoreArticlesResource();
                    }
                }
            }
        });

        newsViewModel.getArticlesResource().observe(getViewLifecycleOwner(), articlesResource -> {
            newsAdapter.setData(articlesResource.getData());

            if (!articlesResource.isLoading()) {
                newsViewModel.setLoading(false);
                if (articlesResource.getData() != null) {
                    newsViewModel.setCurrentResults(articlesResource.getData().size());
                }
            }

            if (articlesResource.getData() != null) {
                Log.d(TAG, "Success - Total results: " + articlesResource.getTotalResults() + " Status code: " +
                        articlesResource.getStatusCode() + "Status message: " + articlesResource.getStatusMessage());

                for (int i = 0; i < articlesResource.getData().size(); i++) {
                    if (articlesResource.getData().get(i) != null) {
                        Log.d(TAG, "Article: " + articlesResource.getData().get(i).getTitle());
                    }
                }
            } else {
                Log.d(TAG, "Error - Status code: " + articlesResource.getStatusCode() + " Status message: " + articlesResource.getStatusMessage());
            }
        });
    }

    private List<Article> getArticleList() {

        Resource<List<Article>> articleListResource = newsViewModel.getArticlesResource().getValue();

        if (articleListResource != null) {
            return articleListResource.getData();
        }

        return null;
    }
}
