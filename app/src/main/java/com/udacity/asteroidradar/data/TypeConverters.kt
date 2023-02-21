package com.udacity.asteroidradar.data

import androidx.room.TypeConverter
import com.udacity.asteroidradar.presentation.utils.Constants.NASA_DATE_FORMATTER
import java.util.Date

object Converters {

    @TypeConverter
    fun fromString(value: String?): Date {
        return value?.let { NASA_DATE_FORMATTER.parse(it) } ?: Date()
    }

    @TypeConverter
    fun dateToString(date: Date?): String? {
        return date?.let { NASA_DATE_FORMATTER.format(date) } ?: NASA_DATE_FORMATTER.format(Date())
    }
}