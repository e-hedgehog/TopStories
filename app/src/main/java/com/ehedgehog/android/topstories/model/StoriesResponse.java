package com.ehedgehog.android.topstories.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StoriesResponse implements Serializable {

    @SerializedName("status")
    private String mStatus;
    @SerializedName("section")
    private String mSection;
    @SerializedName("num_results")
    private int mTotalResults;
    @SerializedName("results")
    private List<Article> mArticles;

    public StoriesResponse() {
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(int totalResults) {
        mTotalResults = totalResults;
    }

    public List<Article> getArticles() {
        return mArticles;
    }

    public void setArticles(List<Article> articles) {
        mArticles = articles;
    }

    public String getSection() {
        return mSection;
    }

    public void setSection(String section) {
        mSection = section;
    }
}
