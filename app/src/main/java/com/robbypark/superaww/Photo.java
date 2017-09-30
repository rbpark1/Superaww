package com.robbypark.superaww;

/**
 * Created by Robby on 9/30/2017.
 */

public class Photo {

    private String mTitle;
    private int mScore;
    private String mPermalink;
    private String mUrl;

    public Photo(String title, int score, String permalink, String url) {
        mTitle = title;
        mScore = score;
        mPermalink = permalink;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public String getPermalink() {
        return mPermalink;
    }

    public void setPermalink(String permalink) {
        mPermalink = permalink;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
