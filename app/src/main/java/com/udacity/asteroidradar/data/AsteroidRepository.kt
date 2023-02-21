package com.udacity.asteroidradar.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.data.entities.Asteroid
import com.udacity.asteroidradar.data.mappers.map
import com.udacity.asteroidradar.data.ws.NasaRetrofit
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.presentation.utils.Constants
import java.lang.Exception
import java.util.Date

class AsteroidRepository(val context: Context) {
    private val nasaApi = NasaRetrofit.nasaApi
    private val database by lazy {
        AsteroidDatabase.getInstance(context).asteroidDao
    }

    fun getAll(): LiveData<List<Asteroid>> {
        return database.getAll()
    }

    fun getAllForToday(date: Date): LiveData<List<Asteroid>> {
        return database.getAllForToday(date)
    }

    fun getWeekAsteroids(dateStart: Date, dateEnd: Date): LiveData<List<Asteroid>> {
        return database.getWeekAsteroids(dateStart, dateEnd)
    }

    suspend fun loadAsteroids(dateStart: String, dateEnd: String) {
        try {
            val result = nasaApi.listAsteroids(
                startDate = dateStart,
                endDate = dateEnd
            ).map()
            saveResultToDatabase(result)

        } catch (e: Exception) {
            println(e)
        }
    }

    suspend fun loadTodayAsteroids() {
        val todayDate = Constants.NASA_DATE_FORMATTER.format(Date())
        loadAsteroids(todayDate, todayDate)
    }

    private fun saveResultToDatabase(result: List<AsteroidModel>) {
        for (asteroid in result) {
            val dbAsteroid = Asteroid(
                id = asteroid.id,
                codename = asteroid.codename,
                closeApproachDate = asteroid.closeApproachDate,
                absoluteMagnitude = asteroid.absoluteMagnitude,
                estimatedDiameter = asteroid.estimatedDiameter,
                relativeVelocity = asteroid.relativeVelocity,
                distanceFromEarth = asteroid.distanceFromEarth,
                isPotentiallyHazardous = asteroid.isPotentiallyHazardous
            )
            if (database.isRowExist(asteroid.id))
                database.update(asteroid = dbAsteroid)
            else
                database.insert(asteroid = dbAsteroid)
        }
    }

    fun deleteOldAsteroids() {
        database.deletePreviousAsteroids(Date())
    }
}