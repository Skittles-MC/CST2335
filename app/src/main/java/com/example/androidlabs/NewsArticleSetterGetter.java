package com.example.androidlabs;



/**********************************************************************
 Filename: MainNewsPage.java
 Version: 1.5
 Authors:	Martin Choy
 Student No:	040835431
 Course Name/Number:	CST2335 Mobile Graphical Interface Programming
 Lab Sect:	013
 Assignment #: Final Project - 1
 Assignment name:  Final_GroupProject F19
 Due Date: Dec 4th 2019 , 11:59PM midnight
 Submission Date: Dec 4th 2019
 Professor: Shahzeb Khowaja
 *********************************************************************/

/**
 * Class is just used as a Getter and Setter methods
 */
public class NewsArticleSetterGetter {

    private long id;
    private String name , author, title, description, url, urlToImage, publishedAt, content;

    /**
     * Overloaded constructor to set id, title, author
     * @param id
     * @param title
     * @param author
     */
    NewsArticleSetterGetter (long id, String title, String author) {
        setId(id);
        setTitle(title);
        setDescription(author);

    }

    /**
     * Overloaded constructor to set the following parameters
     * @param name
     * @param author
     * @param title
     * @param description
     * @param url
     * @param urlToImage
     * @param publishedAt
     * @param content
     */
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
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
