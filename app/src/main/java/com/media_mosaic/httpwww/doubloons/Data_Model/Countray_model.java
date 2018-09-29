package com.media_mosaic.httpwww.doubloons.Data_Model;

/**
 * Created by Rachit on 12/30/2017.
 */

public class Countray_model {
    String id;
    String country_name;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }


    public String toString()
    {
        return country_name;
    }
}
