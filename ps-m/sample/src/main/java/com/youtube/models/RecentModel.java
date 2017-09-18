package com.youtube.models;

/**
 * Created by Admin on 6/30/2016.
 */

public class RecentModel {

    public String name;

    public static final String[] data = {"Cupcake", "Donut", "Eclair",
            "Froyo", "Gingerbread", "Honeycomb",
            "Icecream Sandwich", "Jelly Bean", "Kitkat", "Lollipop"};

    RecentModel(String name){
        this.name=name;
    }
}