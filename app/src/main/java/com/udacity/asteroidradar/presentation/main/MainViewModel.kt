package com.udacity.asteroidradar.presentation.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.data.AsteroidRepository
import com.udacity.asteroidradar.data.entities.Asteroid
import com.udacity.asteroidradar.data.mappers.map
import com.udacity.asteroidradar.data.ws.NasaRetrofit
import com.udacity.asteroidradar.domain.model.PictureOfDayModel
import com.udacity.asteroidradar.presentation.utils.Constants.NASA_DATE_FORMATTER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class MainViewModel(context: Context) : AndroidViewModel(context as Application) {

    private val nasaApi = NasaRetrofit.nasaApi
    private val repository: AsteroidRepository = AsteroidRepository(context)

    private var _pictureOfDay = MutableLiveData<PictureOfDayModel>()
    val pictureOfDay: LiveData<PictureOfDayModel> = _pictureOfDay

    private var _queryFrom = MutableLiveData(TODAY_DATA)
    private var _asteroidList = Transformations.switchMap(_queryFrom) {
        when (it) {
            ALL_DATA -> repository.getAll()
            WEEK_DATA -> getWeekAsteroids()
            else -> repository.getAllForToday(Date())
        }
    }
    val asteroidList: LiveData<List<Asteroid>> = _asteroidList

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var isWeekLoaded = false

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun loadTodayAsteroids() {
        _queryFrom.value = TODAY_DATA
    }

    fun loadAllSavedAsteroids() {
        _queryFrom.value = ALL_DATA
    }

    fun loadWeekAsteroids() {
        _queryFrom.value = WEEK_DATA
    }

    private fun getWeekAsteroids(): LiveData<List<Asteroid>> {
        val today = Calendar.getInstance().time
        val weekAgo = Calendar.getInstance()
        weekAgo.add(Calendar.DAY_OF_YEAR, -7)
        if (!isWeekLoaded) {
            loadWeekData(weekAgo.time, today)
            isWeekLoaded = true
        }
        return repository.getWeekAsteroids(weekAgo.time, today)
    }

    private var _progressBarShowing = MutableLiveData(false)
    val progressBarShowing: LiveData<Boolean> = _progressBarShowing

    private fun loadWeekData(dateStart: Date, dateEnd: Date) {
        _progressBarShowing.value = true
        uiScope.launch {
            withContext(Dispatchers.IO) {
                repository.loadAsteroids(
                    dateStart = NASA_DATE_FORMATTER.format(dateStart),
                    dateEnd = NASA_DATE_FORMATTER.format(dateEnd)
                )
            }
            _progressBarShowing.value = false
        }
    }

    fun getPictureOfDay() {
        uiScope.launch {
            _pictureOfDay.value = nasaApi.getPictureOfDay().map()
        }
    }

    companion object {
        const val ALL_DATA = 0
        const val WEEK_DATA = 1
        const val TODAY_DATA = 2
    }
}