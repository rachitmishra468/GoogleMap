package com.media_mosaic.httpwww.doubloons.Data_Model;

/**
 * Created by akash on 12/30/2017.
 */

public class State_model {

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    String state_name;
    public String toString()
    {
        return state_name;
    }
}
