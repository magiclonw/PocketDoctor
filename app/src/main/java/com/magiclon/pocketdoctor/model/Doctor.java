package com.magiclon.pocketdoctor.model;

import java.io.Serializable;

/**
 * Created by Admin on 2017/8/23 023.
 */

public class Doctor implements Serializable{
    String name;
    String level;
    String hospital;
    String department;
    String info;
    String time;

    public Doctor() {
    }

    public Doctor(String name, String level, String hospital, String department, String info, String time) {
        this.name = name;
        this.level = level;
        this.hospital = hospital;
        this.department = department;
        this.info = info;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getShanchang() {
        return time;
    }

    public void setShanchang(String shanchang) {
        this.time = shanchang;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", hospital='" + hospital + '\'' +
                ", department='" + department + '\'' +
                ", info='" + info + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
