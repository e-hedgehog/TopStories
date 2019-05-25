package com.ehedgehog.android.topstories.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Article extends RealmObject implements Serializable {

    @SerializedName("title")
    private String mTitle;
    @SerializedName("abstract")
    private String mAbstract;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("byline")
    private String mByline;
    @SerializedName("published_date")
    private String mPublishedDate;
    @SerializedName("multimedia")
    private RealmList<Multimedia> mMultimedia;

    public Article() {
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAbstract() {
        return mAbstract;
    }

    public void setAbstract(String anAbstract) {
        mAbstract = anAbstract;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getByline() {
        return mByline;
    }

    public void setByline(String byline) {
        mByline = byline;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        mPublishedDate = publishedDate;
    }

    public List<Multimedia> getMultimedia() {
        return mMultimedia;
    }

    public void setMultimedia(List<Multimedia> multimedia) {
        mMultimedia = (RealmList<Multimedia>) multimedia;
    }
}
