package com.magiclon.pocketdoctor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.magiclon.pocketdoctor.model.City;
import com.magiclon.pocketdoctor.model.Department;
import com.magiclon.pocketdoctor.model.DeptDocHosListBean;
import com.magiclon.pocketdoctor.model.Doctor;
import com.magiclon.pocketdoctor.model.Hospital;
import com.magiclon.pocketdoctor.model.Notify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by baisoo on 16/9/24.
 */
public class DBManager {
    private static final String ASSETS_NAME = "china.db";
    private static final String DB_NAME = "china.db";
    private static final String TABLE_NAME_CITY = "city";
    private static final String TABLE_NAME_HISTORY = "history";
    private static final String TABLE_NAME_NOTIFY = "notify";
    private static final String TABLE_NAME_DOCTOR = "doctor";
    private static final String TABLE_NAME_HOSPITAL = "hospital";
    private static final String TABLE_NAME_DEPARTMENT = "department";
    private static final String NAME = "name";
    private static final String PINYIN = "pinyin";
    private static final int BUFFER_SIZE = 1024;
    private String DB_PATH;
    private Context mContext;


    public DBManager(Context context) {
        this.mContext = context;
        DB_PATH = File.separator + "data"
                + Environment.getDataDirectory().getAbsolutePath() + File.separator
                + context.getPackageName() + File.separator + "databases" + File.separator;
//        Log.e("******",DB_PATH);
//        DB_PATH = File.separator + "data" + Environment.getDataDirectory().getAbsolutePath() + File.separator
//                + "com.magiclon.testdroidplugin" + File.separator + "Plugin" + File.separator + context.getPackageName()
//                + File.separator + "databases" + File.separator;//插件包时用
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void copyDBFile() {
        File dir = new File(DB_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            InputStream is;
            OutputStream os;
            try {
                is = mContext.getResources().getAssets().open(ASSETS_NAME);
                os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = is.read(buffer, 0, buffer.length)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取所有城市
     *
     * @return
     */
    public List<City> getAllCities() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_CITY, null);
        List<City> result = new ArrayList<>();
        City city;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(NAME));
            String pinyin = cursor.getString(cursor.getColumnIndex(PINYIN));
            city = new City(name, pinyin);
            result.add(city);
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }

    /**
     * 读取所有历史纪录
     *
     * @return
     */
    public List<String> getAllHistory() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_HISTORY, null);
        List<String> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(NAME));
            result.add(name);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 读取所有提示
     *
     * @return
     */
    public List<Notify> getAllNotify(String edt) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("%");
        for (int i = 0; i <edt.length() ; i++) {
            stringBuilder.append(edt.charAt(i)+"%");
        }
//        Log.e("*****",stringBuilder.toString());
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select name,type from " + TABLE_NAME_NOTIFY + " where keyword like '" + stringBuilder.toString() + "'", null);
        List<Notify> result = new ArrayList<>();
        Notify notify;
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String type = cursor.getString(1);
            notify = new Notify(name, type);
            result.add(notify);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 读取所有医生
     *
     * @return
     */
    public List<Doctor> getAllDoctor(String dname) {
        String sqlwhere = "";
        if (!"".equals(dname)) {
            sqlwhere = " where name='" + dname + "'";
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_DOCTOR + sqlwhere, null);
        List<Doctor> result = new ArrayList<>();
        Doctor doctor;
        while (cursor.moveToNext()) {
            String docid=cursor.getString(0);
            String name = cursor.getString(1);
            String level = cursor.getString(2);
            String hospital = cursor.getString(3);
            String department = cursor.getString(4);
            String info = cursor.getString(5);
            String time = cursor.getString(6);
            doctor = new Doctor(docid,name, level, hospital, department, info, time);
            result.add(doctor);
        }
        cursor.close();
        db.close();
        return result;
    }
    /**
     * 读取所有医生根据名字和出诊时间
     *
     * @return
     */
    public List<Doctor> getAllDoctor(String dname,String sqltime) {
        String sqlwhere = "";
        if (!"".equals(dname)) {
            sqlwhere = " where docid='" + dname + "' and time like '"+sqltime+"'";
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_DOCTOR + sqlwhere, null);
        List<Doctor> result = new ArrayList<>();
        Doctor doctor;
        while (cursor.moveToNext()) {
            String docid=cursor.getString(0);
            String name = cursor.getString(1);
            String level = cursor.getString(2);
            String hospital = cursor.getString(3);
            String department = cursor.getString(4);
            String info = cursor.getString(5);
            String time = cursor.getString(6);
            doctor = new Doctor(docid,name, level, hospital, department, info, time);
//            Log.e("---",doctor.toString());
            result.add(doctor);
        }
        cursor.close();
        db.close();
        return result;
    }
    /**
     * 读取科室下所有医生
     *
     * @return
     */
    public List<Doctor> getAllDoctorForDept(String hname,String dname) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_DOCTOR + " where hospital='"+hname+"' and department='"+dname+"'", null);
        List<Doctor> result = new ArrayList<>();
        Doctor doctor;
        while (cursor.moveToNext()) {
            String docid=cursor.getString(0);
            String name = cursor.getString(1);
            String level = cursor.getString(2);
            String hospital = cursor.getString(3);
            String department = cursor.getString(4);
            String info = cursor.getString(5);
            String time = cursor.getString(6);
            doctor = new Doctor(docid,name, level, hospital, department, info, time);
            result.add(doctor);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 读取所有医院
     *
     * @return
     */
    public List<Hospital> getAllHospital(String name) {
        String sqlwhere = "";
        if (!"".equals(name)) {
            sqlwhere = " where hname='" + name + "'";
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_HOSPITAL + sqlwhere, null);
        List<Hospital> result = new ArrayList<>();
        Hospital hospital;
        while (cursor.moveToNext()) {
            String hid = cursor.getString(0);
            String hname = cursor.getString(1);
            String info = cursor.getString(2);
            String detail = cursor.getString(3);
            String department = cursor.getString(4);
            String machine = cursor.getString(5);
            String guide = cursor.getString(6);
            hospital = new Hospital(hid, hname, info, detail, department, machine, guide);
            result.add(hospital);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 读取某科室信息
     *
     * @return
     */
    public List<Department> getAllDept(String hospitalname, String dname) {
        String sqlwhere = "";
        if (!"".equals(hospitalname)) {
            sqlwhere = " where hname='" + hospitalname + "' and deptname='" + dname + "'";
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_DEPARTMENT + sqlwhere, null);
        List<Department> result = new ArrayList<>();
        Department department;
        while (cursor.moveToNext()) {
            String deptid = cursor.getString(0);
            String deptname = cursor.getString(1);
            String hid = cursor.getString(2);
            String hname = cursor.getString(3);
            String deptinfo = cursor.getString(4);
            department = new Department(deptid, deptname, hid, hname, deptinfo);
            result.add(department);
        }
        cursor.close();
        db.close();
        return result;
    }
    /**
     * 读取某医院下科室列表
     *
     * @return
     */
    public List<Department> getAllHospitalForDept(String hid) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_DEPARTMENT + " where hid='"+hid+"'", null);
        List<Department> result = new ArrayList<>();
        Department department;
        while (cursor.moveToNext()) {
            String deptid = cursor.getString(0);
            String deptname = cursor.getString(1);
//            String hid = cursor.getString(2);
            String hname = cursor.getString(3);
            String deptinfo = cursor.getString(4);
            department = new Department(deptid, deptname, hid, hname, deptinfo);
            result.add(department);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 读取所有医生，科室，医院
     *
     * @return
     */
    public DeptDocHosListBean getAllInfo(String edt) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("%");
        for (int i = 0; i <edt.length() ; i++) {
            stringBuilder.append(edt.charAt(i)+"%");
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select name,type from " + TABLE_NAME_NOTIFY + " where keyword like '%" + stringBuilder.toString() + "%'", null);
        List<Doctor> doctors = new ArrayList<>();
        List<Department> departments = new ArrayList<>();
        List<Hospital> hospitals = new ArrayList<>();
        DeptDocHosListBean deptdochoslistbean;
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String type = cursor.getString(1);
            if (type.equals("医生")) {
                doctors.addAll(getAllDoctor(name));
            } else if (type.equals("医院")) {
                hospitals.addAll(getAllHospital(name));
            } else if (type.equals("科室")) {
                String sname[]=name.split("-");
                departments.addAll(getAllDept(sname[0],sname[1]));
            }
        }
        deptdochoslistbean = new DeptDocHosListBean(doctors, departments, hospitals);
        cursor.close();
        db.close();
        return deptdochoslistbean;
    }

    public void insertHistory(String name) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        db.execSQL("insert into " + TABLE_NAME_HISTORY + "(name) select '" + name + "' where not exists(select * from " + TABLE_NAME_HISTORY + " where name='" + name + "')");
        db.close();
    }

    public void deleteOneHistory(String name) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        db.delete(TABLE_NAME_HISTORY, "name=?", new String[]{name});
        db.close();
    }

    public void deleteAllHistory() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        db.delete(TABLE_NAME_HISTORY, null, null);
        db.close();
    }


    /**
     * a-z排序
     */
    private class CityComparator implements Comparator<City> {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            return a.compareTo(b);
        }
    }
}
