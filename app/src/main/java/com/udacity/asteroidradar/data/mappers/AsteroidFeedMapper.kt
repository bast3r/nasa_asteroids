package com.udacity.asteroidradar.data.mappers

import com.udacity.asteroidradar.data.dto.AsteroidFeedResponse
import com.udacity.asteroidradar.data.dto.PictureOfDayResponse
import com.udacity.asteroidradar.data.entities.Asteroid
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.domain.model.PictureOfDayModel
import com.udacity.asteroidradar.presentation.utils.Constants.NASA_DATE_FORMATTER

fun AsteroidFeedResponse.map(): List<AsteroidModel> {
    val list = arrayListOf<AsteroidModel>()
    for (entry in this.nearEarthObjects) {
        for (aster in entry.value) {
            val asteroid = AsteroidModel(
                id = aster.id.toLong(),
                codename = aster.name,
                closeApproachDate = NASA_DATE_FORMATTER.parse(entry.key),
                absoluteMagnitude = aster.absoluteMagnitudeH,
                estimatedDiameter = aster.estimatedDiameter.kilometers.sizeMax,
                relativeVelocity = aster.closeApproachData[0].relativeVelocity.kmPerSec.toDouble(),
                distanceFromEarth = aster.closeApproachData[0].missDistance.astronomical.toDouble(),
                isPotentiallyHazardous = aster.isPotentiallyHazardousAsteroid
            )
            list.add(asteroid)
        }
    }

    return list
}

fun PictureOfDayResponse.map(): PictureOfDayModel {
    return PictureOfDayModel(
        mediaType = mediaType,
        title = title,
        url = url,
        explanation = explanation
    )
}

fun Asteroid.map(): AsteroidModel {
    return AsteroidModel(
        id = id,
        codename = codename,
        closeApproachDate = closeApproachDate,
        absoluteMagnitude = absoluteMagnitude,
        estimatedDiameter = estimatedDiameter,
        relativeVelocity = relativeVelocity,
        distanceFromEarth = distanceFromEarth,
        isPotentiallyHazardous = isPotentiallyHazardous
    )
}
