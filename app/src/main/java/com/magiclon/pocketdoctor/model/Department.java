package com.magiclon.pocketdoctor.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：MagicLon
 * 时间：2018/3/2 002
 * 邮箱：1348149485@qq.com
 * 描述：科室
 */

public class Department implements Parcelable {
    String deptid;
    String deptname;
    String hid;
    String hname;
    String deptinfo;

    public Department(String deptid, String deptname, String hid, String hname, String deptinfo) {
        this.deptid = deptid;
        this.deptname = deptname;
        this.hid = hid;
        this.hname = hname;
        this.deptinfo = deptinfo;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
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

    public String getDeptinfo() {
        return deptinfo;
    }

    public void setDeptinfo(String deptinfo) {
        this.deptinfo = deptinfo;
    }

    @Override
    public String toString() {
        return "Department{" +
                "deptid='" + deptid + '\'' +
                ", deptname='" + deptname + '\'' +
                ", hid='" + hid + '\'' +
                ", hname='" + hname + '\'' +
                ", deptinfo='" + deptinfo + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deptid);
        dest.writeString(this.deptname);
        dest.writeString(this.hid);
        dest.writeString(this.hname);
        dest.writeString(this.deptinfo);
    }

    protected Department(Parcel in) {
        this.deptid = in.readString();
        this.deptname = in.readString();
        this.hid = in.readString();
        this.hname = in.readString();
        this.deptinfo = in.readString();
    }

    public static final Creator<Department> CREATOR = new Creator<Department>() {
        @Override
        public Department createFromParcel(Parcel source) {
            return new Department(source);
        }

        @Override
        public Department[] newArray(int size) {
            return new Department[size];
        }
    };
}
