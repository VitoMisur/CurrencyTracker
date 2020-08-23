package com.vito.misur.currencytracker.database

import androidx.room.TypeConverter
import org.joda.time.DateTime

/**
 * DateTime converters for Room Database
 */
class Converters {
    @TypeConverter
    fun dateTimeToMillis(datetime: DateTime): Long = datetime.millis

    @TypeConverter
    fun millisToDateTime(millis: Long): DateTime = DateTime(millis)
}