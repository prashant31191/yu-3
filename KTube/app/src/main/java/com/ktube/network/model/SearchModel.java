package com.ktube.network.model;

import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;

/**
 * Created by Admin on 6/30/2016.
 */
public class SearchModel {
    @SerializedName("kind")
    String kind;

    @SerializedName("nextPageToken")
    String nextPageToken;


    @SerializedName("prevPageToken")
    String prevPageToken;

    @SerializedName("etag")
    String etag;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

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


    public class Items {
        @SerializedName("id")
        Id id; //channel id

        public class Id {
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

        public class Snippet {
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

