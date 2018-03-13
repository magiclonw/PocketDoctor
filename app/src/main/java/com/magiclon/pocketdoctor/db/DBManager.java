package com.magiclon.pocketdoctor.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.magiclon.pocketdoctor.model.City;
import com.magiclon.pocketdoctor.model.Department;
import com.magiclon.pocketdoctor.model.DeptDocHosListBean;
import com.magiclon.pocketdoctor.model.Doctor;
import com.magiclon.pocketdoctor.model.Hospital;
import com.magiclon.pocketdoctor.model.Notify;

//import net.sqlcipher.Cursor;
//import net.sqlcipher.database.SQLiteDatabase;


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
//        else {
//            net.sqlcipher.database.SQLiteDatabase.loadLibs(mContext);
//            encrypt(mContext,"china.db","ljy2018");
//        }
    }

//    public static void encrypt(Context ctxt, String dbName,
//                               String passphrase) {
//        try {
//
//
//            File originalFile = ctxt.getDatabasePath(dbName);
//
//            if (originalFile.exists()) {
//                File newFile =
//                        File.createTempFile("sqlcipherutils", "tmp",
//                                ctxt.getCacheDir());
//                net.sqlcipher.database.SQLiteDatabase db =
//                        net.sqlcipher.database.SQLiteDatabase.openDatabase(originalFile.getAbsolutePath(),
//                                "", null,
//                                net.sqlcipher.database.SQLiteDatabase.OPEN_READWRITE);
//
//                db.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s';",
//                        newFile.getAbsolutePath(), passphrase));
//                db.rawExecSQL("SELECT sqlcipher_export('encrypted')");
//                db.rawExecSQL("DETACH DATABASE encrypted;");
//
//                int version = db.getVersion();
//
//                db.close();
//
//                db =
//                        net.sqlcipher.database.SQLiteDatabase.openDatabase(newFile.getAbsolutePath(),
//                                passphrase, null,
//                                net.sqlcipher.database.SQLiteDatabase.OPEN_READWRITE);
//                db.setVersion(version);
//                db.close();
//
////                originalFile.delete();
////                newFile.renameTo(originalFile);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("%");
        for (int i = 0; i < edt.length(); i++) {
            stringBuilder.append(edt.charAt(i) + "%");
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select notifyid,name,type from " + TABLE_NAME_NOTIFY + " where keyword like '" + stringBuilder.toString() + "'", null);
        List<Notify> result = new ArrayList<>();
        Notify notify;
        while (cursor.moveToNext()) {
            String notifyid = cursor.getString(0);
            String name = cursor.getString(1);
            String type = cursor.getString(2);
            notify = new Notify(notifyid, name, type);
            result.add(notify);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 读取某医生信息
     *
     * @return
     */
    public List<Doctor> getAllDoctor(String id) {
        String sqlwhere = "";
        if (!"".equals(id)) {
            sqlwhere = " where docid='" + id + "'";
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select docid,name,level,hname,deptname,info,time from " + TABLE_NAME_DOCTOR + sqlwhere, null);
        List<Doctor> result = new ArrayList<>();
        Doctor doctor;
        while (cursor.moveToNext()) {
            String docid = cursor.getString(0);
            String name = cursor.getString(1);
            String level = cursor.getString(2);
            String hospital = cursor.getString(3);
            String department = cursor.getString(4);
            String info = cursor.getString(5);
            String time = cursor.getString(6);
            doctor = new Doctor(docid, name, level, hospital, department, info, time);
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
    public List<Doctor> getAllDoctorForDept(String deptid) {
//        Log.e("***",hname+"****"+dname);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select docid,name,level,hname,deptname,info,time from " + TABLE_NAME_DOCTOR + " where deptid='" + deptid + "'", null);
        List<Doctor> result = new ArrayList<>();
        Doctor doctor;
        while (cursor.moveToNext()) {
            String docid = cursor.getString(0);
            String name = cursor.getString(1);
            String level = cursor.getString(2);
            String hospital = cursor.getString(3);
            String department = cursor.getString(4);
            String info = cursor.getString(5);
            String time = cursor.getString(6);
            doctor = new Doctor(docid, name, level, hospital, department, info, time);
            result.add(doctor);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 读取某医院信息
     *
     * @return
     */
    public List<Hospital> getAllHospital(String hid) {
        String sqlwhere = "";
        if (!"".equals(hid)) {
            sqlwhere = " where hid='" + hid + "'";
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_HOSPITAL + sqlwhere, null);
        List<Hospital> result = new ArrayList<>();
        Hospital hospital;
        while (cursor.moveToNext()) {
            String hhid = cursor.getString(0);
            String hname = cursor.getString(1);
            String info = cursor.getString(2);
            String detail = cursor.getString(3);
            String department = cursor.getString(4);
            String machine = cursor.getString(5);
            String guide = cursor.getString(6);
            hospital = new Hospital(hhid, hname, info, detail, department, machine, guide);
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
    public List<Department> getAllDept(String deptid) {
        String sqlwhere = "";
        if (!"".equals(deptid)) {
            sqlwhere = " where deptid='" + deptid + "'";
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_DEPARTMENT + sqlwhere, null);
        List<Department> result = new ArrayList<>();
        Department department;
        while (cursor.moveToNext()) {
            String dptid = cursor.getString(0);
            String deptname = cursor.getString(1);
            String hid = cursor.getString(2);
            String hname = cursor.getString(3);
            String deptinfo = cursor.getString(4);
            department = new Department(dptid, deptname, hid, hname, deptinfo);
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
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_DEPARTMENT + " where hid='" + hid + "'", null);
        List<Department> result = new ArrayList<>();
        Department department;
        while (cursor.moveToNext()) {
            String deptid = cursor.getString(0);
            String deptname = cursor.getString(1);
            String hhid = cursor.getString(2);
            String hname = cursor.getString(3);
            String deptinfo = cursor.getString(4);
            department = new Department(deptid, deptname, hhid, hname, deptinfo);
            result.add(department);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 读取相关所有医生，科室，医院
     *
     * @return
     */
    public DeptDocHosListBean getAllInfo(String edt) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("%");
        for (int i = 0; i < edt.length(); i++) {
            stringBuilder.append(edt.charAt(i) + "%");
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);

        Cursor cursor_doctor = db.rawQuery("select a.docid,a.name,a.level,a.hname,a.deptname,a.info,a.time from " + TABLE_NAME_DOCTOR + " as a," + TABLE_NAME_NOTIFY + " as b where a.docid=b.notifyid AND b.keyword like '%" + stringBuilder.toString() + "%' LIMIT 4", null);
        List<Doctor> doctors = new ArrayList<>();
        while (cursor_doctor.moveToNext()) {
            String docid = cursor_doctor.getString(0);
            String name = cursor_doctor.getString(1);
            String level = cursor_doctor.getString(2);
            String hospital = cursor_doctor.getString(3);
            String department = cursor_doctor.getString(4);
            String info = cursor_doctor.getString(5);
            String time = cursor_doctor.getString(6);
            Doctor doctor = new Doctor(docid, name, level, hospital, department, info, time);
            doctors.add(doctor);
        }
        cursor_doctor.close();
        List<Department> departments = new ArrayList<>();
        Cursor cursor_dept = db.rawQuery("select a.deptid,a.deptname,a.hid,a.hname,a.deptinfo from " + TABLE_NAME_DEPARTMENT + " as a," + TABLE_NAME_NOTIFY + " as b where a.deptid=b.notifyid AND b.keyword like '%" + stringBuilder.toString() + "%' LIMIT 4", null);
        while (cursor_dept.moveToNext()) {
            String dptid = cursor_dept.getString(0);
            String deptname = cursor_dept.getString(1);
            String hid = cursor_dept.getString(2);
            String hname = cursor_dept.getString(3);
            String deptinfo = cursor_dept.getString(4);
            Department department = new Department(dptid, deptname, hid, hname, deptinfo);
            departments.add(department);
        }
        cursor_dept.close();
        List<Hospital> hospitals = new ArrayList<>();
        Cursor cursor_hospital = db.rawQuery("select a.hid,a.hname,a.info,a.detail,a.department,a.machine,a.guide from " + TABLE_NAME_HOSPITAL + " as a," + TABLE_NAME_NOTIFY + " as b where a.hid=b.notifyid and b.keyword like '%" + stringBuilder.toString() + "%' LIMIT 4", null);
        while (cursor_hospital.moveToNext()) {
            String hhid = cursor_hospital.getString(0);
            String hname = cursor_hospital.getString(1);
            String info = cursor_hospital.getString(2);
            String detail = cursor_hospital.getString(3);
            String department = cursor_hospital.getString(4);
            String machine = cursor_hospital.getString(5);
            String guide = cursor_hospital.getString(6);
            Hospital hospital = new Hospital(hhid, hname, info, detail, department, machine, guide);
            hospitals.add(hospital);
        }
        cursor_hospital.close();
        DeptDocHosListBean deptdochoslistbean = new DeptDocHosListBean(doctors, departments, hospitals);
        db.close();
        return deptdochoslistbean;
    }

    /**
     * 读取查询相关的医生
     *
     * @return
     */
    public List<Doctor> getSearchDoctor(String edt, String timelike) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("%");
        for (int i = 0; i < edt.length(); i++) {
            stringBuilder.append(edt.charAt(i) + "%");
        }
        String sqltime = "";
        if (!"".equals(timelike)) {
            sqltime = " and time like '" + timelike + "'";
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);

        Cursor cursor_doctor = db.rawQuery("select a.docid,a.name,a.level,a.hname,a.deptname,a.info,a.time from " + TABLE_NAME_DOCTOR + " as a," + TABLE_NAME_NOTIFY + " as b where a.docid=b.notifyid and b.type='医生' and b.keyword like '%" + stringBuilder.toString() + "%'" + sqltime, null);
        List<Doctor> doctors = new ArrayList<>();
        while (cursor_doctor.moveToNext()) {
            String docid = cursor_doctor.getString(0);
            String name = cursor_doctor.getString(1);
            String level = cursor_doctor.getString(2);
            String hospital = cursor_doctor.getString(3);
            String department = cursor_doctor.getString(4);
            String info = cursor_doctor.getString(5);
            String time = cursor_doctor.getString(6);
            Doctor doctor = new Doctor(docid, name, level, hospital, department, info, time);
            doctors.add(doctor);
        }
        cursor_doctor.close();
        db.close();
        return doctors;
    }

    /**
     * 读取相关科室
     *
     * @return
     */
    public List<Department> getSearchDept(String edt) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("%");
        for (int i = 0; i < edt.length(); i++) {
            stringBuilder.append(edt.charAt(i) + "%");
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        List<Department> departments = new ArrayList<>();
        Cursor cursor_dept = db.rawQuery("select a.deptid,a.deptname,a.hid,a.hname,a.deptinfo from " + TABLE_NAME_DEPARTMENT + " as a," + TABLE_NAME_NOTIFY + " as b where a.deptid=b.notifyid and b.type='科室' AND b.keyword like '%" + stringBuilder.toString() + "%'", null);
        while (cursor_dept.moveToNext()) {
            String dptid = cursor_dept.getString(0);
            String deptname = cursor_dept.getString(1);
            String hid = cursor_dept.getString(2);
            String hname = cursor_dept.getString(3);
            String deptinfo = cursor_dept.getString(4);
            Department department = new Department(dptid, deptname, hid, hname, deptinfo);
            departments.add(department);
        }
        cursor_dept.close();
        db.close();
        return departments;
    }

    /**
     * 读取相关医院
     *
     * @return
     */
    public List<Hospital> getSearchHospital(String edt) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("%");
        for (int i = 0; i < edt.length(); i++) {
            stringBuilder.append(edt.charAt(i) + "%");
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        List<Hospital> hospitals = new ArrayList<>();
        Cursor cursor_hospital = db.rawQuery("select a.hid,a.hname,a.info,a.detail,a.department,a.machine,a.guide from " + TABLE_NAME_HOSPITAL + " as a," + TABLE_NAME_NOTIFY + " as b where a.hid=b.notifyid and b.type='医院' and b.keyword like '%" + stringBuilder.toString() + "%'", null);
        while (cursor_hospital.moveToNext()) {
            String hhid = cursor_hospital.getString(0);
            String hname = cursor_hospital.getString(1);
            String info = cursor_hospital.getString(2);
            String detail = cursor_hospital.getString(3);
            String department = cursor_hospital.getString(4);
            String machine = cursor_hospital.getString(5);
            String guide = cursor_hospital.getString(6);
            Hospital hospital = new Hospital(hhid, hname, info, detail, department, machine, guide);
            hospitals.add(hospital);
        }
        cursor_hospital.close();
        db.close();
        return hospitals;
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
