package com.magiclon.pocketdoctor.tools

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

/**
 * @author MagicLon
 *  key->licensecode激活码
 *  key->username 用户名
 *  key->password 密码
 *  key->deviceid 设备号
 */
object SharePreferenceUtil {
    private var sp: SharedPreferences? = null
    private val SharePreferncesName = "SP_SETTING"

    /**
     * @param context
     * *
     * @param key
     * *
     * @param value
     * *
     * @return 是否保存成功
     */
    fun setValue(context: Context, key: String, value: Any): Boolean {
        if (sp == null) {
            sp = context.getSharedPreferences(SharePreferncesName, Context.MODE_PRIVATE)
        }
        val edit = sp!!.edit()
        if (value is String) {
            return edit.putString(key, value).commit()
        } else if (value is Boolean) {
            return edit.putBoolean(key, value).commit()
        } else if (value is Float) {
            return edit.putFloat(key, value).commit()
        } else if (value is Int) {
            return edit.putInt(key, value).commit()
        } else if (value is Long) {
            return edit.putLong(key, value).commit()
        } else if (value is Set<*>) {
            IllegalArgumentException("Value can not be Set object!")
            return false
        }
        return false
    }

    fun getBoolean(context: Context, key: String): Boolean {
        if (sp == null) {
            sp = context.getSharedPreferences(SharePreferncesName, Context.MODE_PRIVATE)
        }
        return sp!!.getBoolean(key, false)
    }

    fun getString(context: Context, key: String): String {
        if (sp == null) {
            sp = context.getSharedPreferences(SharePreferncesName, Context.MODE_PRIVATE)
        }
        return sp!!.getString(key, "")
    }

    fun getFloat(context: Context, key: String): Float {
        if (sp == null) {
            sp = context.getSharedPreferences(SharePreferncesName, Context.MODE_PRIVATE)
        }
        return sp!!.getFloat(key, 0f)
    }

    fun getInt(context: Context, key: String): Int {
        if (sp == null) {
            sp = context.getSharedPreferences(SharePreferncesName, Context.MODE_PRIVATE)
        }
        return sp!!.getInt(key, 0)
    }

    fun getLong(context: Context, key: String): Long {
        if (sp == null) {
            sp = context.getSharedPreferences(SharePreferncesName, Context.MODE_PRIVATE)
        }
        return sp!!.getLong(key, 0)
    }

    fun deletShare() {
        sp!!.edit().clear().commit()
    }

}
