package com.ehedgehog.android.topstories.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.io.Serializable;

public class Multimedia extends RealmObject {

    @SerializedName("url")
    private String mUrl;
    @SerializedName("format")
    private String mFormat;
    @SerializedName("caption")
    private String mCaption;
    @SerializedName("width")
    private int mWidth;
    @SerializedName("height")
    private int mHeight;

    public Multimedia() {
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getFormat() {
        return mFormat;
    }

    public void setFormat(String format) {
        mFormat = format;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }
}
