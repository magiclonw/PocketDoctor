package com.magiclon.pocketdoctor.model;

import java.io.Serializable;

/**
 * 作者：MagicLon
 * 时间：2018/3/2 002
 * 邮箱：1348149485@qq.com
 * 描述：科室
 */

public class Department implements Serializable {
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
}
