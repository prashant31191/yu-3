package com.youtube.database.classes;

/**
 * Created by Admin on 7/2/2016.
 */

public class YouTubeModel
{


    private Long id;
    /** Not-null value. */
    private String strTitle;
    private String strDesc;
    private String strImage;
    private String strTab;
    private java.util.Date date;

    public YouTubeModel() {
    }

    public YouTubeModel(Long id) {
        this.id = id;
    }

    public YouTubeModel(Long id, String strTitle, String strDesc, java.util.Date date) {
        this.id = id;
        this.strTitle = strTitle;
        this.strDesc = strDesc;
        this.date = date;
    }


    public YouTubeModel(Long id, String strTitle, String strDesc, String strImage,String strTab) {
        this.id = id;
        this.strTitle = strTitle;
        this.strDesc = strDesc;
        this.strImage = strImage;
        this.strTab = strTab;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getStrTitle() {
        return strTitle;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getStrDesc() {
        return strDesc;
    }

    public void setStrDesc(String strDesc) {
        this.strDesc = strDesc;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getStrImage() {
        return strImage;
    }

    public void setStrImage(String strImage) {
        this.strImage = strImage;
    }

    public String getStrTab() {
        return strTab;
    }

    public void setStrTab(String strTab) {
        this.strTab = strTab;
    }

}
