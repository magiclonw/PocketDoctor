package com.magiclon.pocketdoctor.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admin on 2017/8/23 023.
 */

public class Doctor implements Parcelable{
    String docid;
    String name;
    String level;
    String hospital;
    String department;
    String info;
    String time;

    public Doctor() {
    }

    public Doctor(String docid, String name, String level, String hospital, String department, String info, String time) {
        this.docid = docid;
        this.name = name;
        this.level = level;
        this.hospital = hospital;
        this.department = department;
        this.info = info;
        this.time = time;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "docid='" + docid + '\'' +
                ", name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", hospital='" + hospital + '\'' +
                ", department='" + department + '\'' +
                ", info='" + info + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.docid);
        dest.writeString(this.name);
        dest.writeString(this.level);
        dest.writeString(this.hospital);
        dest.writeString(this.department);
        dest.writeString(this.info);
        dest.writeString(this.time);
    }

    protected Doctor(Parcel in) {
        this.docid = in.readString();
        this.name = in.readString();
        this.level = in.readString();
        this.hospital = in.readString();
        this.department = in.readString();
        this.info = in.readString();
        this.time = in.readString();
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel source) {
            return new Doctor(source);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };
}
