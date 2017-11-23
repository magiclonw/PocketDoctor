package com.magiclon.pocketdoctor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.magiclon.pocketdoctor.model.City;
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
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_NOTIFY + " where name like '%" + edt + "%'", null);
        List<Notify> result = new ArrayList<>();
        Notify notify;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(NAME));
            String type = cursor.getString(cursor.getColumnIndex("type"));
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
    public List<Doctor> getAllDoctor() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_DOCTOR, null);
        List<Doctor> result = new ArrayList<>();
        Doctor doctor;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(NAME));
            String level = cursor.getString(cursor.getColumnIndex("level"));
            String hospital = cursor.getString(cursor.getColumnIndex("hospital"));
            String department = cursor.getString(cursor.getColumnIndex("department"));
            String info = cursor.getString(cursor.getColumnIndex("info"));
            String shanchang = cursor.getString(cursor.getColumnIndex("shanchang"));
            doctor = new Doctor(name, level, hospital, department, info, shanchang);
            result.add(doctor);
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
    public List<Hospital> getAllHospital() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_HOSPITAL, null);
        List<Hospital> result = new ArrayList<>();
        Hospital hospital;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("hospital"));
            String info = cursor.getString(cursor.getColumnIndex("info"));
            String hid = cursor.getString(cursor.getColumnIndex("hid"));
            String addr = cursor.getString(cursor.getColumnIndex("addr"));
            hospital = new Hospital(name, info, hid, addr);
            result.add(hospital);
        }
        cursor.close();
        db.close();
        return result;
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
