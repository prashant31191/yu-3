package com.videbut01.network.model;

/**
 * Created by prashant.patel on 9/22/2017.
 */

public class HomeListModel {
    public String productid;
    public String productimg;
    public String productName;
    public String compamyName;
    public String rating;
    public String price;

    public HomeListModel(String s1,String s2,String s3,String s4,String s5)
    {

    }

    public HomeListModel(String s1,String s2,String s3,String s4,String s5,String s6)
    {
        productid = s1;
        productimg = s2;
        productName = s3;
        compamyName = s4;
        rating = s5;
        price = s6;
    }
}
