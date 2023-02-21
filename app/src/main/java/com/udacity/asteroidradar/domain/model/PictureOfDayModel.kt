package com.udacity.asteroidradar.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PictureOfDayModel(
    val mediaType: String,
    val title: String,
    val url: String,
    val explanation: String
): Parcelable