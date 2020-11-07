package com.davydh.covid_19.model;

import org.jetbrains.annotations.NotNull;

/**
 * It represents the source of an Article.
 */
public class ArticleSource {

    private String id;
    private String name;

    public ArticleSource(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Override
    public String toString() {
        return "ArticleSource{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
