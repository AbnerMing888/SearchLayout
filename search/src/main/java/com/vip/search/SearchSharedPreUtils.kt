package com.vip.search

import android.content.Context
import android.content.SharedPreferences


/**
 *AUTHOR:AbnerMing
 *DATE:2022/10/27
 *INTRODUCE:采用sp存储 目前只用到简单的 String存储，其他暂时不涉及
 */
object SearchSharedPreUtils {
    private const val PREFERENCES = "search"
    private var preferencesSharedPreferences: SharedPreferences? = null
    fun put(context: Context, key: String?, value: String?) {
        val preferences = getPreferences(context)
        preferences!!.edit().putString(key, value).apply()
    }

    private fun getPreferences(context: Context): SharedPreferences? {
        if (preferencesSharedPreferences == null) {
            preferencesSharedPreferences = context.getSharedPreferences(PREFERENCES, 0)
        }
        return preferencesSharedPreferences
    }

    fun getString(context: Context, key: String?): String? {
        return getString(context, key, "")
    }

    private fun getString(context: Context, key: String?, defaultVal: String?): String? {
        return getPreferences(context)!!.getString(key, defaultVal)
    }

}