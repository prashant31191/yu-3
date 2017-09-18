package com.sen.api;

/**
 * Created by Adminsss on 08-02-2016.
 */

import com.youtube.apicall.YTrailerModel;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface IApiMethods {

    @GET("/get/curators.json")
    UsernameChannel getCurators(
            @Query("api_key") String key
    );


    @GET("/channels")
    void getUserChannelName
            (
                    @Query("part") String part,
                    @Query("forUsername") String forUsername,
                    @Query("key") String key,
                    @Query("order") String order,
                    @Query("maxResults") String maxResults, Callback<UsernameChannel> cb
            );


    @GET("/search")
    void getUserChannelVideos
            (
                    @Query("part") String part,
                    @Query("channelId") String channelId,
                    @Query("key") String key,
                    @Query("order") String order,
                    @Query("maxResults") String maxResults, Callback<UsernameChannelVideos> cb
            );




    //https://www.googleapis.com/youtube/v3/channels?
    // part=snippet&
    // forUsername=tseries&
    // key=AIzaSyDGDGk8ctTus1DCsWgldVeq2UaUwO7i4WM&
    // order=date&
    // maxResults=50




    @GET("/search")
    void getSeachVideos
            (
                    @Query("part") String part,
                    @Query("q") String q,
                    @Query("type") String type,
                    @Query("key") String key,

                    @Query("maxResults") String maxResults, Callback<UsernameChannelVideos> cb
            );

   /* https://www.googleapis.com/youtube/v3/
    search?
    part=id&
    q=android%20studio%20live%20tv%20lolipop&
    type=video&
    key=AIzaSyDGDGk8ctTus1DCsWgldVeq2UaUwO7i4WM&
    maxResults=20*/

    //https://www.googleapis.com/youtube/v3/search?part=id&q=android&type=video&key=AIzaSyDGDGk8ctTus1DCsWgldVeq2UaUwO7i4WM&maxResults=20



    //https://www.googleapis.com/youtube/v3/search?part=snippet&q=android&type=video&key=AIzaSyDGDGk8ctTus1DCsWgldVeq2UaUwO7i4WM&maxResults=20

    @GET("/search")
    void getSeachVideosTitle
            (
                    @Query("part") String part,
                    @Query("q") String q,
                    @Query("type") String type,
                    @Query("key") String key,

                    @Query("maxResults") String maxResults, Callback<SearchVideoTitle> cb
            );


    @GET("/search")
    void getSeachVideosTrailer
            (
                    @Query("part") String part,
                    @Query("q") String q,
                    @Query("type") String type,
                    @Query("key") String key,

                    @Query("maxResults") String maxResults, Callback<YTrailerModel> cb
            );

    @GET("/search")
    void getSeachVideosTrailerNextPage
            (
                    @Query("part") String part,
                    @Query("q") String q,
                    @Query("type") String type,
                    @Query("key") String key,
                    @Query("pageToken") String pageToken,
                    @Query("maxResults") String maxResults, Callback<YTrailerModel> cb
            );

}