package com.magiclon.pocketdoctor.model;

import java.util.List;

/**
 * 作者：MagicLon
 * 时间：2018/3/2 002
 * 邮箱：1348149485@qq.com
 * 描述：三种集合放一起
 */

public class DeptDocHosListBean {
    List<Doctor> doctorList;
    List<Department> departmentList;
    List<Hospital> hospitalList;

    public DeptDocHosListBean(List<Doctor> doctorList, List<Department> departmentList, List<Hospital> hospitalList) {
        this.doctorList = doctorList;
        this.departmentList = departmentList;
        this.hospitalList = hospitalList;
    }

    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public List<Hospital> getHospitalList() {
        return hospitalList;
    }

    public void setHospitalList(List<Hospital> hospitalList) {
        this.hospitalList = hospitalList;
    }

    @Override
    public String toString() {
        return "DeptDocHosListBean{" +
                "doctorList=" + doctorList +
                ", departmentList=" + departmentList +
                ", hospitalList=" + hospitalList +
                '}';
    }
}
