package com.example.android.project1;

public class News {
    private String newsId;
    private String date;
    private String title;
    private String text;
    private String image;
    private String likesCount;
    private String dislikesCount;

    public News() {}

    public News(String date, String titleNews, String textNews, String imageNews) {
        this.date = date;
        this.title = titleNews;
        this.text = textNews;
        this.image = imageNews;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public String getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(String dislikesCount) {
        this.dislikesCount = dislikesCount;
    }
}
