package com.media_mosaic.httpwww.doubloons.Data_Model;

/**
 * Created by Rachit on 2/15/2018.
 */

public class Invite_Model {
    String name;
    String password;
    String invitation_mode;

    public String getInvitation_mode() {
        return invitation_mode;
    }

    public void setInvitation_mode(String invitation_mode) {
        this.invitation_mode = invitation_mode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Invite_Model(String name, String password,String invitation_mode) {
        this.name = name;
        this.password=password;
        this.invitation_mode=invitation_mode;

    }
}
