package com.udacity.asteroidradar.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PictureOfDayResponse(
    @Json(name = "url") val url: String,
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "title") val title: String,
    @Json(name = "explanation") val explanation: String
)