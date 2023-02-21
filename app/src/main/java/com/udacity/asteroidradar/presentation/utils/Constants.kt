package com.udacity.asteroidradar.presentation.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object Constants {
    private const val API_QUERY_DATE_FORMAT = "yyyy-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov/"

    @SuppressLint("SimpleDateFormat")
    val NASA_DATE_FORMATTER = SimpleDateFormat(API_QUERY_DATE_FORMAT)
}