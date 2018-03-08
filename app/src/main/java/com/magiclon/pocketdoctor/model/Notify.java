package com.magiclon.pocketdoctor.model;

/**
 * Created by Admin on 2017/8/23 023.
 */

public class Notify {
    String notifyid;
    String name;
    String type;

    public Notify(String notifyid, String name, String type) {
        this.notifyid = notifyid;
        this.name = name;
        this.type = type;
    }

    public String getNotifyid() {
        return notifyid;
    }

    public void setNotifyid(String notifyid) {
        this.notifyid = notifyid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Notify{" +
                "notifyid='" + notifyid + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
