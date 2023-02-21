package com.udacity.asteroidradar.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.udacity.asteroidradar.data.entities.Asteroid
import java.util.Date

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroids ORDER BY close_approach_date DESC")
    fun getAll(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroids WHERE close_approach_date " +
            "BETWEEN date(:dateStart) AND date(:dateEnd) " +
            "ORDER BY close_approach_date DESC")
    fun getWeekAsteroids(dateStart: Date, dateEnd: Date) : LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroids WHERE close_approach_date IN (:date)")
    fun getAllForToday(date: Date) : LiveData<List<Asteroid>>

    @Insert
    fun insert(asteroid: Asteroid)

    @Update
    fun update(asteroid: Asteroid)

    @Delete
    fun delete(asteroid: Asteroid)

    @Query("DELETE FROM asteroids WHERE close_approach_date < date(:date)")
    fun deletePreviousAsteroids(date: Date)

    @Query("SELECT EXISTS(SELECT * FROM asteroids WHERE id = :id)")
    fun isRowExist(id: Long) : Boolean
}