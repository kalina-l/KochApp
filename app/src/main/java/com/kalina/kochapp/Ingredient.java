package com.kalina.kochapp;

/**
 * Created by Kalina on 10.01.2017.
 */

public class Ingredient {

    public double quantity;
    public String metric;
    public String name;

    public Ingredient(){
        this.quantity = 0;
        this.metric = "";
        this.name = "";
    }

    public Ingredient(double quantity, String metric, String name){
        this.quantity = quantity;
        this.metric = metric;
        this.name = name;
    }
}
