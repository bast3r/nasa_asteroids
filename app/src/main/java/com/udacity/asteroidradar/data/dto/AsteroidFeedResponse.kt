package com.udacity.asteroidradar.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AsteroidFeedResponse(
    @Json(name = "links") val links: Links,
    @Json(name = "element_count") val elementCount: Int,
    @Json(name = "near_earth_objects") val nearEarthObjects: Map<String, List<Asteroid>>
) {

    data class Links(
        @Json(name = "next") val next: String?,
        @Json(name = "previous") val previous: String?,
        @Json(name = "self") val self: String?
    )

    data class Asteroid(
        @Json(name = "links") val links: Links,
        @Json(name = "id") val id: String,
        @Json(name = "neo_reference_id") val neoReferenceId: String,
        @Json(name = "name") val name: String,
        @Json(name = "nasa_jpl_url") val nasaJplUrl: String,
        @Json(name = "absolute_magnitude_h") val absoluteMagnitudeH: Double,
        @Json(name = "estimated_diameter") val estimatedDiameter: EstimatedDiameter,
        @Json(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardousAsteroid: Boolean,
        @Json(name = "close_approach_data") val closeApproachData: List<CloseApproachData>,
        @Json(name = "is_sentry_object") val isSentryObject: Boolean,
    )

    data class EstimatedDiameter(
        @Json(name = "kilometers") val kilometers: DiameterSize,
        @Json(name = "meters") val meters: DiameterSize,
        @Json(name = "miles") val miles: DiameterSize,
        @Json(name = "feet") val feet: DiameterSize
    )

    data class DiameterSize(
        @Json(name = "estimated_diameter_min") val sizeMin: Double,
        @Json(name = "estimated_diameter_max") val sizeMax: Double
    )

    data class CloseApproachData(
        @Json(name = "close_approach_date") val closeApproachDate: String,
        @Json(name = "close_approach_date_full") val closeApproachDateFull: String,
        @Json(name = "epoch_date_close_approach") val epochDateCloseApproach: Long,
        @Json(name = "relative_velocity") val relativeVelocity: RelativeVelocity,
        @Json(name = "miss_distance") val missDistance: MissDistance,
        @Json(name = "orbiting_body") val orbitingBody: String
    )

    data class RelativeVelocity(
        @Json(name = "kilometers_per_second") val kmPerSec: String,
        @Json(name = "kilometers_per_hour") val kmPerHour: String,
        @Json(name = "miles_per_hour") val milesPerHour: String
    )

    data class MissDistance(
        @Json(name = "astronomical") val astronomical: String,
        @Json(name = "lunar") val lunar: String,
        @Json(name = "kilometers") val kilometers: String,
        @Json(name = "miles") val miles: String
    )
}