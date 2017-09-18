package com.sen.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Adminsss on 08-02-2016.
 */

public class UsernameChannel  {


    @SerializedName("kind")
    String kind;

    @SerializedName("items")
    ArrayList<Items> arrayListItems = new ArrayList<>();


     public class Items
     {
         @SerializedName("id")
         String id; //channel id

         public String getId() {
             return id;
         }

         public void setId(String id) {
             this.id = id;
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