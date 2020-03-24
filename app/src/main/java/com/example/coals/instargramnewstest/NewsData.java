package com.example.coals.instargramnewstest;

import java.io.Serializable;

/**
 * Created by KPlo on 2018. 11. 3..
 */
//뉴스 데이터를 한번에 넘기기위한 serializable (직렬화)
public class NewsData implements Serializable {
    private String title;
    private String urlToImage;
    private String description;
    private String url;
//    private String content;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

//    public String getContent() {
//        return content;
//    }

//    public void setContent(String content) {
//        this.content = content;
//    }
}
