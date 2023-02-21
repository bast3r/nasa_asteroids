package com.udacity.asteroidradar.data.ws

import com.udacity.asteroidradar.data.dto.AsteroidFeedResponse
import com.udacity.asteroidradar.data.dto.PictureOfDayResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaWS {

    @GET("/neo/rest/v1/feed")
    suspend fun listAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = API_KEY
    ): AsteroidFeedResponse

    @GET("/planetary/apod")
    suspend fun getPictureOfDay(
        @Query("api_key") apiKey: String = API_KEY
    ): PictureOfDayResponse

    companion object {
        const val API_KEY = "9f1ZvbcTOLuoTxVU1nsReWpQfvohB11aKOZstUxQ"
    }
}