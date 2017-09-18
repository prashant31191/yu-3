package com.sen.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Adminsss on 08-02-2016.
 */ public class SearchVideoTitle {
    @SerializedName("kind")
    String kind;

    @SerializedName("items")
    ArrayList<Items> arrayListItems = new ArrayList<>();



    @SerializedName("pageInfo")
public PageInfo pageInfo;

    public class PageInfo {
        @SerializedName("totalResults")
        String totalResults; //channel id

        @SerializedName("resultsPerPage")
        String resultsPerPage; //channel id

        public String getResultsPerPage() {
            return resultsPerPage;
        }

        public void setResultsPerPage(String resultsPerPage) {
            this.resultsPerPage = resultsPerPage;
        }

        public String getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(String totalResults) {
            this.totalResults = totalResults;
        }
    }




     public class Items
     {
/*
         @SerializedName("snippet)
                 Snippet snippet; //channel id

                 public class Snippet{
                 @SerializedName("title")
                 String title; //channel id
                 }*/


         @SerializedName("id")
         Id id; //channel id

         public class Id
         {
             @SerializedName("videoId")
             String videoId; //channel id

             public String getVideoId() {
                 return videoId;
             }

             public void setVideoId(String videoId) {
                 this.videoId = videoId;
             }
         }

         public Id getId() {
             return id;
         }

         public void setId(Id id) {
             this.id = id;
         }







         @SerializedName("snippet")
         Snippet snippet; //channel id

         public class Snippet
         {
             @SerializedName("title")
             String videoTitle; //channel id

             public String getVideoTitle() {
                 return videoTitle;
             }

             public void setVideoTitle(String videoTitle) {
                 this.videoTitle = videoTitle;
             }

             @SerializedName("description")
             String videoDescription; //channel id

             public String getVideoDescription() {
                 return videoDescription;
             }

             public void setVideoDescription(String videoDescription) {
                 this.videoDescription = videoDescription;
             }
         }

         public Snippet getSnippet() {
             return snippet;
         }

         public void setSnippet(Snippet snippet) {
             this.snippet = snippet;
         }








     }

    public ArrayList<Items> getArrayListItems() {
        return arrayListItems;
    }

    public void setArrayListItems(ArrayList<Items> arrayListItems) {
        this.arrayListItems = arrayListItems;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}