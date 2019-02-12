package com.klinker.android.twitter_l.data.roomdb

import androidx.room.TypeConverter


class ListStringTypeConverter {


    companion object {

        @TypeConverter
        @JvmStatic fun fromListString(strings: List<String>) : String {
            return strings.joinToString(separator = " ")
        }

        @TypeConverter
        @JvmStatic fun toListString(element: String) : List<String> {
            return element.split(" ")
        }

    }


}