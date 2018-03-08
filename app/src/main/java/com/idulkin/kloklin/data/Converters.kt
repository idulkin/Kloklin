package com.idulkin.kloklin.data

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idulkin.kloklin.objects.Interval


/**
 * Created by igor on 3/7/18.
 *
 * Type converters to help Room store ArrayLists
 */
class Converters {

    @TypeConverter
    fun fromString(value: String): ArrayList<Interval> {
        val listType = object : TypeToken<ArrayList<Interval>>() {}.type

        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Interval>): String {
        val gson = Gson()
        val json = gson.toJson(list)

        return json
    }
}