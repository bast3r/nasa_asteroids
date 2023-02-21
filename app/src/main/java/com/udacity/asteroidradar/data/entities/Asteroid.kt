package com.udacity.asteroidradar.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.udacity.asteroidradar.data.Converters
import java.util.Date

@Entity(tableName = "asteroids")
data class Asteroid(
      @PrimaryKey
      val id: Long,
      val codename: String,
      @TypeConverters(Converters::class)
      @ColumnInfo(name = "close_approach_date")
      val closeApproachDate: Date,
      @ColumnInfo(name = "absolute_magnitude")
      val absoluteMagnitude: Double,
      @ColumnInfo(name = "estimated_diameter")
      val estimatedDiameter: Double,
      @ColumnInfo(name = "relative_velocity")
      val relativeVelocity: Double,
      @ColumnInfo(name = "distance_from_earth")
      val distanceFromEarth: Double,
      @ColumnInfo(name = "is_potentially_hazardous")
      val isPotentiallyHazardous: Boolean
)