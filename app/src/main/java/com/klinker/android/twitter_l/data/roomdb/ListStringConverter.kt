package com.klinker.android.twitter_l.data.roomdb

import androidx.room.TypeConverter
import java.util.regex.Pattern

class ListStringConverter {

    @TypeConverter
    fun fromListString(values: List<String>) : String {
        return values.joinToString(",") { value -> value.replace(",", "\\,") }
    }

    @TypeConverter
    fun toListString(str: String) : List<String> {
        return str.split(Pattern.compile("[^\\\\],]")).map { value ->
            value.replace("\\,", ",")
        }
    }

}