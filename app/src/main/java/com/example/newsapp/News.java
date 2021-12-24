package com.example.newsapp;

//To store news data
public class News {

    private String mSectionName;
    private String mAuthor = "";
    private String mDateAndTimePublished;
    private String mHeadline;
    private static final String NO_AUTHOR_PROVIDED = "";
    private String mUrl;


    /**
     * @param sectionName                   section of the article that is belongs to e.g world news
     * @param author                        author of the article
     * @param dateAndTimePublishedPublished published date and time of the article
     * @param headline                      headline of the article or main story of the article
     */
    public News(String sectionName, String author, String dateAndTimePublishedPublished, String headline, String url) {
        mSectionName = sectionName;
        mAuthor = author;
        mDateAndTimePublished = dateAndTimePublishedPublished;
        mHeadline = headline;
        mUrl = url;
    }

    /**
     * @param sectionName                   section of the article that is belongs to e.g world news
     * @param dateAndTimePublishedPublished published date and time of the article
     * @param headline                      headline of the article or main story of the article
     */
    //constructor when there is no author for the article mentioned.
    public News(String sectionName, String dateAndTimePublishedPublished, String headline, String url) {
        mSectionName = sectionName;
        mDateAndTimePublished = dateAndTimePublishedPublished;
        mHeadline = headline;
        mUrl = url;
    }

    /**
     * @return section of the article that is belongs to e.g world news
     */
    public String getSectionName() {
        return mSectionName;
    }

    /**
     * @return author of the article
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * @return dateAndTimePublishedPublished published date and time of the article
     */
    public String getDateAndTimePublishedPublished() {
        return mDateAndTimePublished;
    }

    /**
     * @return headline headline of the article or main story of the article
     */
    public String getHeadline() {
        return mHeadline;
    }

    public boolean hasAuthor() {
        return mAuthor != NO_AUTHOR_PROVIDED;
    }

    public String getUrl() {
        return mUrl;
    }
}
