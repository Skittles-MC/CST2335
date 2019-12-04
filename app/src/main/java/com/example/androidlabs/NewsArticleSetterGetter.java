package com.example.androidlabs;



public class NewsArticleSetterGetter {

    private long id;
    private String name , author, title, description, url, urlToImage, publishedAt, content;

    // private String content;
    //not using content maybe

    //WALLAH USED FOR MainNewsPage
    /**
     * no-arg constructor
     *
     * overloaded constructor to set id, title and author
     * NewsArticleSetterGetter (long id, String title, String author) {
     *
     * overloaded constructor to set name, author, title, description, url, url to image, publisher, and content
     *  NewsArticleSetterGetter (String name, String author, String title, String description, String url, String urlToImage, String publishedAt, String content) {
     */
    // News (){}


    NewsArticleSetterGetter (long id, String title, String author) {
        setId(id);
        setTitle(title);
        setDescription(author);

    }

    NewsArticleSetterGetter (String name, String author, String title, String description, String url, String urlToImage, String publishedAt, String content) {

        setName(name);
        setAuthor(author);
        setTitle(title);
        setDescription(description);
        setUrl(url);
        setUrlToImage(urlToImage);
        setPublishedAt(publishedAt);
        setContent(content);
    }



    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setAuthor(String author) {
        this.author = author;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }


    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }


    public void setContent(String content) {
        this.content = content;
    }

}
