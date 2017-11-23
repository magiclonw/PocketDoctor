package com.magiclon.pocketdoctor.model;

import java.io.Serializable;

/**
 * Created by Admin on 2017/8/23 023.
 */

public class Hospital implements Serializable{
    String hospital;
    String info;
    String hid;
    String addr;

    public Hospital() {

    }

    public Hospital(String hospital, String info, String hid, String addr) {
        this.hospital = hospital;
        this.info = info;
        this.hid = hid;
        this.addr = addr;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "Hospital='" + hospital + '\'' +
                ", info='" + info + '\'' +
                ", hid='" + hid + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }
}
