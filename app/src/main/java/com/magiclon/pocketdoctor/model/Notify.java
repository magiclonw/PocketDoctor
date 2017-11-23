package com.magiclon.pocketdoctor.model;

/**
 * Created by Admin on 2017/8/23 023.
 */

public class Notify {
    String name;
    String type;

    public Notify(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Notify() {
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
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
