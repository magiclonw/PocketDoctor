package com.magiclon.pocketdoctor.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Admin on 2017/8/23 023.
 */

public class Hospital implements Parcelable{
    String hid;
    String hname;
    String info;
    String detail;
    String department;
    String machine;
    String guide;

    public Hospital(String hid, String hname, String info, String detail, String department, String machine, String guide) {
        this.hid = hid;
        this.hname = hname;
        this.info = info;
        this.detail = detail;
        this.department = department;
        this.machine = machine;
        this.guide = guide;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hid);
        dest.writeString(this.hname);
        dest.writeString(this.info);
        dest.writeString(this.detail);
        dest.writeString(this.department);
        dest.writeString(this.machine);
        dest.writeString(this.guide);
    }

    protected Hospital(Parcel in) {
        this.hid = in.readString();
        this.hname = in.readString();
        this.info = in.readString();
        this.detail = in.readString();
        this.department = in.readString();
        this.machine = in.readString();
        this.guide = in.readString();
    }

    public static final Creator<Hospital> CREATOR = new Creator<Hospital>() {
        @Override
        public Hospital createFromParcel(Parcel source) {
            return new Hospital(source);
        }

        @Override
        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };
}
